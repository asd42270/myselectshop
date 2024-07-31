package com.sparta.myselectshop.product.controller;

import com.sparta.myselectshop.product.dto.request.ProductMypriceRequestDto;
import com.sparta.myselectshop.product.dto.request.ProductRequestDto;
import com.sparta.myselectshop.product.dto.response.ProductResponseDto;
import com.sparta.myselectshop.product.service.ProductService;
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
