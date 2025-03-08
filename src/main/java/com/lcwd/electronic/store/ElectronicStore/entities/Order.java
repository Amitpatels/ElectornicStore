package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    //Pending,Delivered,Dispatch
    private String orderStatus;

    //Not-Paid, Paid //ENUM
    //boolean - false-> NOT-PAID || true=>PAID
    private String paymentStatus;

    private int orderAmount;

    @Column(length= 1000)
    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

}
