package com.batch.example.config;

import com.batch.example.model.Products1;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Products1, Products1> {
    @Override
    public Products1 process(Products1 item) throws Exception {
        try {
            System.out.println(item.getDescription());

            // Validate if discount is a valid number
            if (item.getDiscount() != null && item.getDiscount().matches("\\d+")) {
                int discountPer = Integer.parseInt(item.getDiscount().trim());
                double originalPrice = Double.parseDouble(item.getPrice().trim());
                double discount = (discountPer / 100.0) * originalPrice; // Calculate discount
                double finalPrice = originalPrice - discount;
                item.setDiscountedPrice(String.valueOf(finalPrice));
            } else {
                // Handle invalid discount input
                System.out.println("Invalid discount value for product: " + item.getProductId());
                item.setDiscountedPrice(item.getPrice()); // Set price as discounted price in case of invalid discount
            }

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return item;
    }
}
