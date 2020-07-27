package com.trafigura.mq.reciver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trafigura.biz.shipm.MicroServiceApplication;
import com.trafigura.mq.common.OrderMessage;
import com.trafigura.mq.sender.order.OrderedSender;
import com.trafigura.mq.sender.order.OrderedSenderThread;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroServiceApplication.class)
public class ActiveMqTest {
	
	@Autowired
	private OrderedSender orderedSender;
	@Test
	public void testOrderedSender() {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		
		for(int i = 0 ; i < 5; i ++) {
			
			OrderMessage orderMsg = new OrderMessage();
			orderMsg.setContent(String.valueOf(i));
			orderMsg.setTimestamp(new DateTime().toDate());

			Runnable task = new OrderedSenderThread(orderedSender, orderMsg);
			singleThreadExecutor.execute(task);
		}
		
		try {
			Thread.sleep(5000);
			singleThreadExecutor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOrderedSender2() {
		ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(5);
		for(int i = 0 ; i < 5; i ++) {
			OrderMessage orderMsg = new OrderMessage();
			orderMsg.setContent(String.valueOf(i));
			orderMsg.setTimestamp(new DateTime().toDate());

			Runnable task = new OrderedSenderThread(orderedSender, orderMsg);
			singleThreadExecutor.execute(task);
		}
		try {
			Thread.sleep(5000);
			singleThreadExecutor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
