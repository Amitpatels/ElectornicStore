package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDto {
    private String productId;
    private String title;
    private String description;
    private int price;
    private int discount;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
}
