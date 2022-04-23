package org.example.test.domain;

import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private @Id String id;
    private List<CartItem> cartItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Cart() { }
    public Cart(String id) {
        this(id, new ArrayList<>());
    }

    public Cart(String id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
    }

    public String deleteCartItem(String id) {
        this.cartItems.stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(id))
                .findFirst()
                .map(cartItem -> {
                    this.cartItems.remove(cartItem);
                    return id;
                })
                .orElseGet(() -> {
                    System.out.println("Empty");
                    return null;
                });

        return id;
    }
}
