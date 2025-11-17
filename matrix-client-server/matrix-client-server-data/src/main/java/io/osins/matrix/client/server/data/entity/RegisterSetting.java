package io.osins.matrix.client.server.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 客户端注册设置实体类
 * 对应数据库表 matrix.matrix_register_setting
 */
@Table("matrix.matrix_register_setting")
public record RegisterSetting(
        @Id Long id,

        @Column("client_name")
        String clientName,

        @Column("client_name_en_us")
        String clientNameEnUs,

        @Column("client_name_en_gb")
        String clientNameEnGb,

        @Column("client_name_fr")
        String clientNameFr,

        @Column("tos_uri")
        String tosUri,

        @Column("tos_uri_fr")
        String tosUriFr,

        @Column("policy_uri")
        String policyUri,

        @Column("policy_uri_fr")
        String policyUriFr,

        @Column("created_time")
        LocalDateTime createdTime,

        @Column("updated_time")
        LocalDateTime updatedTime,

        @Column("deleted_at")
        LocalDateTime deletedAt
) {}