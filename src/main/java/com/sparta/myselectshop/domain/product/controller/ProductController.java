package com.sparta.myselectshop.domain.product.controller;

import com.sparta.myselectshop.domain.product.dto.request.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.service.ProductService;
import com.sparta.myselectshop.domain.product.dto.request.ProductRequestDto;
import com.sparta.myselectshop.domain.product.dto.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ProductResponseDto insert(@RequestBody ProductRequestDto requestDto) {
        return productService.insert(requestDto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable("id") Long id, @RequestBody ProductMypriceRequestDto requestDto
    ) {
        return productService.update(id, requestDto);
    }

    @GetMapping()
    public List<ProductResponseDto> getAllMyProducts() {
        return productService.getAllMyProducts();
    }
}
