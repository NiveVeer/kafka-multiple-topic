package Kafka.Consumer.controller;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.Record;

import avro.consumer.model.Billing;
import avro.consumer.model.master_nf;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
@Service
@AllArgsConstructor
public class ConsumerController {
	 @Value("${firehose.delivery-stream.name1}")
	    private String deliveryStreamName1;
	    @Value("${firehose.delivery-stream.name2}")
	    private String deliveryStreamName2;
	   
    public ConsumerController() {
    }

    @KafkaListener(topics = "Trials-1-Datagen", groupId = "Trials", containerFactory = "kafkaListenerContainerFactory")
    public void listener1(@Payload List<Billing> data){

       
        System.out.println(data);
        AmazonKinesisFirehose firehoseClient = getFirehoseClient();
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(deliveryStreamName1);
        Record record = new Record().withData(ByteBuffer.wrap(data.toString().getBytes()));
        putRecordRequest.setRecord(record);
        firehoseClient.putRecord(putRecordRequest);
        System.out.println("Data from Topic Trials-1-Datagen Sent to Trials 1!");
    }
    @KafkaListener(topics = "Avro_messeges", groupId = "Avro", containerFactory = "kafkaListenerContainerFactory")
    public void listener2(@Payload List<master_nf> data){

       
        System.out.println(data);
        AmazonKinesisFirehose firehoseClient = getFirehoseClient();
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(deliveryStreamName2);
        Record record = new Record().withData(ByteBuffer.wrap(data.toString().getBytes()));
        putRecordRequest.setRecord(record);
        firehoseClient.putRecord(putRecordRequest);
        System.out.println("Data from Topic Avro_messeges Sent to Trials 2!");
    }
    public AmazonKinesisFirehose getFirehoseClient(){ 	
    	String accesskey="";
    	String secretkey="";
        return AmazonKinesisFirehoseClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accesskey, secretkey)))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }
}