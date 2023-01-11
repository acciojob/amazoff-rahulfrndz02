package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    OrderRepository orderRepository = new OrderRepository();

    public void addOrder(Order order) {
        orderRepository.addOrderInDb(order);
    }

    public void addPartner(String partnerId) {
        orderRepository.addPartnerInDb(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public Order getOrderById(String orderId) {
        Order order= orderRepository.getOrderById(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner deliveryPartner = orderRepository.getPartnerById(partnerId);
        return deliveryPartner;
    }

    public Integer getOrderCount(String partnerId) {
        Integer c = orderRepository.getOrderCount(partnerId);
        return c;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orderList = orderRepository.getOrdersByPartnerId(partnerId);
        return orderList;
    }

    public List<String> getAllOrders() {
        List<String> allOdersList = orderRepository.getAllOrders();
        return allOdersList;
    }

    public Integer getCountOfUnassignedOrders() {
        int countOfOrders = orderRepository.getCountOfUnassignedOrders();
        return countOfOrders;
    }

    public void deletePArtnerById(String partnerId) {
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String time = orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
        return time;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer count = orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
        return count;
    }
}