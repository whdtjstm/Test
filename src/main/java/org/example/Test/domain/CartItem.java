package org.example.test.domain;

public class CartItem {
    private Item item;
    private int quantity;

    public CartItem() { }
    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }

    public CartItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void increment() {
        this.quantity = this.quantity + 1;
    }

    public void decrement() {
        this.quantity = this.quantity > 1 ? this.quantity - 1 : 1;
    }
}
