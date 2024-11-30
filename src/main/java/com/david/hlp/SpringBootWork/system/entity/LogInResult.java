package com.david.hlp.SpringBootWork.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogInResult {
    /**
     * 认证生成的令牌（可选）
     */
    private String accessToken;
}
