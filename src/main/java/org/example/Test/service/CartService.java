package org.example.test.service;

import org.example.test.domain.Cart;
import org.example.test.domain.CartItem;
import org.example.test.repository.CartRepository;
import org.example.test.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartService {
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    CartService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> addToCart(String cartId, String id) {
        return this.cartRepository.findById(cartId)
                .log("foundCart")
                .defaultIfEmpty(new Cart(cartId))
                .log("emptyCart")
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(id))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        })
                        .orElseGet(() -> this.itemRepository.findById(id)
                                .map(CartItem::new)
                                .doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                                .map(cartItem -> cart)))
                .log("cartWithAnotherItem")
                .flatMap(this.cartRepository::save)
                .log("savedCart");
    }

    public Mono<Cart> deleteToCart(String cartId, String id) {
        return this.cartRepository.findById(cartId)
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(id))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.decrement();
                            return Mono.just(cart);
                        })
                        .orElseGet(() -> this.itemRepository.findById(id)
                                .map(CartItem::new)
                                .doOnNext(cartItem -> {
                                    cartItem.decrement();
                                    cart.getCartItems().add(cartItem);
                                })
                                .map(cartItem -> cart)))
                .flatMap(this.cartRepository::save);
    }

    public Mono<Cart> deleteCartItem(String cartId, String id) {
        return this.cartRepository.findById(cartId)
                .flatMap(cart -> {
                    cart.deleteCartItem(id);
                    return Mono.just(cart);
                })
                .flatMap(this.cartRepository::save);
    }
}
