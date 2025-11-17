package io.osins.matrix.shared.snowflake.id.generator.test;

import io.osins.matrix.shared.snowflake.id.generator.IdService;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IdService单元测试
 */
class IdServiceTest {

    @Test
    void testDefaultConstructor() {
        IdService idService = new IdService();
        assertNotNull(idService);
        assertTrue(idService.getDatacenterId() >= 0);
        assertTrue(idService.getMachineId() >= 0);
    }

    @Test
    void testConstructorWithDatacenterId() {
        IdService idService = new IdService(1L);
        assertNotNull(idService);
        assertEquals(1L, idService.getDatacenterId());
        assertTrue(idService.getMachineId() >= 0);
    }

    @Test
    void testConstructorWithDatacenterIdAndMachineId() {
        IdService idService = new IdService(1L, 2L);
        assertNotNull(idService);
        assertEquals(1L, idService.getDatacenterId());
        assertEquals(2L, idService.getMachineId());
    }

    @Test
    void testNextId() {
        IdService idService = new IdService();
        long id1 = idService.nextId();
        long id2 = idService.nextId();

        assertTrue(id1 > 0);
        assertTrue(id2 > 0);
        assertTrue(id2 > id1); // 后生成的ID应该更大
    }

    @Test
    void testNextIdUniqueness() {
        IdService idService = new IdService();
        Set<Long> ids = new HashSet<>();
        int count = 1000;

        for (int i = 0; i < count; i++) {
            long id = idService.nextId();
            assertTrue(ids.add(id), "ID should be unique: " + id);
        }

        assertEquals(count, ids.size());
    }

    @Test
    void testInvalidDatacenterId() {
        assertThrows(IllegalArgumentException.class, () -> new IdService(-1L));
        assertThrows(IllegalArgumentException.class, () -> new IdService(32L)); // 超出5位最大值31
    }

    @Test
    void testInvalidMachineId() {
        assertThrows(IllegalArgumentException.class, () -> new IdService(1L, -1L));
        assertThrows(IllegalArgumentException.class, () -> new IdService(1L, 1024L)); // 超出10位最大值1023
    }
}
