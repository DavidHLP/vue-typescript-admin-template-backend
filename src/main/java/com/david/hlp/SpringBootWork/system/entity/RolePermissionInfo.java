package com.david.hlp.SpringBootWork.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionInfo {
    private Long id;
    private Long roleId;
    private Long permissionId;

    private Action action;

    public enum Action{
        CREATE,
        UPDATE,
        DELETE
    }
}
