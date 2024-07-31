package com.sparta.myselectshop.domain.folder.repository;

import com.sparta.myselectshop.domain.folder.entity.Folder;
import com.sparta.myselectshop.domain.folder.entity.ProductFolder;
import com.sparta.myselectshop.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}
