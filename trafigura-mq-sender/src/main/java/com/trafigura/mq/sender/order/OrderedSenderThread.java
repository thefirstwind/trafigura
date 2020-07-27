package com.trafigura.mq.sender.order;

import com.trafigura.mq.common.OrderMessage;

public class OrderedSenderThread implements Runnable {
	
	private OrderedSender orderSend = null;
	
	private OrderMessage orderMessage = null;

	public OrderedSenderThread(OrderedSender _orderSend, OrderMessage _message) {
		this.orderSend = _orderSend;
		this.orderMessage = _message;
	}

	@Override
	public void run() {
		this.orderSend.send(orderMessage);
	}
}
