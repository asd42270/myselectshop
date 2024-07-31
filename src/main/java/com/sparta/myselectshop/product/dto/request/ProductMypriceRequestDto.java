package com.sparta.myselectshop.product.dto.request;

import lombok.Builder;

@Builder
public record ProductMypriceRequestDto(
        Integer myPrice
) {
}
