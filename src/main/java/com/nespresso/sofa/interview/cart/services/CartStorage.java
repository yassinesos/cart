package com.nespresso.sofa.interview.cart.services;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import com.nespresso.sofa.interview.cart.model.Cart;

public class CartStorage {

    private Map<UUID, Cart> carts = new Hashtable<>();

    public Cart loadCart(UUID cartId) {
        return carts.getOrDefault(cartId, new Cart(cartId));
    }

    public Cart saveCart(Cart cart) {
        carts.put(cart.getId(), cart);
        return cart;
    }
}
