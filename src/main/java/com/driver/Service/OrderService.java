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

    public void callSaveOrder(Order order){
        orderRepository.saveOrders(order);
    }

    public void callSavePartner(String partnerId){
        orderRepository.savePartners(partnerId);
    }

    public void callSaveOrderPartner(String orderId, String partnerId){
        orderRepository.assignOrdertoPartner(orderId,partnerId);
    }

    public Order callReturnOrderbyId(String orderId){
        return orderRepository.returnOrderById(orderId);
    }

    public DeliveryPartner callReturnPartnerById(String partnerId){
        return orderRepository.returnPartnerById(partnerId);
    }

    public Integer callOrderNumberByPartnerId(String partnerId){
        return orderRepository.returnOrderCountByPartnerId(partnerId);
    }

    public List<String> callOrderListofPartner(String partnerId){
        return orderRepository.returnOrdersOfAPartner(partnerId);
    }

    public List<String> callReturnAllOrders(){
        return orderRepository.returnAllOrders();
    }

    public Integer callReturnUnassignedOrders(){
        return orderRepository.returnUnassignedOrders();
    }

    public Integer callReturnOrdersLeftAfterTime(String time, String partnerId){
        int hoursToMins = Integer.valueOf(time.substring(0,2)) * 60;
        int mins = Integer.valueOf(time.substring(3,5));

        return orderRepository.returnOrdersLeftAfterCertainTime(hoursToMins+mins,partnerId);
    }

    public String callLatestTimeOfDelivery(String partnerId){
        int minutes = orderRepository.returnLastDeliveryTime(partnerId);
        return minutesToTimeString(minutes);
    }

    public String minutesToTimeString(int minutes) {
        int hours = minutes / 60;
        int min = minutes % 60;
        return String.format("%02d:%02d", hours, min);
    }

    public void callDeletePartnerbyId(String partnerId){
        orderRepository.deletePartnerbyId(partnerId);
    }

    public void callDeleteOrderById(String orderId){
        orderRepository.deleteOrderById(orderId);
    }
}