package com.nespresso.sofa.interview.cart.services;

import static com.nespresso.sofa.interview.cart.matcher.CartMatcher.containProduct;
import static com.nespresso.sofa.interview.cart.matcher.CartMatcher.emptyCart;
import static java.util.UUID.randomUUID;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.nespresso.sofa.interview.cart.model.Cart;

public class PromotionEngineTest {

    public static final String PRODUCT_WITH_PROMOTION = "1000";
    public static final String PROMOTION = "9000";
    public static final String GIFT = "7000";

    private final String randomProduct = randomUUID().toString();

    public PromotionEngine promotionEngine = new PromotionEngine();

    @Test
    public void empty_cart() {
        assertThat(promotionEngine.apply(new Cart()), emptyCart());
    }

    @Test
    public void no_promotion() {
        Map<String, Integer> products = mapBuilder().put(randomProduct, 1).build();
        assertThat(promotionEngine.apply(new Cart(products)), not(containProduct(PROMOTION)));
    }

    @Test
    public void promotion_can_not_be_add() {
        Map<String, Integer> products = mapBuilder().put(PROMOTION, 1).build();
        assertThat(promotionEngine.apply(new Cart(products)), not(containProduct(PROMOTION)));
    }

    @Test
    public void product_promotion() {
        Map<String, Integer> products = new HashMap<>(mapBuilder().put(PRODUCT_WITH_PROMOTION, 1).build());
        assertThat(promotionEngine.apply(new Cart(products)), containProduct(PROMOTION, 1));
        products.put(PRODUCT_WITH_PROMOTION, 2);
        assertThat(promotionEngine.apply(new Cart(products)), containProduct(PROMOTION, 1));
    }

    @Test
    public void gift() {
        Map<String, Integer> products = mapBuilder().put(randomProduct, 1).build();
        assertThat(promotionEngine.apply(new Cart(products)), not(containProduct(GIFT)));
        products = mapBuilder().put(randomProduct, 10).build();
        assertThat(promotionEngine.apply(new Cart(products)), containProduct(GIFT, 1));
        products = mapBuilder().put(randomProduct, 39).build();
        assertThat(promotionEngine.apply(new Cart(products)), containProduct(GIFT, 3));
    }

    @Test
    public void gift_can_not_be_add() {
        Map<String, Integer> products = mapBuilder().put(randomProduct, 5).put(GIFT, 1).build();
        assertThat(promotionEngine.apply(new Cart(products)), containProduct(randomProduct, 5));
        assertThat(promotionEngine.apply(new Cart(products)), not(containProduct(GIFT)));
    }

    @Test
    public void gift_and_promotion() {
        Map<String, Integer> products = new HashMap<>(mapBuilder().put(randomProduct, 37).build());
        products.put(PRODUCT_WITH_PROMOTION, 2);
        assertThat(promotionEngine.apply(new Cart(products)), containProduct(GIFT, 3));
        assertThat(promotionEngine.apply(new Cart(products)), containProduct(PROMOTION, 1));
    }

    private ImmutableMap.Builder<String, Integer> mapBuilder() {
        return ImmutableMap.<String, Integer>builder();
    }

}
