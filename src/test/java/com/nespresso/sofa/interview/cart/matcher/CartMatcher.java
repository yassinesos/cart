package com.nespresso.sofa.interview.cart.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.nespresso.sofa.interview.cart.model.Cart;

public class CartMatcher {

    public static Matcher<Cart> emptyCart() {
        return new BaseMatcher<Cart>() {
            @Override
            public boolean matches(final Object item) {
                return ((Cart) item).getProducts().isEmpty();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Cart must be empty");
            }
        };
    }

    public static Matcher<Cart> containProduct(final String product) {
        return new BaseMatcher<Cart>() {
            @Override
            public boolean matches(final Object item) {
                return ((Cart) item).getProducts().containsKey(product);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Cart must contains product ").appendValue(product);
            }
        };
    }

    public static Matcher<Cart> containProduct(final String product, final Integer quantity) {
        return new BaseMatcher<Cart>() {
            @Override
            public boolean matches(final Object item) {
                return quantity.equals(((Cart) item).getProducts().get(product));
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Cart must contains product ").appendValue(product)
                    .appendText(" with quantity ").appendValue(quantity);
            }
        };
    }
}
