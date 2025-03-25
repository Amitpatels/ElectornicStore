package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String userId;
    @Column(name="user_name")
    private String name;
    @Column(name="user_email", unique = true)
    private String email;
    @Column(name="user_password",length = 100)
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
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
