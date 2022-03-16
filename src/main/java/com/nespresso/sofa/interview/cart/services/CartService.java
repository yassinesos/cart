package com.nespresso.sofa.interview.cart.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.nespresso.sofa.interview.cart.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("cartService")
public class CartService {

    @Autowired
    private PromotionEngine promotionEngine;
    @Autowired
    private CartStorage cartStorage;




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
        synchronized (this) {
            final Cart cart = cartStorage.loadCart(cartId);
            if (quantity == 0) return false;
            cart.getProducts().merge(productCode, quantity, Integer::sum);
            if (cart.getProducts().get(productCode) < 0) {
                return false;
            } else {
                cartStorage.saveCart(promotionEngine.apply(cart));
                return true;
            }
        }

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
    @Async
    public boolean set(UUID cartId, String productCode, int quantity) {
        synchronized (this) {
            final Cart cart = cartStorage.loadCart(cartId);
            if (cart.getProducts().containsKey(productCode) && cart.getProducts().get(productCode) == quantity) {
                return false;
            }
            if (quantity <= 0) {
                Map copy = new LinkedHashMap(cart.getProducts());
                copy.remove(productCode);
                cart.setProducts(copy);
                cartStorage.saveCart(promotionEngine.apply(cart));
                return true;
            }
            cart.getProducts().put(productCode, quantity);
            cartStorage.saveCart(promotionEngine.apply(cart));
            return true;
        }
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
