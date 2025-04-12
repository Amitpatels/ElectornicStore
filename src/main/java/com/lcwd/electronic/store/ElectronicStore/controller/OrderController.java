package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.config.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequestDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderUpdateRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.dtos.common.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

        @Autowired
        private OrderService orderService;

        @PostMapping
        public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequestDto createOrderRequestDto){
            OrderDto orderDto = orderService.createOrder(createOrderRequestDto);
            return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
        }

        @PreAuthorize("hasRole('"+ AppConstants.ROLE_ADMIN +"')")
        @DeleteMapping("/{userId}")
        public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String userId){
            orderService.removeOrder(userId);
            ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                    .status(HttpStatus.OK)
                    .message("Oder is removed !!")
                    .success(true)
                    .build();
            return new ResponseEntity<>(responseMessage,HttpStatus.OK);
        }

        @PreAuthorize("hasAnyRole('"+AppConstants.ROLE_NORMAL+"','"+AppConstants.ROLE_ADMIN+"')")
        @GetMapping("/user/{userId}")
        public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
            List<OrderDto> orderDtos = orderService.getOrdersOfUser(userId);
            return new ResponseEntity<>(orderDtos,HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<PageableResponse<OrderDto>> getOrders(
                @RequestParam(value="pageNumber", defaultValue = "0", required = false) int pageNumber,
                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                @RequestParam(value = "sortBy", defaultValue = "orderStatus", required = false) String sortBy,
                @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
        ){
            PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber,pageSize,sortBy,sortDir);
            return new ResponseEntity<>(orders,HttpStatus.OK);
        }

    //Assignment Solution: update order
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable("orderId") String orderId,
            @RequestBody OrderUpdateRequest request
    ) {

        OrderDto dto = this.orderService.updateOrder(orderId,request);
        return ResponseEntity.ok(dto);
    }


}
