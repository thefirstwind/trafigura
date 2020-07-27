package com.trafigura.mq.sender.order;

import static com.trafigura.mq.common.config.ActiveMQConfig.ORDER_QUEUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.trafigura.mq.common.OrderMessage;

@Service
public class OrderedSender {

    private static Logger log = LoggerFactory.getLogger(OrderedSender.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(OrderMessage myMessage) {
        log.info("sending with convertAndSend() to queue <" + myMessage + ">");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage);
    }
    

}
