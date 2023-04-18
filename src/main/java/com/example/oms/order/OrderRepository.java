package com.example.oms.order;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    //define custom query to find max order id
    @Query(value = "SELECT MAX(id) FROM orders", nativeQuery = true)
    int findMaxId();

    //define custom query to find orders by user id
    @Query(value = "SELECT * FROM orders WHERE user_id = ?1", nativeQuery = true)
    List<Order> findByUser_id(int id);

    
}
