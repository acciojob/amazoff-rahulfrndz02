package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class OrderRepository {

    private HashMap<String, Order> orderDB;
    private HashMap<String, DeliveryPartner> deliveryPartnerDB;
    private HashMap<String, String> pairDB;
    private HashMap<String, HashSet<String>> partnerOrderDB;

    public OrderRepository() {
        this.orderDB = new HashMap<>();
        this.deliveryPartnerDB = new HashMap<>();
        this.pairDB = new HashMap<>();
        this.partnerOrderDB = new HashMap<>();
    }

    public void addOrder(Order order) {
        orderDB.put(order.getId(), order);
    }

    public Order getOrderById(String orderId) {
        return orderDB.get(orderId);
    }

    public void addPartner(String partnerId) {
        deliveryPartnerDB.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {

        if(orderDB.containsKey(orderId) && deliveryPartnerDB.containsKey(partnerId)){

            HashSet<String> currentOrders = new HashSet<String>();
            if(partnerOrderDB.containsKey(partnerId))
                currentOrders = partnerOrderDB.get(partnerId);
            currentOrders.add(orderId);
            partnerOrderDB.put(partnerId, currentOrders);

            DeliveryPartner partner = deliveryPartnerDB.get(partnerId);
            partner.setNumberOfOrders(currentOrders.size());
            pairDB.put(orderId, partnerId);
        }
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDB.get(partnerId);
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderDB.keySet());
    }

    public void deleteOrder(String orderId) {
        if(pairDB.containsKey(orderId)){
            String partnerId = pairDB.get(orderId);
            HashSet<String> orders = partnerOrderDB.get(partnerId);
            orders.remove(orderId);
            partnerOrderDB.put(partnerId, orders);

            //change order count of partner
            DeliveryPartner partner = deliveryPartnerDB.get(partnerId);
            partner.setNumberOfOrders(orders.size());
        }

        if(orderDB.containsKey(orderId)){
            orderDB.remove(orderId);
        }

    }

    public void deletePartner(String partnerId) {
        HashSet<String> orders = new HashSet<>();
        if(partnerOrderDB.containsKey(partnerId)){
            orders = partnerOrderDB.get(partnerId);
            for(String order: orders){
                if(pairDB.containsKey(order)){

                    pairDB.remove(order);
                }
            }
            partnerOrderDB.remove(partnerId);
        }

        if(deliveryPartnerDB.containsKey(partnerId)){
            deliveryPartnerDB.remove(partnerId);
        }
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        Integer time = 0;

        if(partnerOrderDB.containsKey(partnerId)){
            HashSet<String> orders = partnerOrderDB.get(partnerId);
            for(String order: orders){
                if(orderDB.containsKey(order)){
                    Order currOrder = orderDB.get(order);
                    time = Math.max(time, currOrder.getDeliveryTime());
                }
            }
        }

        Integer hour = time/60;
        Integer minutes = time%60;

        String hourInString = String.valueOf(hour);
        String minInString = String.valueOf(minutes);
        if(hourInString.length() == 1){
            hourInString = "0" + hourInString;
        }
        if(minInString.length() == 1){
            minInString = "0" + minInString;
        }

        return  hourInString + ":" + minInString;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String timeS, String partnerId) {
        Integer hour = Integer.valueOf(timeS.substring(0, 2));
        Integer minutes = Integer.valueOf(timeS.substring(3));
        Integer time = hour*60 + minutes;

        Integer countOfOrders = 0;
        if(partnerOrderDB.containsKey(partnerId)){
            HashSet<String> orders = partnerOrderDB.get(partnerId);
            for(String order: orders){
                if(orderDB.containsKey(order)){
                    Order currOrder = orderDB.get(order);
                    if(time < currOrder.getDeliveryTime()){
                        countOfOrders += 1;
                    }
                }
            }
        }
        return countOfOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        Integer countOfOrders = 0;
        List<String> orders =  new ArrayList<>(orderDB.keySet());
        for(String orderId: orders){
            if(!pairDB.containsKey(orderId)){
                countOfOrders += 1;
            }
        }
        return countOfOrders;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        HashSet<String> orderList = new HashSet<>();
        if(partnerOrderDB.containsKey(partnerId)) orderList = partnerOrderDB.get(partnerId);
        return new ArrayList<>(orderList);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        Integer orderCount = 0;
        if(deliveryPartnerDB.containsKey(partnerId)){
            orderCount = deliveryPartnerDB.get(partnerId).getNumberOfOrders();
        }
        return orderCount;
    }
}