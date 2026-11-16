package com.bootcamp;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Publica los cambios de estado de un pedido en un topic de Kafka. La clave del mensaje es el
 * id del pedido: Kafka manda todos los mensajes de una clave a la misma partición.
 */
public class OrderEvents implements AutoCloseable {

    private final KafkaProducer<String, String> producer;
    private final String topic;

    public OrderEvents(String bootstrapServers, String topic) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        this.producer = new KafkaProducer<>(config);
        this.topic = topic;
    }

    /** Envía y espera la confirmación del broker. */
    public void publish(String orderId, String status) {
        try {
            producer.send(new ProducerRecord<>(topic, orderId, status)).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while publishing " + orderId, e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("could not publish " + orderId, e.getCause());
        }
    }

    @Override
    public void close() {
        producer.close();
    }
}
