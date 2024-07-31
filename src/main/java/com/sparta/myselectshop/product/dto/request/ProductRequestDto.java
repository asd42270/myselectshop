package com.sparta.myselectshop.product.dto.request;

public record ProductRequestDto(
        String title,
        String image,
        String link,
        Integer lPrice,
        Integer myPrice

) {
}
