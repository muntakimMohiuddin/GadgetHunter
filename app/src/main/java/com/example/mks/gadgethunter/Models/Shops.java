package com.example.mks.gadgethunter.Models;

/**
 * Created by HP on 02-Nov-17.
 */

public class Shops {
    String shopName, shopImageUrl;

    public Shops() {
    }

    public Shops(String shopName, String shopImageUrl) {
        this.shopName = shopName;
        this.shopImageUrl = shopImageUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    @Override
    public String toString() {
        return "Shops{" +
                "shopName='" + shopName + '\'' +
                ", shopImageUrl='" + shopImageUrl + '\'' +
                '}';
    }
}
