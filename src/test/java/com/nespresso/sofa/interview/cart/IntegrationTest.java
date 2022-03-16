package com.nespresso.sofa.interview.cart;

import static com.nespresso.sofa.interview.cart.matcher.CartMatcher.containProduct;
import static com.nespresso.sofa.interview.cart.services.PromotionEngineTest.PRODUCT_WITH_PROMOTION;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nespresso.sofa.interview.cart.config.AppConfig;
import com.nespresso.sofa.interview.cart.model.Cart;
import com.nespresso.sofa.interview.cart.services.CartService;
import com.nespresso.sofa.interview.cart.services.PromotionEngineTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class IntegrationTest {

    private final String product_1 = PRODUCT_WITH_PROMOTION;
    private final String product_2 = randomUUID().toString();
    private ExecutorService executor = newFixedThreadPool(3);

    @Autowired
    private CartService cartService;

    @Test
    public void integration() {
        UUID cartId = randomUUID();
        cartService.add(cartId, product_1, 10);
        assertThat(cartService.get(cartId), containProduct(PromotionEngineTest.PROMOTION));
        cartService.add(cartId, product_2, 8);
        cartService.add(cartId, product_2, -1);
        assertThat(cartService.get(cartId), containProduct(product_2, 7));
        cartService.set(cartId, product_1, 0);
        assertThat(cartService.get(cartId), not(containProduct(product_1)));
        assertThat(cartService.get(cartId), not(containProduct(PromotionEngineTest.PROMOTION)));
        assertThat(cartService.get(cartId), containProduct(product_2));

    }

    @Test(timeout = 60000)
    public void multi_thread_integration() {
        UUID cartId = randomUUID();
        for (int i = 0; i < 1000000; i++) {
            executor.submit(() -> cartService.add(cartId, product_1, 10));
            executor.submit(() -> cartService.add(cartId, product_2, 10));
            executor.submit(() -> cartService.set(cartId, product_1, 0));
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            Cart cart = cartService.get(cartId);
            if (containProduct(product_1).matches(cart)) {
                assertThat(cart, containProduct(PromotionEngineTest.PROMOTION));
            } else {
                assertThat(cart, not(containProduct(PromotionEngineTest.PROMOTION)));
            }
        }
        assertThat(cartService.get(cartId), containProduct(product_2));
        assertThat(cartService.get(cartId), containProduct(PromotionEngineTest.GIFT));
        int nbGift = (cartService.get(cartId).getProducts().getOrDefault(product_1, 0)
            + cartService.get(cartId).getProducts().getOrDefault(product_2, 0)) / 10;
        assertThat(cartService.get(cartId), containProduct(PromotionEngineTest.GIFT, nbGift));

    }
}
