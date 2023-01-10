package com.driver.Service;

import com.driver.Models.DeliveryPartner;
import com.driver.Models.Order;
import com.driver.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;


@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void createOrder(Order order){
        orderRepository.createOrder(order);
    }

    public void createPartner(String partnerId){
        orderRepository.createPartner(partnerId);
    }

    public void createOrderPartnerPair(String order, String partner){
        orderRepository.createOrderPartnerPair(order, partner);
    }

    public Order getOrderById(String id){
        return orderRepository.getOrderById(id);
    }

    public DeliveryPartner getPartnerById(String id){
        return orderRepository.getPartnerById(id);
    }
    public int getOrderCountByPartnerId(String id){
        return orderRepository.getOrderCountByPartnerId(id);
    }
    public List<String> getOrdersByPartnerId(String id){
        return orderRepository.getOrdersByPartnerId(id);
    }

    public List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }
    public int getCountOfUnassignedOrders(){
        return orderRepository.getCountOfUnassignedOrders();

    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        int hoursToMins = Integer.valueOf(time.substring(0,2)) * 60;
        int mins = Integer.valueOf(time.substring(3,5));
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(hoursToMins+mins,partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int minutes = orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
        return minutesToTimeString(minutes);
    }

        public String minutesToTimeString(int minutes) {
            int hours = minutes / 60;
            int min = minutes % 60;
            return String.format("%02d:%02d", hours, min);
        }

    public void deletePartnerById(String id){
            orderRepository.deletePartnerById(id);
    }

    public void deleteOrderById(String id){
       orderRepository.deleteOrderById(id);
    }

}
