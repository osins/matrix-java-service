package club.hm.matrix.shared.snowflake.id.generator;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 改进版基于MAC的Snowflake ID生成器（天然正数）
 */
@Slf4j
public class MacBasedSnowflakeIdGenerator {
    private final static String ALGORITHM = "SHA-256";
    private final static long START_TIMESTAMP = 1640995200000L; // 2022-01-01

    private final static long SEQUENCE_BIT = 12;   // 12位序列号
    private final static long MACHINE_BIT = 8;    // 10位机器ID
    private final static long DATACENTER_BIT = 2;  // 5位数据中心ID
    private final static long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT + DATACENTER_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long MACHINE_LEFT = SEQUENCE_BIT;

    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);

    private final long datacenterId;
    private final long machineId;

    private final AtomicLong state = new AtomicLong(0);

    public MacBasedSnowflakeIdGenerator() {
        long[] ids = generateDatacenterAndMachineId();
        this.datacenterId = ids[0];
        this.machineId = ids[1];
    }

    public MacBasedSnowflakeIdGenerator(long datacenterId) {
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_NUM)
            throw new IllegalArgumentException("datacenterId must be 0-" + MAX_DATACENTER_NUM);
        this.datacenterId = datacenterId;
        this.machineId = generateMachineId();
    }

    public MacBasedSnowflakeIdGenerator(long datacenterId, long machineId) {
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_NUM)
            throw new IllegalArgumentException("datacenterId must be 0-" + MAX_DATACENTER_NUM);
        if (machineId < 0 || machineId > MAX_MACHINE_NUM)
            throw new IllegalArgumentException("machineId must be 0-" + MAX_MACHINE_NUM);
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    public long nextId() {
        int spin = 0;
        long failCount = 0;
        while (true) {
            long current = System.currentTimeMillis();
            final long prev = state.get();
            final long prevTs = prev >>> 12;
            final long prevSeq = prev & MAX_SEQUENCE;

            if (current < prevTs) {
                long offset = prevTs - current;
                if (offset <= 5) {
                    current = nextTimeStamp(prevTs);
                } else {
                    current = prevTs + 1;
                }
            }

            long newSeq = (current == prevTs) ? (prevSeq + 1) & MAX_SEQUENCE : 0;

            if (newSeq == 0 && current == prevTs) {
                current = nextTimeStamp(prevTs + 1);
            }

            long newState = (current << 12) | newSeq;
            if (state.compareAndSet(prev, newState)) {
                var id = (((current - START_TIMESTAMP) << TIMESTAMP_LEFT)
                        | (datacenterId << DATACENTER_LEFT)
                        | (machineId << MACHINE_LEFT)
                        | newSeq);
                if (id <= 0)
                    throw new RuntimeException("ID 生成器生成 ID 失败，请检查时钟是否正常");

                return id;
            }

            failCount++;
            if (++spin >= 100) { // 连续 100 次失败
                if (failCount % 1000 == 0) { // 每 1000 次失败打一次日志
                    log.warn("ID 生成 CAS 竞争激烈，总失败 {} 次，线程：{}", failCount, Thread.currentThread().getName());
                }

                Thread.yield(); // 让出 CPU 时间片
                spin = 0; // 重置计数
            }
        }
    }


    private long nextTimeStamp(long timestamp) {
        long now = System.currentTimeMillis();
        long maxWait = 5000; // 最大等待 5 秒
        long start = now;
        while (now < timestamp && (now - start) < maxWait) {
            try {
                Thread.sleep(1); // 避免忙等待
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("等待时钟时被中断", e);
            }
            now = System.currentTimeMillis();
        }
        if (now < timestamp) {
            throw new IllegalStateException("时钟回拨过大: " + (timestamp - now) + " 毫秒");
        }
        return now;
    }

    private long[] generateDatacenterAndMachineId() {
        try {
            String mac = getMacAddress();
            long pid = getProcessId();
            String unique = mac + pid;
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = md.digest(unique.getBytes());
            long datacenterId = ((hash[0] & 0xFFL) << 8 | (hash[1] & 0xFFL)) % (MAX_DATACENTER_NUM + 1);
            long machineId = ((hash[2] & 0xFFL) << 8 | (hash[3] & 0xFFL)) % (MAX_MACHINE_NUM + 1);
            return new long[]{datacenterId, machineId};
        } catch (Exception e) {
            log.error("生成数据中心和机器ID失败: {}", e.getMessage());
            long datacenterId = ThreadLocalRandom.current().nextLong(MAX_DATACENTER_NUM + 1);
            long machineId = ThreadLocalRandom.current().nextLong(MAX_MACHINE_NUM + 1);
            return new long[]{datacenterId, machineId};
        }
    }

    private long generateMachineId() {
        try {
            String mac = getMacAddress();
            long pid = getProcessId();
            String unique = mac + pid;

            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = md.digest(unique.getBytes());
            return ((hash[0] & 0xFFL) << 8 | (hash[1] & 0xFFL)) % (MAX_MACHINE_NUM + 1);
        } catch (Exception e) {
            return ThreadLocalRandom.current().nextLong(MAX_MACHINE_NUM + 1);
        }
    }

    private String getMacAddress() throws SocketException {
        // 优先检查环境变量（例如容器ID）
        var containerId = Optional.ofNullable(System.getenv("CONTAINER_ID")).orElse(System.getenv("HOSTNAME"));
        if (containerId != null && !containerId.isEmpty()) {
            log.info("获取Mac address, 优先使用容器ID或主机名: {}", containerId);
            return containerId;
        }

        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            if (ni.isLoopback() || ni.isVirtual() || !ni.isUp()) continue;
            byte[] mac = ni.getHardwareAddress();
            if (mac != null) {
                StringBuilder sb = new StringBuilder();
                for (byte b : mac) sb.append(String.format("%02X", b));
                System.out.println("Using MAC from interface: " + ni.getName());
                return sb.toString();
            }
        }
        // 回退到主机名或 UUID
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return UUID.randomUUID().toString();
        }
    }

    private long getProcessId() {
        String name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        try {
            return Long.parseLong(name.split("@")[0]);
        } catch (Exception e) {
            return ThreadLocalRandom.current().nextLong(1, 100000);
        }
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getMachineId() {
        return machineId;
    }
}
