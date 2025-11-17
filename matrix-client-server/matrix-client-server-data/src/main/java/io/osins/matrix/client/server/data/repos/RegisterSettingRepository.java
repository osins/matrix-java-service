package io.osins.matrix.client.server.data.repos;

import io.osins.matrix.client.server.data.entity.RegisterSetting;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * RegisterSetting 数据访问层接口
 * 提供对 matrix.matrix_register_setting 表的响应式 CRUD 操作
 */
@Repository
public interface RegisterSettingRepository extends ReactiveCrudRepository<RegisterSetting, Long> {

    /**
     * 根据客户端名称查找设置
     */
    Mono<RegisterSetting> findByClientName(String clientName);

    /**
     * 根据客户端名称查找设置（英文美国版）
     */
    Mono<RegisterSetting> findByClientNameEnUs(String clientNameEnUs);

    /**
     * 根据客户端名称查找设置（英文英国版）
     */
    Mono<RegisterSetting> findByClientNameEnGb(String clientNameEnGb);

    /**
     * 根据客户端名称查找设置（法语版）
     */
    Mono<RegisterSetting> findByClientNameFr(String clientNameFr);
}