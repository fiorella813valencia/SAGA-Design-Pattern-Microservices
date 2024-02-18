package com.example.sagapattern.entity;

import com.example.sagapattern.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {



}
