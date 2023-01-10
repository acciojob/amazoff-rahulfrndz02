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

    Map<String,Order> ordersDB = new HashMap<>();
    Map<String,DeliveryPartner> partnersDB = new HashMap<>();
    Map<String,List<Order>> orderPartnerMapping = new HashMap<>();
    Map<String,String> assignedOrders = new HashMap<>();

    public void createOrder(Order order){
        ordersDB.put(order.getId(),order);
    }

    public void createPartner(String partnerId){
        partnersDB.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void createOrderPartnerPair(String orderId, String partnerId){
        if(ordersDB.containsKey(orderId) && partnersDB.containsKey(partnerId) && !assignedOrders.containsKey(orderId)){
            Order currentOrder = ordersDB.get(orderId);
            DeliveryPartner currentPartner = partnersDB.get(partnerId);
            currentPartner.setNumberOfOrders(currentPartner.getNumberOfOrders()+1);
            assignedOrders.put(orderId,partnerId);
            List<Order> ordersListOfAPartner = new ArrayList<>();
            if(orderPartnerMapping.containsKey(partnerId))
                ordersListOfAPartner = orderPartnerMapping.get(partnerId);
            ordersListOfAPartner.add(currentOrder);
            orderPartnerMapping.put(partnerId,ordersListOfAPartner);
        }
    }

    public Order getOrderById(String orderId){
        return ordersDB.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return partnersDB.get(partnerId);
    }
    public Integer getOrderCountByPartnerId(String partnerId){
        if(partnersDB.containsKey(partnerId))
            return partnersDB.get(partnerId).getNumberOfOrders();
        return -1;
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        if(partnersDB.containsKey(partnerId)) {
            List<String> ordersOfThePartner = new ArrayList<>();
            for (Order order : orderPartnerMapping.get(partnerId))
                ordersOfThePartner.add(order.getId());
            return ordersOfThePartner;
        }
        return null;

    }

    public List<String> getAllOrders() {
        List<String> allOrders = new ArrayList<>();
        for (Order order : ordersDB.values())
            allOrders.add(order.getId());
        return allOrders;
    }

        public int getCountOfUnassignedOrders(){
            return ordersDB.size() - assignedOrders.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(Integer time, String partnerId){
        List<Order> ordersOfAPartner = orderPartnerMapping.get(partnerId);
        Integer numberOfOrders = 0;
        for(Order order : ordersOfAPartner){
            if(order.getDeliveryTime() > time)
                numberOfOrders++;
        }
        return numberOfOrders;
    }

    public Integer getLastDeliveryTimeByPartnerId(String partnerId){
        List<Order> ordersOfAPartner = orderPartnerMapping.get(partnerId);

        Integer latestTime = 0;

        for(Order order : ordersOfAPartner){
            if(order.getDeliveryTime()>=latestTime)
                latestTime = order.getDeliveryTime();
        }
        return latestTime;
    }

    public void deletePartnerById(String partnerId){
        if(partnersDB.containsKey(partnerId))
            partnersDB.remove(partnerId);

        List<Order> orderOfPartner = new ArrayList<>();
        if(orderPartnerMapping.containsKey(partnerId)) {
            orderOfPartner = orderPartnerMapping.get(partnerId);
            orderPartnerMapping.remove(partnerId);
        }
        for(Order order : orderOfPartner) {
            if (assignedOrders.containsKey(order.getId()))
                assignedOrders.remove(order.getId());
        }
    }

    public void deleteOrderById(String orderId){
        if(ordersDB.containsKey(orderId))
            ordersDB.remove(orderId);

        if(assignedOrders.containsKey(orderId)){
            List<Order> ordersOfThePartner = orderPartnerMapping.get(assignedOrders.get(orderId));
            for(Order order : ordersOfThePartner)
                if(order.getId().equals(orderId)){
                    ordersOfThePartner.remove(order);
                    break;
                }
            orderPartnerMapping.put(assignedOrders.get(orderId),ordersOfThePartner);
        }
    }

}
