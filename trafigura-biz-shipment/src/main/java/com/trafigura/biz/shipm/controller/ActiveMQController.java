package com.trafigura.biz.shipm.controller;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.trafigura.mq.common.DBSaveMessage;
import com.trafigura.mq.common.OrderMessage;
import com.trafigura.mq.sender.dbsave.DBSaveSender;
import com.trafigura.mq.sender.dbsave.DBSaveSenderSingleThreadPool;
import com.trafigura.mq.sender.order.OrderedSender;
import com.trafigura.mq.sender.order.OrderedSenderSingleThreadPool;


@RestController
public class ActiveMQController {
	
    private static Logger log = LoggerFactory.getLogger(ActiveMQController.class);

	@Autowired
	private OrderedSender orderSender;
	
	@Autowired
	private OrderedSenderSingleThreadPool orderThreadPool;
	
	@Autowired
	private DBSaveSender dbSaveSender;
	
	@Autowired
	private DBSaveSenderSingleThreadPool dbSaveThreadPool;
	
	@PostMapping("/activemq/demo")
	public void sendTopic(String msg) {
		OrderMessage myMessage = new OrderMessage(" - Sending JMS Message using Embedded activeMQ", new DateTime().toDate());
		orderSender.send(myMessage);
		log.info("Waiting for all ActiveMQ JMS Messages to be consumed");
	}
	
	@PostMapping("/activemq/queue")
	public void sendQueue(String msg) {
		log.info("sendQueue {}", msg);
		OrderMessage myMessage = new OrderMessage();
		myMessage.setContent(msg);
		myMessage.setTimestamp(new DateTime().toDate());
		orderSender.send(myMessage);
	}

	
	@PostMapping("/activemq/queue2")
	public void sendQueue2(String msg) {
		log.info("sendQueue {}", msg); 
		
		OrderMessage orderMsg = new OrderMessage();
		orderMsg.setContent(msg);
		orderMsg.setTimestamp(new DateTime().toDate());

		log.info("threadPool {}", orderThreadPool);

		orderThreadPool.execute(orderSender, orderMsg);
	}
	
    @RequestMapping(value ="/activemq/dbstore", method = RequestMethod.POST)
	public <T> void dbstore(@RequestBody T reqjson) {
		log.info("sendQueue {}", reqjson); 
		
		DBSaveMessage<T> dbSaveMsg = new DBSaveMessage<T>();
		dbSaveMsg.setContent(reqjson);
		dbSaveMsg.setTimestamp(new DateTime().toDate());

		log.info("threadPool {}", dbSaveThreadPool);

		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);
	}

	


}
