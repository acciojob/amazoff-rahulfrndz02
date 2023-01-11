package com.driver;

import com.driver.DeliveryPartner;
import com.driver.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();

    //    here,string(Key) will be partnerId and value will be List<Order>
    HashMap<String, List<Order>> orderPartnerPairDb = new HashMap<>();
    HashSet<String> unassignedOrders = new HashSet<>();
    public void addOrderInDb(Order order) {
        orderDb.put(order.getId(), order);
        unassignedOrders.add(order.getId());
    }

    public void addPartnerInDb(String partnerId) {
        deliveryPartnerDb.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        unassignedOrders.remove(orderId);
        List<Order> orderList = orderPartnerPairDb.getOrDefault(partnerId,new ArrayList<>());
        Order order = orderDb.get(orderId);
        orderList.add(order);
        DeliveryPartner partner = deliveryPartnerDb.get(partnerId);
        int noOfOrder = partner.getNumberOfOrders();
        noOfOrder++;
        partner.setNumberOfOrders(noOfOrder);
        orderPartnerPairDb.put(partnerId,orderList);
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDb.get(partnerId);
    }

    public Integer getOrderCount(String partnerId) {
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        return deliveryPartner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orderList = null;
        List<Order> orderObjectList = orderPartnerPairDb.get(partnerId);
        for(Order order : orderObjectList){
            orderList.add(order.getId());
        }
        return orderList;
    }

    public List<String> getAllOrders() {
        List<String> orderIdList =null;
        for( String key : orderDb.keySet()){
            orderIdList.add(key);
        }
        return orderIdList;
    }

    public Integer getCountOfUnassignedOrders() {
        return unassignedOrders.size();
    }

    public void deletePartnerById(String partnerId) {
        List<Order> orderList = orderPartnerPairDb.get(partnerId);
        for(Order order : orderList){
            unassignedOrders.add(order.getId());
        }
        deliveryPartnerDb.remove(partnerId);
        orderPartnerPairDb.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(unassignedOrders.contains(orderId)){
            unassignedOrders.remove(orderId);
        }
        else{
            for(String partnerId : orderPartnerPairDb.keySet()){
                List<Order> orderList = orderPartnerPairDb.get(partnerId);
                for(Order order : orderList){
                    if(order.getId()==orderId){
                        orderList.remove(order);
                        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
                        int noOfOrder = deliveryPartner.getNumberOfOrders();
                        noOfOrder--;
                        deliveryPartner.setNumberOfOrders(noOfOrder);
                    }
                }
            }
        }
        orderDb.remove(orderId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int time=-1;
        List<Order> orderList = orderPartnerPairDb.get(partnerId);

        for (Order order : orderList){
            if(order.getDeliveryTime()>time){
                time=order.getDeliveryTime();
            }
        }
        String timeInHhMmFormat = convertInHhMm(time);
        return timeInHhMmFormat;
    }

    private String convertInHhMm(int time) {
        String ans="";
        int hh = time/60;

        if(hh<10){
            ans="0"+hh+":";
        }
        ans=hh+":";
        int mm = time%60;
        if(mm<10){
            ans+=("0"+mm);
        }
        ans+=mm;

        return ans;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        List<Order> orderList = orderPartnerPairDb.get(partnerId);
        int count =0;
        for (Order order : orderList){
            if(order.getDeliveryTime()>order.timeConversion(time)){
                count++;
            }
        }
        return count;
    }
}