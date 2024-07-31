package com.sparta.myselectshop.product.dto.response;

import lombok.Builder;

@Builder
public record ProductResponseDto(
        Long id,
        String title,
        String link,
        String image,
        Integer lprice,
        Integer myprice
) {
}
