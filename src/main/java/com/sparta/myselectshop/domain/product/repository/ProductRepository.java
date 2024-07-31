package com.sparta.myselectshop.domain.product.repository;

import com.sparta.myselectshop.domain.product.entity.Product;
import com.sparta.myselectshop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findALlByUser(User user);
}
