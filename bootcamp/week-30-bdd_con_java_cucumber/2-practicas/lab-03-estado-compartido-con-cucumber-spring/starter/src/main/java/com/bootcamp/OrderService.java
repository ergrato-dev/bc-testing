package com.bootcamp;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

/** Pedidos en memoria. Es un singleton de Spring: vive lo que vive el contexto. */
@Service
public class OrderService {

    private final Map<Integer, Order> orders = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger();

    public Order create(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        Order order = new Order(sequence.incrementAndGet(), amount, Order.Status.PENDING);
        orders.put(order.id(), order);
        return order;
    }

    /** Paga un pedido. El pago tiene que cubrir el importe exacto. */
    public Order pay(int id, int payment) {
        Order order = find(id);
        if (order.status() == Order.Status.PAID) {
            throw new IllegalStateException("order already paid");
        }
        if (payment != order.amount()) {
            throw new IllegalArgumentException("payment " + payment + " does not match amount " + order.amount());
        }
        Order paid = order.paid();
        orders.put(id, paid);
        return paid;
    }

    public Order find(int id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new NoSuchElementException("unknown order: " + id);
        }
        return order;
    }

    public List<Order> pending() {
        return orders.values().stream().filter(o -> o.status() == Order.Status.PENDING).toList();
    }

    /** Borra todos los pedidos. Lo usan los tests para empezar cada escenario de cero. */
    public void clear() {
        orders.clear();
    }
}
