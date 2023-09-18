package com.concungshop.dto;

import com.concungshop.entity.Category;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    private String name;

    private Category category;

    private String description;

    private String avatar;

    private Double price;

    private Integer quantity;

    private boolean activated;

    private MultipartFile path;

}
