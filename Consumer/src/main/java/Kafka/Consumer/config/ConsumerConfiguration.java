package Kafka.Consumer.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import avro.consumer.deserializer.AvroDeserializer;
import avro.consumer.model.Billing;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@EnableKafka
@Configuration
public class ConsumerConfiguration {


    @Bean
    public Properties getProperties() throws IOException {

        Properties props = new Properties();

        InputStream stream = ConsumerConfiguration.class.getClassLoader().getResourceAsStream("application.properties");

        props.load(stream);

        return props;
    }


    @Bean
    public ConsumerFactory getConsumerFactory() throws IOException {

        return new DefaultKafkaConsumerFactory(getProperties());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory() throws IOException {

        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(getConsumerFactory());

        return factory;

    }


}