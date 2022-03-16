package com.nespresso.sofa.interview.cart.services;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nespresso.sofa.interview.cart.model.Cart;

/**
 * If one or more product with code "1000" is purchase, ONE product with code 9000 is offer
 * For each 10 products purchased a gift with code 7000 is offer.
 */
@Service
public class PromotionEngine {
    @Autowired
    private CartService cartService;

    public Cart apply(Cart cart) {
        if(!cart.getProducts().isEmpty()) {
            int quantity = 0;
            for (String key : cart.getProducts().keySet()) {
                quantity += cart.getProducts().get(key);
            }
            int giftTotalQuantity = 0;
            if (quantity >= 10) {
                giftTotalQuantity = quantity / 10;
                Map mutableCart = new LinkedHashMap(cart.getProducts());
                mutableCart.put("7000", giftTotalQuantity);
                cart.setProducts(mutableCart);
            } else {
                Map mutableCart = new LinkedHashMap(cart.getProducts());
                mutableCart.remove("7000");
                cart.setProducts(mutableCart);
            }
            if (cart.getProducts().containsKey("1000")) {
                cart.getProducts().put("9000", 1);

            } else if (cart.getProducts().containsKey("9000")) {
                cart.getProducts().remove("9000");
            }
        }

        return cart;
    }
}
