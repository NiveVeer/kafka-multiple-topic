package MConsumer.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.Record;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
@Service
@AllArgsConstructor
public class KafkaConsumerController {
    @Value("${firehose.delivery-stream.name1}")
    private String deliveryStreamName1;
    @Value("${firehose.delivery-stream.name2}")
    private String deliveryStreamName2;
    public KafkaConsumerController() {
    }

    @KafkaListener(topics = "Trials-1-Datagen", groupId = "Trials", containerFactory = "kafkaListenerContainerFactory")
    public void listener(String data) {

        data = data + "\n";
        System.out.println(data);
        AmazonKinesisFirehose firehoseClient = getFirehoseClient();
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(deliveryStreamName1);
        Record record = new Record().withData(ByteBuffer.wrap(data.getBytes()));
        putRecordRequest.setRecord(record);
        firehoseClient.putRecord(putRecordRequest);
        System.out.println("Data Sent to firehose stream 1!");
       
    }
    @KafkaListener(topics = "Demo-trials-datagen", groupId = "Demo", containerFactory = "kafkaListenerContainerFactory")
    public void listener1(String data) {

        data = data + "\n";
        System.out.println(data);
        AmazonKinesisFirehose firehoseClient = getFirehoseClient();
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(deliveryStreamName2);
        Record record = new Record().withData(ByteBuffer.wrap(data.getBytes()));
        putRecordRequest.setRecord(record);
        firehoseClient.putRecord(putRecordRequest);
        System.out.println("Data Sent to firehose stream 2!");
    }
    
    public AmazonKinesisFirehose getFirehoseClient(){ 	
    	String accesskey="AKIATYHVOKP6VJB7YE5V";
    	String secretkey="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";
        return AmazonKinesisFirehoseClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accesskey, secretkey)))
                .withRegion(Regions.AP_SOUTH_1)
                //.withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }
}