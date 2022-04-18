package org.example.Test;

import org.example.Test.domain.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class WebController {
    @GetMapping("/")
    public String index(Model model) {
        System.out.println("index");
        
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();

        list.add("apple");
        list.add("banana");
        list.add("fineapple");

        products.add(new Product(1,"사과",1000,10));
        products.add(new Product(2,"배",2000,16));
        products.add(new Product(3,"초콜릿",1000,3));
        products.add(new Product(4,"치킨",15000,1));

        model.addAttribute("test", "This is Test. by JSUN.");
        model.addAttribute("list", list);
        model.addAttribute("products", products);

        return "index";
    }
}
