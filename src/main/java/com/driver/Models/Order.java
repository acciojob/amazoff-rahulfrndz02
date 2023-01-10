package com.driver.Models;
import java.sql.Time;
public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        int hoursToMins = Integer.valueOf(deliveryTime.substring(0,2)) * 60;
        int mins = Integer.valueOf(deliveryTime.substring(3,5));
        this.deliveryTime = hoursToMins + mins;

    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}