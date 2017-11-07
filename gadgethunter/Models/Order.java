package com.example.mks.gadgethunter.Models;

/**
 * Created by HP on 03-Nov-17.
 */

public class Order {
    String orderer, orderid, productId, quantity, deliveryAddress, paymentMethod, contactInfo;

    public Order() {
    }

    public Order(String orderer, String orderid, String productId, String quantity, String deliveryAddress, String paymentMethod, String contactInfo) {
        this.orderer = orderer;
        this.orderid = orderid;
        this.productId = productId;
        this.quantity = quantity;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.contactInfo = contactInfo;
    }

    public String getOrderer() {
        return orderer;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getProductId() {
        return productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderer='" + orderer + '\'' +
                ", orderid='" + orderid + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
