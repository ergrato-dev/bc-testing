package com.bootcamp;

// PASO 2
// import static org.assertj.core.api.Assertions.assertThat;

// import org.apache.kafka.clients.consumer.ConsumerRecord;
// import org.junit.jupiter.api.Test;

// class PaymentEventsTest extends KafkaTestBase {

    // @Test
    // void shouldPublishAPayment() {
        // try (OrderEvents events = new OrderEvents(KAFKA.getBootstrapServers(), "payments")) {
            // events.publish("ORD-7", "PAID");
        // }

        // assertThat(TestConsumer.read(KAFKA.getBootstrapServers(), "payments", 1))
            // .extracting(ConsumerRecord::value)
            // .containsExactly("PAID");
    // }
// }
