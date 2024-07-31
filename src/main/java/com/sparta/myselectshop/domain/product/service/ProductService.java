package com.sparta.myselectshop.domain.product.service;

import com.sparta.myselectshop.domain.folder.entity.Folder;
import com.sparta.myselectshop.domain.folder.entity.ProductFolder;
import com.sparta.myselectshop.domain.folder.repository.FolderRepository;
import com.sparta.myselectshop.domain.folder.repository.ProductFolderRepository;
import com.sparta.myselectshop.domain.product.dto.request.ProductMypriceRequestDto;
import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.product.repository.ProductRepository;
import com.sparta.myselectshop.domain.naver.dto.ItemDto;
import com.sparta.myselectshop.domain.product.dto.request.ProductRequestDto;
import com.sparta.myselectshop.domain.product.dto.response.ProductResponseDto;
import com.sparta.myselectshop.domain.user.entity.User;
import com.sparta.myselectshop.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;
    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto update(Long id, ProductMypriceRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("상품을 못 찾았어요."));

        Integer myPrice = requestDto.myPrice();
        int price = (myPrice != null) ? myPrice : 0; // 기본값 설정

        if (price < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.");
        }

        product.setMyPrice(requestDto.myPrice());

        return getProductResponseDto(productRepository.save(product));
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
                .lPrice(requestDto.lprice())
                .myPrice(0)
                .user(user)
                .build());

        return getProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user,
                                                int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> productList;

        if (userRoleEnum == UserRoleEnum.USER) {
            // 사용자 권한이 USER 일 경우
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }

        return productList.map(ProductResponseDto::new);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(getProductResponseDto(product));
        }
        return responseDtoList;
    }

    public void addFolder(Long productId, Long folderId, User user) {

        // 1) 상품을 조회합니다.
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("해당 상품이 존재하지 않습니다.")
        );

        // 2) 폴더를 조회합니다.
        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NullPointerException("해당 폴더가 존재하지 않습니다.")
        );

        // 3) 조회한 폴더와 상품이 모두 로그인한 회원의 소유인지 확인합니다.
        if (!product.getUser().getId().equals(user.getId())
                || !folder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        // 중복확인
        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product, folder);

        if (overlapFolder.isPresent()) {
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        // 4) 상품에 폴더를 추가합니다.
        productFolderRepository.save(new ProductFolder(product, folder));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsInFolder(Long folderId, int page, int size, String sortBy, boolean isAsc, User user) {

        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 해당 폴더에 등록된 상품을 가져옵니다.
        Page<Product> products = productRepository.findAllByUserAndProductFolderList_FolderId(user, folderId, pageable);

        Page<ProductResponseDto> responseDtoList = products.map(ProductResponseDto::new);

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
