package com.sparta.myselectshop.domain.product.controller;

import com.sparta.myselectshop.common.security.UserDetailsImpl;
import com.sparta.myselectshop.domain.product.dto.request.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.service.ProductService;
import com.sparta.myselectshop.domain.product.dto.request.ProductRequestDto;
import com.sparta.myselectshop.domain.product.dto.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 관심 상품 등록하기
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 응답 보내기
        return productService.createProduct(requestDto, userDetails.getUser());
    }

    // 관심 상품 조회하기
    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 응답 보내기
        return productService.getProducts(userDetails.getUser());
    }

    // 관리자 조회
    @GetMapping("/admin/products")
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
