package com.example.oms.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.oms.orderitem.OrderItem;
import com.example.oms.orderitem.OrderItemRepository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(path = "/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping(path = "/add")
    //add new order to database and call order item controller to add order items
    public @ResponseBody String addNewOrder(@RequestBody String order) {
        //convert json string to json object
        JsonObject orderObject = JsonParser.parseString(order).getAsJsonObject();
        int user_id = orderObject.get("user_id").getAsInt();
        int total = orderObject.get("total").getAsInt();
        String status = orderObject.get("status").getAsString();
        String address = orderObject.get("address").getAsString();

        Order n = new Order();
        n.setUser_id(user_id);
        n.setTotal(total);
        n.setStatus(status);
        n.setAddress(address);
        n.setTimestamp(new java.sql.Timestamp(new java.util.Date().getTime()));
        orderRepository.save(n);

        //get order id
        int order_id = orderRepository.findMaxId();
        System.out.println(order_id);

        JsonArray orderjson = orderObject.get("orderitems").getAsJsonArray();
        System.out.println(orderjson);
        orderjson.forEach(jsonElement -> {
            JsonObject orderitem = JsonParser.parseString(jsonElement.toString()).getAsJsonObject();
            //call order item controller to add order items
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order_id);
            orderItem.setItem_name(orderitem.get("item_name").getAsString());
            orderItem.setItem_price(orderitem.get("item_price").getAsInt());
            orderItem.setItem_status(orderitem.get("item_status").getAsString());
            orderItemRepository.save(orderItem);
        });

        //return response data
        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("message", "Order added successfully");
        return response.toString();

    }
    
    @GetMapping(path = "/find/{id}")
    public @ResponseBody OrderSchema findOrder(@PathVariable("id") String idx) {
        int id = Integer.parseInt(idx);
        OrderSchema order = new OrderSchema();
        order.setId(id);
        order.setUser_id(orderRepository.findById(id).get().getUser_id());
        order.setTotal(orderRepository.findById(id).get().getTotal());
        order.setStatus(orderRepository.findById(id).get().getStatus());
        order.setAddress(orderRepository.findById(id).get().getAddress());
        order.setTimestamp(orderRepository.findById(id).get().getTimestamp());
        List<OrderItem> orderItems = orderItemRepository.findByOrderid(id);
        order.setOrderitems(orderItems);
        return order;
    }
    

    @GetMapping(path = "/{id}")
    public @ResponseBody Iterable<OrderSchema> findOrderByUser(@PathVariable("id") String idx) {
        int id = Integer.parseInt(idx);
        List<Order> orders = orderRepository.findByUser_id(id);
        List<OrderSchema> orderSchemas = new ArrayList<OrderSchema>();
        for (Order order : orders) {
            OrderSchema orderSchema = new OrderSchema();
            orderSchema.setId(order.getId());
            orderSchema.setUser_id(order.getUser_id());
            orderSchema.setTotal(order.getTotal());
            orderSchema.setStatus(order.getStatus());
            orderSchema.setAddress(order.getAddress());
            orderSchema.setTimestamp(order.getTimestamp());
            List<OrderItem> orderItems = orderItemRepository.findByOrderid(order.getId());
            orderSchema.setOrderitems(orderItems);
            orderSchemas.add(orderSchema);
        }
        return orderSchemas;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<OrderSchema> getAllOrders() {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        List<OrderSchema> orderSchemas = new ArrayList<OrderSchema>();
        for (Order order : orders) {
            OrderSchema orderSchema = new OrderSchema();
            orderSchema.setId(order.getId());
            orderSchema.setUser_id(order.getUser_id());
            orderSchema.setTotal(order.getTotal());
            orderSchema.setStatus(order.getStatus());
            orderSchema.setAddress(order.getAddress());
            orderSchema.setTimestamp(order.getTimestamp());
            List<OrderItem> orderItems = orderItemRepository.findByOrderid(order.getId());
            orderSchema.setOrderitems(orderItems);
            orderSchemas.add(orderSchema);
        }
        return orderSchemas;
    }

    @PutMapping(path="/update")
    public @ResponseBody String updateOrder(@RequestParam int id, @RequestParam String status ) {
        Order n = orderRepository.findById(id).get();
        n.setStatus(status);
        orderRepository.save(n);
        return "Updated";
    }
}
