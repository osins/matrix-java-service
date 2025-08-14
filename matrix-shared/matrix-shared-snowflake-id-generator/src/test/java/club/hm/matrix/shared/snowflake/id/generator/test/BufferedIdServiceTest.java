package club.hm.matrix.shared.snowflake.id.generator.test;


import club.hm.matrix.shared.snowflake.id.generator.BufferedIdService;
import club.hm.matrix.shared.snowflake.id.generator.IdService;
import club.hm.matrix.shared.snowflake.id.generator.MacBasedSnowflakeIdGenerator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * BufferedIdService单元测试
 */
@Slf4j
class BufferedIdServiceTest {

    private BufferedIdService bufferedIdService;

    @BeforeEach
    void setUp() {
        bufferedIdService = new BufferedIdService(100); // 使用较小的缓冲区便于测试
    }

    @Test
    void testConstructorWithDefaultBufferSize() {
        BufferedIdService service = new BufferedIdService();
        assertNotNull(service);
        assertTrue(service.getDatacenterId() >= 0);
        assertTrue(service.getMachineId() >= 0);
    }

    @Test
    void testConstructorWithCustomBufferSize() {
        BufferedIdService service = new BufferedIdService(50);
        assertNotNull(service);
        assertTrue(service.getDatacenterId() >= 0);
        assertTrue(service.getMachineId() >= 0);
    }

    @Test
    void testConstructorWithDatacenterIdAndBufferSize() {
        BufferedIdService service = new BufferedIdService(1L, 50);
        assertNotNull(service);
        assertEquals(1L, service.getDatacenterId());
        assertTrue(service.getMachineId() >= 0);
    }

    @Test
    void testConstructorWithAllParameters() {
        BufferedIdService service = new BufferedIdService(1L, 2L, 50);
        assertNotNull(service);
        assertEquals(1L, service.getDatacenterId());
        assertEquals(2L, service.getMachineId());
    }

    @Test
    void testGetNextId() {
        long id1 = bufferedIdService.getNextId();
        long id2 = bufferedIdService.getNextId();

        assertTrue(id1 > 0);
        assertTrue(id2 > 0);
        assertNotEquals(id1, id2); // ID应该是唯一的
    }

    @Test
    void testGetNextIdUniqueness() {
        Set<Long> ids = new HashSet<>();
        int count = 1000;

        for (int i = 0; i < count; i++) {
            long id = bufferedIdService.getNextId();
            assertTrue(ids.add(id), "ID should be unique: " + id);
        }

        assertEquals(count, ids.size());
    }

    @Test
    void testConcurrentGetNextId() throws InterruptedException {
        int threadCount = 10;
        int idsPerThread = 100;
        var ids = Collections.synchronizedList(new LinkedList<Long>());
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < idsPerThread; j++) {
                        ids.add(bufferedIdService.getNextId());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 等待所有线程完成
        executor.shutdown();

        assertEquals(threadCount * idsPerThread, ids.size(), "All IDs should be unique");

        var idCountMap = ids.stream()
                .collect(Collectors.groupingBy(
                        id -> id,
                        Collectors.counting()
                ));

        log.info("ID 计数: {}", idCountMap);
        idCountMap.forEach((id, count) -> {
            assertTrue(count == 1, "ID should be unique: " + id);
        });

        assertFalse(idCountMap.values().stream().anyMatch(count -> count > 1), "存在重复的id");
    }

    @Test
    void testGetDatacenterId() {
        long datacenterId = bufferedIdService.getDatacenterId();
        assertTrue(datacenterId >= 0);
        assertTrue(datacenterId < 32); // 5位数据中心ID最大值为31
    }

    @Test
    void testGetMachineId() {
        long machineId = bufferedIdService.getMachineId();
        assertTrue(machineId >= 0);
        assertTrue(machineId < 1024); // 10位机器ID最大值为1023
    }

    @Test
    void testShutdown() {
        // 确保服务可以正常关闭
        assertDoesNotThrow(() -> bufferedIdService.shutdown());
    }

    @Test
    void testBufferRefill() {
        // 取出大部分ID以触发缓冲区重新填充
        for (int i = 0; i < 95; i++) {
            bufferedIdService.getNextId();
        }

        // 等待一段时间让异步填充完成
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 继续获取ID
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                bufferedIdService.getNextId();
            }
        });
    }

    @Test
    void testClockSkew() {
        MacBasedSnowflakeIdGenerator mockGenerator = mock(MacBasedSnowflakeIdGenerator.class);
        when(mockGenerator.nextId()).thenThrow(new IllegalStateException("时钟回拨过大"));
        BufferedIdService service = new BufferedIdService(1L, 1L, 10);
        assertThrows(RuntimeException.class, service::getNextId);
    }

    @Test
    void testConcurrentPerformance() throws InterruptedException {
        int threadCount = 10;
        int idsPerThread = 1000;
        Set<Long> ids = ConcurrentHashMap.newKeySet();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < idsPerThread; j++) {
                        ids.add(bufferedIdService.getNextId());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        long duration = System.currentTimeMillis() - startTime;
        assertEquals(threadCount * idsPerThread, ids.size());
        assertTrue(duration < 1000, "生成10000个ID应在1秒内完成");
    }
}