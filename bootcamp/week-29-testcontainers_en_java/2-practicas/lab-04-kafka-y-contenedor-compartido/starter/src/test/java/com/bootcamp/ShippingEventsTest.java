package com.bootcamp;

// PASO 2
// import static org.assertj.core.api.Assertions.assertThat;

// import org.apache.kafka.clients.consumer.ConsumerRecord;
// import org.junit.jupiter.api.Test;

// class ShippingEventsTest extends KafkaTestBase {

    // @Test
    // void shouldPublishAShipment() {
        // try (OrderEvents events = new OrderEvents(KAFKA.getBootstrapServers(), "shipments")) {
            // events.publish("ORD-7", "SHIPPED");
        // }

        // assertThat(TestConsumer.read(KAFKA.getBootstrapServers(), "shipments", 1))
            // .extracting(ConsumerRecord::value)
            // .containsExactly("SHIPPED");
    // }
// }
