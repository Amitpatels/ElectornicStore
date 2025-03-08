package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequestDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderUpdateRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.*;
import com.lcwd.electronic.store.ElectronicStore.exceptions.BadApiRequest;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableResponseUtility;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.OrderRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(CreateOrderRequestDto orderDto) {

        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));

        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow(()-> new ResourceNotFoundException("Cart not found for given id!!"));

        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size() <= 0){
            throw new BadApiRequest("Invalid number of items in cart !!");
        }

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        //orderItems & amount
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

            //cartItem to orderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderAmount(order.getOrderAmount());
        order.setOrderItems(orderItems);

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order not found for given id "));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for given Id!!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> ordersDtos = orders.stream().map(order -> modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());

        return ordersDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageableResponseUtility.getPageableSortObject(pageNumber,pageSize,sortBy,sortDir);
        Page<Order> pageOrders = orderRepository.findAll(pageable);

        return PageableResponseUtility.getPageableResponse(pageOrders,OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderUpdateRequest request) {

        //get the order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadApiRequest("Invalid update data"));
        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus());
        order.setDeliveredDate(request.getDeliveredDate());
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }
}
