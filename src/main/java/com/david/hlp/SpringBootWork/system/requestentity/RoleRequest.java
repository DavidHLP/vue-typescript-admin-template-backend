package com.david.hlp.SpringBootWork.system.requestentity;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    String roleName;
    String description;
    List<Long> permissionsArrId;
    Boolean status;
}
