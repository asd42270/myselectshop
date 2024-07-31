package com.sparta.myselectshop.domain.user.dto;

import lombok.Builder;

@Builder
public record UserInfoDto(
        String username,
        Boolean isAdmin
) {
}
