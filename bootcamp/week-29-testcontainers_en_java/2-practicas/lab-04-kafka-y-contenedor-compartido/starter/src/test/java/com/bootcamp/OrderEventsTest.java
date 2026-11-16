package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
// PASO 1
// import java.util.Map;
// import java.util.concurrent.ExecutionException;
// import org.apache.kafka.clients.admin.Admin;
// import org.apache.kafka.clients.admin.AdminClientConfig;
// import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

@Testcontainers
class OrderEventsTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:4.2.1");

    @Test
    void shouldDeliverTheEventWithTheOrderIdAsKey() {
        try (OrderEvents events = new OrderEvents(kafka.getBootstrapServers(), "orders")) {
            events.publish("ORD-1", "CREATED");
        }

        List<ConsumerRecord<String, String>> records = TestConsumer.read(kafka.getBootstrapServers(), "orders", 1);

        assertThat(records)
            .extracting(ConsumerRecord::key, ConsumerRecord::value)
            .containsExactly(tuple("ORD-1", "CREATED"));
    }

    // PASO 1: el orden se garantiza por clave, no por topic
    // @Test
    // void shouldKeepTheOrderOfTheEventsOfEachOrderAcrossThreePartitions()
            // throws InterruptedException, ExecutionException {
        // try (Admin admin = Admin.create(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers()))) {
            // admin.createTopics(List.of(new NewTopic("orders-3p", 3, (short) 1))).all().get();
        // }
        // try (OrderEvents events = new OrderEvents(kafka.getBootstrapServers(), "orders-3p")) {
            // events.publish("ORD-A", "CREATED");
            // events.publish("ORD-B", "CREATED");
            // events.publish("ORD-A", "PAID");
            // events.publish("ORD-B", "CANCELLED");
            // events.publish("ORD-A", "SHIPPED");
        // }

        // List<ConsumerRecord<String, String>> records = TestConsumer.read(kafka.getBootstrapServers(), "orders-3p", 5);
        // records.forEach(r -> System.out.println("partición " + r.partition() + " offset " + r.offset() + " " + r.key() + " " + r.value()));

        // assertThat(records).hasSize(5);
        // assertThat(records).filteredOn(r -> r.key().equals("ORD-A"))
            // .extracting(ConsumerRecord::value)
            // .containsExactly("CREATED", "PAID", "SHIPPED");
        // assertThat(records).filteredOn(r -> r.key().equals("ORD-B"))
            // .extracting(ConsumerRecord::value)
            // .containsExactly("CREATED", "CANCELLED");
    // }
}
