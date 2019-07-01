package tinystruct.examples;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import org.tinystruct.AbstractApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class rabbitmq extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	public void test() throws UnsupportedEncodingException, IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		factory.setPort(5672);

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

			String message = "Hello, James.";

			channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
					message.getBytes("UTF-8"));
			System.out.println(" [x] Sent '" + message + "'");
		}
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	public static final String TASK_QUEUE_NAME = "hello";

}
