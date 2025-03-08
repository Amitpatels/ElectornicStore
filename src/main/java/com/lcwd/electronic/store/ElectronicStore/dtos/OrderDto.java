package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDto {
    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="UNPAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate= new Date();
    private Date deliveredDate;
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
