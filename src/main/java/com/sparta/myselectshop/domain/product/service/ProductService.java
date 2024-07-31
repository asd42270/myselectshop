package com.sparta.myselectshop.domain.product.service;

import com.sparta.myselectshop.domain.product.dto.request.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.product.repository.ProductRepository;
import com.sparta.myselectshop.domain.naver.dto.ItemDto;
import com.sparta.myselectshop.domain.product.dto.request.ProductRequestDto;
import com.sparta.myselectshop.domain.product.dto.response.ProductResponseDto;
import com.sparta.myselectshop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto insert(ProductRequestDto requestDto) {

        Product savedProduct = productRepository.save(Product.builder()
                .title(requestDto.title())
                .image(requestDto.image())
                .link(requestDto.link())
                .lPrice(requestDto.lPrice())
                .myPrice(requestDto.myPrice())
                .build());

        return getProductResponseDto(savedProduct);
    }

    public ProductResponseDto update(Long id, ProductMypriceRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("상품을 못 찾았어요."));

        if (requestDto.myPrice() < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.");
        }

        product.setMyPrice(requestDto.myPrice());

        return getProductResponseDto(productRepository.save(product));
    }

    public List<ProductResponseDto> getAllMyProducts() {
        return productRepository.findAll().stream()
                .map(this::getProductResponseDto)
                .collect(Collectors.toList());
    }

    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
        );
        product.setMyPrice(itemDto.getLprice());
    }

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(Product.builder()
                .title(requestDto.title())
                .image(requestDto.image())
                .link(requestDto.link())
                .lPrice(requestDto.lPrice())
                .myPrice(requestDto.myPrice())
                .build());

        return getProductResponseDto(product);
    }

    public List<ProductResponseDto> getProducts(User user) {
        List<Product> productList = productRepository.findALlByUser(user);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(getProductResponseDto(product));
        }
        return responseDtoList;
    }

    public List<ProductResponseDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(getProductResponseDto(product));
        }
        return responseDtoList;
    }

    private ProductResponseDto getProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .image(product.getImage())
                .link(product.getLink())
                .lprice(product.getLPrice())
                .myprice(product.getMyPrice())
                .build();
    }
}
