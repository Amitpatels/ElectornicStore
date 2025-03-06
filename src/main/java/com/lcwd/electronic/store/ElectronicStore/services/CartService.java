package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;

//add item to cart:
//case1: cart for user is not available: we will create a cart then add the items
//case2: cart available add the item to cart
public interface CartService {

    CartDto addItemToCart(String userId, AddItemToCartRequest addItemToCartRequest);

    void removeItemFromCart(String userId, int cartItem);

    void clearCart(String userId);

    public CartDto getCartByUser(String userId);
}
