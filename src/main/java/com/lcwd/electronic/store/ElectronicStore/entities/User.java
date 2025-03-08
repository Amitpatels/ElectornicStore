package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private String userId;
    @Column(name="user_name")
    private String name;
    @Column(name="user_email", unique = true)
    private String email;
    @Column(name="user_password",length = 10)
    private String password;
    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name="user_profile_image_name")
    private String userProfileImageName;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Cart cart;
}
