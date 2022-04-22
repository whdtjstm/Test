package org.example.test.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private @Id String id;
    private List<CartItem> cartItems;

    public Cart(String id) {
        this(id, new ArrayList<>());
    }
}
