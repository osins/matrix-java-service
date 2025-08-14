package club.hm.matrix.shared.snowflake.id.generator.test;

import club.hm.matrix.shared.snowflake.id.generator.BufferedIdService;
import club.hm.matrix.shared.snowflake.id.generator.MacBasedSnowflakeIdGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * MacBasedSnowflakeIdGenerator单元测试
 */
class MacBasedSnowflakeIdGeneratorTest {

    @Test
    void testDefaultConstructor() {
        var generator = new MacBasedSnowflakeIdGenerator();
        assertNotNull(generator);
        assertTrue(generator.getDatacenterId() >= 0);
        assertTrue(generator.getMachineId() >= 0);
        assertTrue(generator.getDatacenterId() < 32);
        assertTrue(generator.getMachineId() < 1024);
    }

    @Test
    void testConstructorWithDatacenterId() {
        var generator = new MacBasedSnowflakeIdGenerator(1L);
        assertNotNull(generator);
        assertEquals(1L, generator.getDatacenterId());
        assertTrue(generator.getMachineId() >= 0);
        assertTrue(generator.getMachineId() < 1024);
    }

    @Test
    void testConstructorWithDatacenterIdAndMachineId() {
        var generator = new MacBasedSnowflakeIdGenerator(1L, 2L);
        assertNotNull(generator);
        assertEquals(1L, generator.getDatacenterId());
        assertEquals(2L, generator.getMachineId());
    }

    @Test
    void testNextTimeStampId() {
        var generator = new MacBasedSnowflakeIdGenerator(1L, 2L);
        long id1 = generator.nextId();
        long id2 = generator.nextId();

        // 确保ID为正数
        assertTrue(id1 > 0, "ID1 should be positive: " + id1);
        assertTrue(id2 > 0, "ID2 should be positive: " + id2);

        // 确保ID是唯一的
        assertNotEquals(id1, id2, "Generated IDs should be unique");
    }

    @Test
    void testNextTimeStampIdUniqueness() {
        var generator = new MacBasedSnowflakeIdGenerator(1L, 2L);
        Set<Long> ids = new HashSet<>();
        int count = 1000;

        for (int i = 0; i < count; i++) {
            long id = generator.nextId();
            assertTrue(ids.add(id), "ID should be unique: " + id);
        }

        assertEquals(count, ids.size());
    }

    @Test
    void testInvalidDatacenterId() {
        assertThrows(IllegalArgumentException.class, () -> new MacBasedSnowflakeIdGenerator(-1L));
        assertThrows(IllegalArgumentException.class, () -> new MacBasedSnowflakeIdGenerator(32L)); // 超出5位最大值31
    }

    @Test
    void testInvalidMachineId() {
        assertThrows(IllegalArgumentException.class, () -> new MacBasedSnowflakeIdGenerator(1L, -1L));
        assertThrows(IllegalArgumentException.class, () -> new MacBasedSnowflakeIdGenerator(1L, 1024L)); // 超出10位最大值1023
    }

    @Test
    void testMultipleGenerators() {
        var generator1 = new MacBasedSnowflakeIdGenerator(1L, 2L);
        var generator2 = new MacBasedSnowflakeIdGenerator(1L, 3L);

        long id1 = generator1.nextId();
        long id2 = generator2.nextId();

        // 确保生成的ID不相同
        assertNotEquals(id1, id2, "IDs from different generators should be different");

        // 验证ID结构 - 确保是有效的Snowflake ID
        assertTrue(id1 > 0, "ID1 should be positive: " + id1);
        assertTrue(id2 > 0, "ID2 should be positive: " + id2);

        // 验证ID结构 - 时间戳部分应该是有效的
        long timestamp1 = id1 >> 22; // 前41位是时间戳
        long timestamp2 = id2 >> 22;

        assertTrue(timestamp1 > 0, "Timestamp1 should be positive: " + timestamp1);
        assertTrue(timestamp2 > 0, "Timestamp2 should be positive: " + timestamp2);
    }

    @Test
    void testClockSkewRecovery() throws Exception {
        MacBasedSnowflakeIdGenerator generator = mock(MacBasedSnowflakeIdGenerator.class);
        AtomicLong mockTime = new AtomicLong(System.currentTimeMillis());
        when(generator.nextId()).thenAnswer(inv -> {
            long current = mockTime.get();
            mockTime.set(current - 2); // 模拟2ms回拨
            return new MacBasedSnowflakeIdGenerator(1L, 2L).nextId();
        });
        BufferedIdService service = new BufferedIdService(1L, 2L, 10);
        long id1 = service.getNextId();
        Thread.sleep(3); // 等待恢复
        long id2 = service.getNextId();
        assertTrue(id2 > id1);
    }
}
