package io.osins.matrix.shared.snowflake.id.generator;

import lombok.extern.slf4j.Slf4j;

/**
 * ID服务类
 */
@Slf4j
public class IdService {
    private final MacBasedSnowflakeIdGenerator idGenerator;

    public IdService() {
        this.idGenerator = new MacBasedSnowflakeIdGenerator();
    }

    public IdService(long datacenterId) {
        this.idGenerator = new MacBasedSnowflakeIdGenerator(datacenterId);
    }

    public IdService(long datacenterId, long machineId) {
        this.idGenerator = new MacBasedSnowflakeIdGenerator(datacenterId, machineId);
    }

    /**
     * 生成下一个ID
     * @return 生成的ID
     */
    public long nextId() {
        try {
            return idGenerator.nextId();
        } catch (IllegalStateException e) {
            log.error("ID生成失败: {}", e.getMessage(), e);
            throw new RuntimeException("无法生成唯一ID: " + e.getMessage(), e);
        }
    }

    /**
     * 获取数据中心ID
     * @return 数据中心ID
     */
    public long getDatacenterId() {
        return idGenerator.getDatacenterId();
    }

    /**
     * 获取机器ID
     * @return 机器ID
     */
    public long getMachineId() {
        return idGenerator.getMachineId();
    }
}
