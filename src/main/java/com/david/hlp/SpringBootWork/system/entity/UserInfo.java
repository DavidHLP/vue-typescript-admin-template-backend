package com.david.hlp.SpringBootWork.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String name;
    private String avatar;
    private String introduction;
    private String email;
    private Role roles;
}
