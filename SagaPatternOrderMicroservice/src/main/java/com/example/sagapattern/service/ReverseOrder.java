package com.example.sagapattern.service;

import com.example.sagapattern.entity.Order;
import com.example.sagapattern.dto.OrderEvent;
import com.example.sagapattern.entity.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ReverseOrder {
    @Autowired
    private OrderRepository orderRepository;
    @KafkaListener(topics = "reversed-orders",groupId = "orders-group")
    public void reverseOrder(String event){
        System.out.println("Reverse order event :: "+event);
        try{
            OrderEvent orderEvent=new ObjectMapper().readValue(event,OrderEvent.class);
            Optional<Order> order=orderRepository.findById(orderEvent.getOrder().getOrderId());
            order.ifPresent(o->{
                o.setStatus("Failed");
            });

        }catch(Exception e){
            System.out.println("Exception occurred while reverting order details");

        }
    }
}
