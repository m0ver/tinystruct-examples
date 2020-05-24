package tinystruct.examples;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//import org.junit.Assert;
import org.tinystruct.AbstractApplication;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class rabbitmqTest extends AbstractApplication {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws IOException, TimeoutException {
		// TODO Auto-generated method stub
		/*
		 * Stream<Integer> stream = Stream.iterate(1, i -> i); //
		 * stream.forEach(System.out::println); System.out.println(stream.anyMatch(i ->
		 * i > 5));
		 */

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		factory.setPort(5672);
		final CountDownLatch countDown = new CountDownLatch(1);
		System.out.println("Waiting for messages...");
		Connection connection = factory.newConnection();
		connection.addShutdownListener(new ShutdownListener() {

			@Override
			public void shutdownCompleted(ShutdownSignalException cause) {
				countDown.countDown();
			}
		});
		Channel channel = connection.createChannel();
		try {
			channel.queueDeclare(rabbitmq.TASK_QUEUE_NAME, false, false, false, null);
			channel.basicQos(1);
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					try {
						String message = new String(body, "UTF-8");
						// process the message
						System.out.println("[x] Received:" + message);
					}
					catch(Exception e) {
						e.printStackTrace();
						channel.basicNack(envelope.getDeliveryTag(), false, true);
					}
					finally {
						countDown.countDown();
					}
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			};
			channel.basicConsume(rabbitmq.TASK_QUEUE_NAME, false, consumer);
			countDown.await(5, TimeUnit.SECONDS);

//			Assert.assertEquals(0, countDown.getCount());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			channel.close();
		}

	}

}
