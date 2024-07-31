package com.sparta.myselectshop.domain.product.entity;

import com.sparta.myselectshop.domain.folder.entity.ProductFolder;
import com.sparta.myselectshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private Integer lPrice;

    @Column(nullable = false)
    @Setter
    private Integer myPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "product")
    private List<ProductFolder> productFolderList = new ArrayList<>();

}
