package io.osins.matrix.shared.snowflake.id.generator;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带缓冲的ID服务类（支持预分配）
 */
@Slf4j
public class BufferedIdService {
    private final MacBasedSnowflakeIdGenerator idGenerator;
    private final Queue<Long> idBuffer = new ConcurrentLinkedQueue<>();
    private final int bufferSize;
    private final ExecutorService executorService;
    private final AtomicBoolean isFilling = new AtomicBoolean(false);
    private final AtomicInteger pendingTasks = new AtomicInteger(0);

    public BufferedIdService() {
        this(1000); // 默认缓冲区大小为1000
    }

    public BufferedIdService(int bufferSize) {
        if (bufferSize <= 0 || bufferSize > 10000) {
            throw new IllegalArgumentException("bufferSize必须在1到10000之间");
        }
        this.idGenerator = new MacBasedSnowflakeIdGenerator();
        this.bufferSize = bufferSize;
        this.executorService = Executors.newFixedThreadPool(2);
        fillBuffer();
    }

    public BufferedIdService(long datacenterId, int bufferSize) {
        this.idGenerator = new MacBasedSnowflakeIdGenerator(datacenterId);
        this.bufferSize = bufferSize;
        this.executorService = Executors.newSingleThreadExecutor();
        fillBuffer();
    }

    public BufferedIdService(long datacenterId, long machineId, int bufferSize) {
        this.idGenerator = new MacBasedSnowflakeIdGenerator(datacenterId, machineId);
        this.bufferSize = bufferSize;
        this.executorService = Executors.newSingleThreadExecutor();
        fillBuffer();
    }

    /**
     * 填充缓冲区
     */
    private void fillBuffer() {
        try {
            int currentSize = idBuffer.size();
            int needToFill = Math.min(bufferSize - currentSize, 100); // 限制单次填充

            log.info("填充缓冲区，当前大小: {}, 需填充: {}", currentSize, needToFill);

            for (int i = 0; i < needToFill; i++) {
                try {
                    idBuffer.offer(idGenerator.nextId());
                } catch (Exception e) {
                    log.error("填充缓冲区时生成ID失败: {}", e.getMessage(), e);
                    // 继续尝试填充剩余ID，避免中断
                }
            }
        } finally {
            isFilling.set(false);
        }
    }

    /**
     * 获取下一个ID
     *
     * @return 生成的ID
     */
    public long getNextId() {
        Long id = idBuffer.poll();
        if (idBuffer.size() < bufferSize / 10 && !executorService.isShutdown()) {
            if (isFilling.compareAndSet(false, true) && pendingTasks.get() == 0) {
                pendingTasks.incrementAndGet();
                executorService.submit(() -> {
                    try {
                        fillBuffer();
                    } finally {
                        pendingTasks.decrementAndGet();
                        isFilling.set(false);
                    }
                });
            }
        }

        if (id == null) {
            log.warn("缓冲区耗尽，同步填充少量ID");

            id = idBuffer.poll();
            if (id != null) {
                log.warn("缓冲区耗尽, 缓冲池重新获取填充的ID: {}", id);
                return id;
            }

            try {
                id = idGenerator.nextId();
            } catch (Exception e) {
                log.error("无法生成唯一ID: {}", e.getMessage(), e);
                throw new RuntimeException("无法生成唯一ID: " + e.getMessage(), e);
            }

            log.warn("缓冲区耗尽, 直接生成ID: {}", id);

            return id;
        }

        return id;
    }

    /**
     * 获取数据中心ID
     *
     * @return 数据中心ID
     */
    public long getDatacenterId() {
        return idGenerator.getDatacenterId();
    }

    /**
     * 获取机器ID
     *
     * @return 机器ID
     */
    public long getMachineId() {
        return idGenerator.getMachineId();
    }

    /**
     * 关闭服务
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
