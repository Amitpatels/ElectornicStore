package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "categories")
public class Category {

    @Id
    private String categoryId;
    @Column(name= "category_title", length = 60, nullable = false)
    private String title;
    @Column(name= "category_desc", length=50)
    private String description;
    private String coverImage;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
