package com.nespresso.sofa.interview.cart.services;

import static com.nespresso.sofa.interview.cart.matcher.CartMatcher.containProduct;
import static java.util.UUID.randomUUID;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.nespresso.sofa.interview.cart.model.Cart;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    private final String productOne = PromotionEngineTest.PRODUCT_WITH_PROMOTION;
    private final String productTwo = randomUUID().toString();

    @Mock
    private PromotionEngine promotionEngine;

    @Mock
    private CartStorage cartStorage;

    @InjectMocks
    private CartService cartService = new CartService(promotionEngine, cartStorage);

    private Cart currentCart;

    @Before
    public void setup() {
        initMocks(this);
        currentCart = null;
        when(promotionEngine.apply(any(Cart.class))).
            thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(cartStorage.loadCart(any(UUID.class)))
            .thenAnswer(invocationOnMock -> currentCart != null ? currentCart : new Cart((UUID) invocationOnMock.getArguments()[0]));
        when(cartStorage.saveCart(any(Cart.class)))
            .thenAnswer(invocationOnMock -> currentCart = (Cart) invocationOnMock.getArguments()[0]);
    }

    @Test
    public void add() {
        UUID cartId = randomUUID();
        boolean add = cartService.add(cartId, productOne, 10);
        assertThat(add, is(true));
        assertThat(cartService.get(cartId), containProduct(productOne, 10));
        assertThat(cartService.get(cartId), not(containProduct(PromotionEngineTest.PROMOTION)));
    }

    @Test
    public void add_zero() {
        UUID cartId = randomUUID();
        boolean add = cartService.add(cartId, productTwo, 1);
        assertThat(add, is(true));
        cartService.add(cartId, productOne, 0);
        add = cartService.add(cartId, productTwo, 0);
        assertThat(add, is(false));
        assertThat(cartService.get(cartId), not(containProduct(productOne)));
        assertThat(cartService.get(cartId), containProduct(productTwo, 1));
    }

    @Test
    public void add_negative() {
        UUID cartId = randomUUID();
        boolean add = cartService.add(cartId, productOne, -1);
        assertThat(add, is(false));
        assertThat(cartService.get(cartId), not(containProduct(productOne)));
    }

    @Test
    public void set() {
        UUID cartId = randomUUID();
        boolean add = cartService.set(cartId, productOne, 10);
        assertThat(add, is(true));
        assertThat(cartService.get(cartId), containProduct(productOne, 10));
        assertThat(cartService.get(cartId), not(containProduct(PromotionEngineTest.PROMOTION)));
    }

    @Test
    public void set_zero() {
        UUID cartId = randomUUID();
        cartService.add(cartId, productTwo, 10);
        cartService.set(cartId, productTwo, 0);
        assertThat(cartService.get(cartId), not(containProduct(productTwo)));
    }

    @Test
    public void multiple_add() {
        UUID cartId = randomUUID();
        boolean add = cartService.add(cartId, productOne, 10);
        cartService.add(cartId, productOne, 100);
        assertThat(add, is(true));
        assertThat(cartService.get(cartId), containProduct(productOne, 110));
    }

    @Test
    public void set_negative() {
        UUID cartId = randomUUID();
        boolean add = cartService.set(cartId, productOne, 10);
        assertThat(add, is(true));
        assertThat(cartService.get(cartId), containProduct(productOne, 10));
        add = cartService.set(cartId, productOne, -20);
        assertThat(add, is(true));
        assertThat(cartService.get(cartId), not(containProduct(productOne)));
    }

    @Test
    public void set_multiple() {
        UUID cartId = randomUUID();
        boolean update = cartService.set(cartId, productOne, 10);
        assertThat(update, is(true));
        update = cartService.set(cartId, productOne, 10);
        assertThat(update, is(false));
        update = cartService.set(cartId, productOne, 1);
        assertThat(update, is(true));
        assertThat(cartService.get(cartId), containProduct(productOne, 1));
    }

    @Test
    public void add_update() {
        UUID cartId = randomUUID();
        cartService.set(cartId, productOne, 10);
        boolean add = cartService.add(cartId, productOne, 10);
        assertThat(add, is(true));
        assertThat(cartService.get(cartId), containProduct(productOne, 20));
        boolean update = cartService.set(cartId, productOne, 20);
        assertThat(update, is(false));
        assertThat(cartService.get(cartId), containProduct(productOne, 20));
    }

    @Test
    public void to_string() {
        UUID cartId = randomUUID();
        cartService.set(cartId, "7666", 12);
        assertThat(cartService.get(cartId).toString(), is("Cart {id: " + cartId + ", products: {7666=12}}"));
    }
}
