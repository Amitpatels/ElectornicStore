package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest addItemToCartRequest) {

        int quantity = addItemToCartRequest.getQuantity();
        String productId = addItemToCartRequest.getProductId();

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found in database"));

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found in db against given id!!"));

        Cart cart = cartRepository.findByUser(user).orElseGet(()-> {
            Cart cart1 = new Cart();
            cart1.setCartId(UUID.randomUUID().toString());
            cart1.setCreatedDate(new Date());
            return cart1;
        });

        //AtomicReference<Boolean> updated = new AtomicReference<>(false);
        boolean isProductExistInCart = cart.getCartItems().stream().anyMatch(item -> item.getProduct().getProductId().equals(productId));
        if(isProductExistInCart){
            List<CartItem> updatedItems = updateCartItem(product,cart,quantity);
            cart.setCartItems(updatedItems);
            //updated.set(true);
        }else{
            CartItem cartItem = createCartItem(product,cart,quantity);
            cart.getCartItems().add(cartItem);
        }

        //if(!updated.get()){}

        cart.setUser(user);

        Cart updateCart = cartRepository.save(cart);

        return mapper.map(updateCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

    }

    @Override
    public void clearCart(String userId) {

    }

    public CartItem createCartItem(Product product, Cart cart, int quantity){
         return CartItem.builder()
                .quantity(quantity)
                .totalPrice(quantity * product.getPrice())
                .cart(cart)
                .product(product)
                .build();
    }


    public List<CartItem> updateCartItem(Product product,Cart cart, int quantity){
        return cart.getCartItems().stream().map(item -> {
            if(item.getProduct().getProductId().equals(product.getProductId())){
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getPrice());
            }
            return item;
        }).collect(Collectors.toList());
    }

}
