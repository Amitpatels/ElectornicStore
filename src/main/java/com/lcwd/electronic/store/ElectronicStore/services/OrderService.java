package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequestDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderUpdateRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(CreateOrderRequestDto orderDto);

    //order remove
    void removeOrder(String orderId);

    //get order of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get all orders for admin
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    OrderDto updateOrder(String orderId, OrderUpdateRequest request);
    //OrderDto updateOrder(String orderId, OrderDto request);
    //order method(logic) related to order
}
