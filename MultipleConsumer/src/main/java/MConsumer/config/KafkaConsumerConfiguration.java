package MConsumer.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;



@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {


    @Bean
    public Properties getProperties() throws IOException {

        Properties props = new Properties();

        InputStream stream = KafkaConsumerConfiguration.class.getClassLoader().getResourceAsStream("application.properties");

        props.load(stream);

        return props;
    }


    @Bean
    public ConsumerFactory getConsumerFactory() throws IOException {

        return new DefaultKafkaConsumerFactory(getProperties(), new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() throws IOException {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(getConsumerFactory());

        return factory;

    }

}