package com.example.itme_service.item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class item {
    private Long id;
    private String itemName;
    private int price;
    private Integer quanity;

    public item(String itemName, int price, Integer quanity) {
        this.itemName = itemName;
        this.price = price;
        this.quanity = quanity;
    }

    public item() {
    }
}
