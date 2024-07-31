package com.sparta.myselectshop.domain.product.dto.request;

public record ProductRequestDto(
        String title,
        String image,
        String link,
        Integer lprice

) {
}
