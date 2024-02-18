package com.payment.paymentms.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.paymentms.dto.CustomerOrder;
import com.payment.paymentms.dto.OrderEvent;
import com.payment.paymentms.dto.PaymentEvent;
import com.payment.paymentms.entity.Payment;
import com.payment.paymentms.entity.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentController {
    @Autowired
    private PaymentRepository repository;
    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, OrderEvent> orderKafkaTemplate;
    @KafkaListener()
    @KafkaListener(topics = "new-orders", groupId = "orders-group")
    public void processPayment(String event) throws Exception {
        System.out.println("Process payment event: "+event);
        OrderEvent orderEvent=new ObjectMapper().readValue(event,OrderEvent.class);
        CustomerOrder order=orderEvent.getOrder();
        Payment payment =new Payment();


        try{
            payment.setAmount(order.getAmount());
            payment.setMode(order.getPaymentMode());
            payment.setOrderId(order.getOrderId());
            payment.setStatus("Success");

            repository.save(payment);
            PaymentEvent paymentEvent=new PaymentEvent();
            paymentEvent.setOrder(orderEvent.getOrder());
            paymentEvent.setType("PAYMENT_CREATED");
            kafkaTemplate.send("new-payments", paymentEvent);

        }catch (Exception e){
            payment.setOrderId(order.getOrderId());
            payment.setStatus("Failed");
            repository.save(payment);

            OrderEvent oe=new OrderEvent();
            oe.setOrder(order);
            oe.setType("ORDER_REVERSED");
            orderKafkaTemplate.send("reversed-orders",oe);
        }



    }
}
