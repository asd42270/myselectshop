package com.sparta.myselectshop.domain.product.dto.request;

import lombok.Builder;

@Builder
public record ProductMypriceRequestDto(
        Integer myPrice
) {
}
