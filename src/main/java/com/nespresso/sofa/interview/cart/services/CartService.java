package com.nespresso.sofa.interview.cart.services;

import java.util.HashMap;
import java.util.UUID;

import com.nespresso.sofa.interview.cart.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;

public class CartService {

    @Autowired
    private final PromotionEngine promotionEngine;

    @Autowired
    private final CartStorage cartStorage;

    public CartService(PromotionEngine promotionEngine, CartStorage cartStorage) {
        this.promotionEngine = promotionEngine;
        this.cartStorage = cartStorage;
    }

    /**
     * Add a quantity of a product to the cart and store the cart
     *
     * @param cartId
     *     The cart ID
     * @param productCode
     *     The product code
     * @param quantity
     *     Quantity must be added
     * @return True if card has been modified
     */
    public boolean add(UUID cartId, String productCode, int quantity) {
        final Cart cart = cartStorage.loadCart(cartId);

        if(cart == null) {
            HashMap<String,Integer> products = new HashMap<String,Integer>();
            products.put(productCode, quantity);
            cartStorage.saveCart(new Cart(cartId, products));
            return true;
        }
        cartStorage.saveCart(cart);

        return false;
    }

    /**
     * Set a quantity of a product to the cart and store the cart
     *
     * @param cartId
     *     The cart ID
     * @param productCode
     *     The product code
     * @param quantity
     *     The new quantity
     * @return True if card has been modified
     */
    public boolean set(UUID cartId, String productCode, int quantity) {
        final Cart cart = cartStorage.loadCart(cartId);
        cartStorage.saveCart(cart);
        return false;
    }

    /**
     * Return the card with the corresponding ID
     *
     * @param cartId
     * @return
     */
    public Cart get(UUID cartId) {
        return cartStorage.loadCart(cartId);
    }
}
