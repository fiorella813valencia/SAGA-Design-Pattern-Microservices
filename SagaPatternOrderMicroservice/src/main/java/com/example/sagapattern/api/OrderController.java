package com.example.sagapattern.api;

import com.example.sagapattern.dto.CustomerOrder;
import com.example.sagapattern.entity.Order;
import com.example.sagapattern.dto.OrderEvent;
import com.example.sagapattern.entity.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private KafkaTemplate<String,OrderEvent> kafkaTemplate;
    @PostMapping("/orders")
    public void createOrder(@RequestBody CustomerOrder customerOrder){
        Order order=new Order();


        try{
            order.setAmount(customerOrder.getAmount());
            order.setItem(customerOrder.getItem());
            order.setQuality(customerOrder.getQuantity());
            order.setStatus("Created");
            order=repository.save(order);
            customerOrder.setOrderId(order.getId());

            OrderEvent orderEvent=new OrderEvent();
            orderEvent.setOrder(customerOrder);
            orderEvent.setType("ORDER_CREATED");
            kafkaTemplate.send("new-orders",orderEvent);
        }catch(Exception e){
            order.setStatus("Failed");
            repository.save(order);

        }

    }
}
