package com.driver.Repository;

import com.driver.Models.DeliveryPartner;
import com.driver.Models.Order;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String, Order> orderMap = new HashMap<>();
    Map<String, Integer> unassignedOrderMap = new HashMap<>();
    Map<String, List<String>> pairMap = new HashMap<>();

    public void createOrder(Order order){
        orderMap.put(order.getId(), order);
        unassignedOrderMap.put(order.getId(), 0);
    }

    public void createPartner(String partnerId){
        List<String> list = new ArrayList<>();
        pairMap.put(partnerId, list);
    }

    public void createOrderPartnerPair(String orderId, String partnerId){
        if ( unassignedOrderMap.get(orderId) == 0) {
            List<String> listOfOrders = pairMap.get(partnerId);
            listOfOrders.add(orderId);
            pairMap.replace(partnerId, listOfOrders);

            unassignedOrderMap.replace(orderId, 1);
        }
    }

    public Order getOrderById(String id){
        return orderMap.get(id);
    }

    public DeliveryPartner getPartnerById(String id){
        DeliveryPartner deliveryPartner = new DeliveryPartner(id);
        deliveryPartner.setNumberOfOrders(pairMap.get(id).size());
        return deliveryPartner;
    }
    public int getOrderCountByPartnerId(String id){
        return pairMap.get(id).size();
    }
    public List<String> getOrdersByPartnerId(String id){
        List<String> orderList = pairMap.get(id);
        return orderList;

    }

    public List<String> getAllOrders(){
        List<String> orders=new ArrayList<>();
        for(String key: orderMap.keySet())
        {
            orders.add(orderMap.get(key).getId());
        }
        return orders;
    }

    public int getCountOfUnassignedOrders(){
        int count=0;
        if(!orderMap.isEmpty()) {
            for (String key : orderMap.keySet()) {
                if (unassignedOrderMap.get(key) == 0) {
                    count++;
                }
            }
        }
        return  count;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        int hour=0;
        int minute=0;
        for (int i=0; i<2; i++) {
            hour=(hour*10)+ (int)time.charAt(i) -'0';
        }
        for (int i=3; i<5; i++) {
            minute=(minute*10)+ (int)time.charAt(i) -'0';
        }
        System.out.println(hour+" "+minute);

        int currentTime=hour*60 + minute;
        System.out.println(currentTime);

        List<String> listOfOrders=pairMap.get(partnerId);

        int count=0;
        //traverse throung list of orders
        for (int i=0; i<listOfOrders.size(); i++) {
            Order order=orderMap.get(listOfOrders.get(i));
            System.out.println(order.getDeliveryTime()+" "+currentTime);
            if (order.getDeliveryTime()> currentTime) {
                count++;
            }
        }
        System.out.println(count);
        return  count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String lastDeliveryTime="";
        List<String> allOrdersOfPartner=pairMap.get(partnerId);

        for (int i=0; i<allOrdersOfPartner.size(); i++){
            Order order=orderMap.get(allOrdersOfPartner.get(i));

            int deliveryTime=order.getDeliveryTime();  //which is in int -> we have to convert in into string;
            int hour=deliveryTime/60;
            int minute=deliveryTime % 60;

            lastDeliveryTime=""+hour+":";
            if (minute < 10){
                lastDeliveryTime=lastDeliveryTime+"0"+minute;
            }
            else {
                lastDeliveryTime=lastDeliveryTime+minute;
            }
        }
        return lastDeliveryTime;
    }
    public void deletePartnerById(String id){
        pairMap.remove(id);
    }

    public void deleteOrderById(String id){
        orderMap.remove(id);
    }

}
