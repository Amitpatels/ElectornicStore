package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.BadApiRequest;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartItemRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

  /*  @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest addItemToCartRequest) {

        int quantity = addItemToCartRequest.getQuantity();
        String productId = addItemToCartRequest.getProductId();

        if(quantity<=0){
            throw new BadApiRequest("Request quantity is not valid !!");
        }

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found in database"));

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found in db against given id!!"));

        Cart cart = cartRepository.findByUser(user).orElseGet(()-> {
            Cart cart1 = new Cart();
            cart1.setCartId(UUID.randomUUID().toString());
            cart1.setCreatedAt(new Date());
            return cart1;
        });

        //AtomicReference<Boolean> updated = new AtomicReference<>(false);
        boolean isProductExistInCart = cart.getItems().stream().anyMatch(item -> item.getProduct().getProductId().equals(productId));
        if(isProductExistInCart){
            List<CartItem> updatedItems = updateCartItem(product,cart,quantity);
            cart.setItems(updatedItems);
            //updated.set(true);
        }else{
            CartItem cartItem = createCartItem(product,cart,quantity);
            cart.getItems().add(cartItem);
        }

        //if(!updated.get()){}

        cart.setUser(user);

        Cart updateCart = cartRepository.save(cart);

        return mapper.map(updateCart,CartDto.class);
    }*/

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequest("Requested quantity is not valid !!");
        }

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //perform cart operations
        //if cart items already present; then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems);

        //create items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(()-> new ResourceNotFoundException("CartItem not found in cart!!"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("cart not found for the given user !!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }



    public CartItem createCartItem(Product product, Cart cart, int quantity){
         return CartItem.builder()
                .quantity(quantity)
                .totalPrice(quantity * product.getDiscountedPrice())
                .cart(cart)
                .product(product)
                .build();
    }


    public List<CartItem> updateCartItem(Product product,Cart cart, int quantity){
        return cart.getItems().stream().map(item -> {
            if(item.getProduct().getProductId().equals(product.getProductId())){
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
            }
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("cart not found for the user !!"));
        return mapper.map(cart,CartDto.class);
    }

}
