package com.example.quickbuyfooddelivery;

import com.example.quickbuyfooddelivery.models.ShoppingCart;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<ShoppingCart> cartList = new ArrayList<>();

    public static void addToCart(Food food) {
        boolean exists = false;
        for (ShoppingCart item : cartList) {
            if (item.getTen().equals(food.getName())) {
                // If you had a setNum method, you could increment here.
                // For simplicity, we'll just add as a new entry or you can update ShoppingCart model later.
                // For now, let's just add it.
                exists = true;
                break;
            }
        }
        
        // Convert Food price "45.000 vnđ" to a simpler format if needed, 
        // but ShoppingCart takes String so it's fine.
        cartList.add(new ShoppingCart(food.getImageResId(), food.getName(), food.getPrice(), 1));
    }

    public static List<ShoppingCart> getCartList() {
        return cartList;
    }
}
