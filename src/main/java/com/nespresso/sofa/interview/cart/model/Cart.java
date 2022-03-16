package com.nespresso.sofa.interview.cart.model;

import static java.util.UUID.randomUUID;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public final class Cart implements Serializable {

    private final UUID id;

    public Cart() {
        this(randomUUID());
    }

    public Cart(UUID id) {
        this.id = id;
    }

    public Cart(Map<String, Integer> products) {
        this.id = randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public Map<String, Integer> getProducts() {
        return null;
    }

}
