package com.trafigura.mq.sender.order;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.trafigura.mq.common.OrderMessage;


@Service
public class OrderedSenderSingleThreadPool {

	private static volatile ExecutorService singleThreadExecutor = null;
	
	public OrderedSenderSingleThreadPool() {
		singleThreadExecutor = getInstance();
	}
	public ExecutorService getInstance() {
		if(singleThreadExecutor == null) {
			synchronized(OrderedSenderSingleThreadPool.class) {
				if(singleThreadExecutor == null) {
					singleThreadExecutor = Executors.newSingleThreadExecutor();
				}
			}
		}
		return singleThreadExecutor;
	}
	
	public void execute(OrderedSender orderedSender, OrderMessage orderMsg) {
		Runnable task = new OrderedSenderThread(orderedSender, orderMsg);
		singleThreadExecutor.execute(task);
	}
}
