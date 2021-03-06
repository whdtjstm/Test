package org.example.test.domain;

public class Dish {
    private String description;
    private boolean delivered = false;

    public Dish(String description) {
        this.description = description;
    }

    public Dish(String description, boolean delivered) {
        this.description = description;
        this.delivered = delivered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public static Dish deliver(Dish dish) {
        Dish deliveredDish = new Dish(dish.description);
        deliveredDish.delivered = true;

        return deliveredDish;
    }
}
