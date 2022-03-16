package com.nespresso.sofa.interview.cart.services;

import com.nespresso.sofa.interview.cart.model.Cart;

/**
 * If one or more product with code "1000" is purchase, ONE product with code 9000 is offer
 * For each 10 products purchased a gift with code 7000 is offer.
 */
public class PromotionEngine {

    public Cart apply(Cart cart) {
        return cart;
    }
}
