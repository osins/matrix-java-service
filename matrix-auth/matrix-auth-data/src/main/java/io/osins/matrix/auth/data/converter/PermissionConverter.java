package io.osins.matrix.auth.data.converter;

import io.osins.matrix.auth.data.dto.PermissionDTO;
import io.osins.matrix.auth.data.entity.SysPermission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SysPermission 和 PermissionDTO 之间的转换器
 */
@Component
public class PermissionConverter {

    /**
     * 将 SysPermission 实体转换为 PermissionDTO
     *
     * @param entity SysPermission 实体
     * @return PermissionDTO 对象，如果输入为 null 则返回 null
     */
    public PermissionDTO toDTO(SysPermission entity) {
        if (entity == null) {
            return null;
        }

        PermissionDTO dto = new PermissionDTO();
        dto.setId(entity.id());
        dto.setCode(entity.code());
        dto.setName(entity.description()); // 将 description 映射到 name

        return dto;
    }

    /**
     * 将 PermissionDTO 转换为 SysPermission 实体
     *
     * @param dto PermissionDTO 对象
     * @return SysPermission 实体，如果输入为 null 则返回 null
     */
    public SysPermission toEntity(PermissionDTO dto) {
        if (dto == null) {
            return null;
        }

        return new SysPermission(
                dto.getId(),
                dto.getCode(),
                dto.getName(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    /**
     * 批量将 SysPermission 实体列表转换为 PermissionDTO 列表
     *
     * @param entities SysPermission 实体列表
     * @return PermissionDTO 列表
     */
    public List<PermissionDTO> toDTOList(List<SysPermission> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 批量将 PermissionDTO 列表转换为 SysPermission 实体列表
     *
     * @param dtos PermissionDTO 列表
     * @return SysPermission 实体列表
     */
    public List<SysPermission> toEntityList(List<PermissionDTO> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}