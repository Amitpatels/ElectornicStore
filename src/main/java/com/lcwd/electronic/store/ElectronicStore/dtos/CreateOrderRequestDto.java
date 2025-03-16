package com.lcwd.electronic.store.ElectronicStore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    @NotBlank(message = "Cart Id is required!!")
    private String cartId;
    @NotBlank(message = "User Id is required!!")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="UNPAID";
    @NotBlank(message = "Billing Address is required!!")
    private String billingAddress;
    @NotBlank(message = "Billing Phone is required!!")
    private String billingPhone;
    @NotBlank(message = "Billing Name is required!!")
    private String billingName;
}
