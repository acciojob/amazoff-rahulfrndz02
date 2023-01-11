package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        this.deliveryTime = timeConversion(deliveryTime);
    }

    public int timeConversion(String deliveryTime) {
        String hourString = deliveryTime.substring(0,2);
        String minString = deliveryTime.substring(3);
        int hour= stringToInt(hourString);
        int min = stringToInt(minString);
        int convertedTime = hour*60+min;
        return convertedTime;
    }

    private int stringToInt(String time) {
        int res=0;
        for(int i = 0 ; i < time.length() ; i++){
            int val = time.charAt(i)-'0';
            res = res*10 + val;
        }
        return res;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}