package com.xlx.utility;

import junit.framework.TestCase;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

public class RabbitmqTest extends TestCase {
	private final static String QUEUE_NAME = "q1";
	private final static String QUEUE_NAME2 = "q2";
	public static void testSend() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.37.66");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
		
		String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		channel.basicPublish("", QUEUE_NAME2, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		
		channel.close();
		connection.close();
	}
	
	public static void testConsume() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("192.168.37.66");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    
	    Consumer consumer = new DefaultConsumer(channel) {
	    	  @Override
	    	  public void handleDelivery(String consumerTag, Envelope envelope,
	    	                             AMQP.BasicProperties properties, byte[] body)
	    	      throws IOException {
	    	    String message = new String(body, "UTF-8");
	    	    System.out.println(" [x] Received '" + message + "'");
	    	  }
	    	};
	    	channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}
