package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {
    private int oderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;
}
