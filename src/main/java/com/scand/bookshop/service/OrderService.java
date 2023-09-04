package com.scand.bookshop.service;

import com.scand.bookshop.entity.*;
import com.scand.bookshop.repository.OrderDetailRepository;
import com.scand.bookshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    private Order createEmptyOrder(User user) {
        return new Order(null,
                UUID.randomUUID().toString(),
                user,
                LocalDateTime.now(),
                null,
                OrderStatus.PENDING);
    }

    @Transactional
    public Order createOrder(Book book, User user) {
        Order order = createEmptyOrder(user);
        orderRepository.save(order);
        OrderDetail orderDetail = new OrderDetail(null, order, book, null);
        orderDetailRepository.save(orderDetail);
        order.getOrderDetails().add(orderDetail);
        return order;
    }

    public Optional<Order> findOrderByUuid(String uuid) {
        return orderRepository.findByUuid(uuid);
    }

    public Order getOrder(String uuid) {
        return findOrderByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
    }

    public Page<Order> getAllOrders(Pageable pageable, User user) {
        return orderRepository.findAllByUser(user, pageable);
    }

    @Transactional
    public void changeStatus(Order order, OrderStatus status) {
        order = orderRepository.getReferenceById(order.getId());
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(status);
            order.getOrderDetails().forEach(detail -> {
                detail.setUnitPrice(BigDecimal.valueOf(detail.getBook().getPrice()));
            });
            BigDecimal totalPrice = order.getOrderDetails().stream()
                    .map(OrderDetail::getUnitPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalPrice(totalPrice);
        } else {
            throw new RuntimeException("Order closed");
        }
    }
}
