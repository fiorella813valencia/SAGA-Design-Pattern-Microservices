package com.example.sagapattern.dto;

import com.example.sagapattern.dto.CustomerOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Getter
@Setter
public class OrderEvent {
    private CustomerOrder order;
    private String type;


}
