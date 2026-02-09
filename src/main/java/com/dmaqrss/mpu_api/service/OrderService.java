package com.dmaqrss.mpu_api.service;

import com.dmaqrss.mpu_api.dto.order.CreateOrderRequestDTO;
import com.dmaqrss.mpu_api.dto.order.OrderItemRequestDTO;
import com.dmaqrss.mpu_api.dto.order.OrderResponseDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.mapper.OrderMapper;
import com.dmaqrss.mpu_api.model.Order;
import com.dmaqrss.mpu_api.model.OrderItem;
import com.dmaqrss.mpu_api.model.Product;
import com.dmaqrss.mpu_api.model.User;
import com.dmaqrss.mpu_api.model.roles.OrderStatusRole;
import com.dmaqrss.mpu_api.repository.OrderRepository;
import com.dmaqrss.mpu_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderMapper mapper;

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request, User user){
        Order order = createOrderBase(user);

        List<OrderItem> items = processItems(request, order);
        order.setItems(items);

        order.setTotal(calculateTotal(items));
        orderRepository.save(order);
        return mapper.toResponse(order);
    }

    private Order createOrderBase(User user){
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatusRole.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        return order;
    }

    private List<OrderItem> processItems(CreateOrderRequestDTO request, Order order){
        List<OrderItem> items = new ArrayList<>();
        for(OrderItemRequestDTO itemRequest : request.items()){
            Product product =  validadeProduct(itemRequest);
            updateStock(product, itemRequest);
            OrderItem item = new OrderItem(order, product, itemRequest.quantity());
            items.add(item);
        }
        return items;
    }

    private Product validadeProduct(OrderItemRequestDTO itemRequest){
         return productRepository.findByBarCode(itemRequest.barCode()).orElseThrow(() ->
                new BusinessException("produto: " + itemRequest.barCode() + " invalido ou indisponivel"));
    }

    private void updateStock(Product product, OrderItemRequestDTO itemRequest){
        if(product.getAmount() < itemRequest.quantity() || product.getAmount() == 0){
            throw new BusinessException("quantidade do produto: " + itemRequest.barCode() + " indisponivel");
        }
        product.setAmount(product.getAmount() - itemRequest.quantity());
        productRepository.save(product);
    }

    private BigDecimal calculateTotal(List<OrderItem> items){
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
