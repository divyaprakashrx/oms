package com.example.oms.orderitem;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderid(int orderid);
    
    
}
