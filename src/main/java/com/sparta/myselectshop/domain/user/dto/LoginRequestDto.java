package com.sparta.myselectshop.domain.user.dto;

import lombok.Builder;

@Builder
public record LoginRequestDto(
        String username,
        String password
) {
}
