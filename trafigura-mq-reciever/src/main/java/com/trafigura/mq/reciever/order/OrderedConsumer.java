package com.trafigura.mq.reciever.order;

import static com.trafigura.mq.common.config.ActiveMQConfig.ORDER_QUEUE;

import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.trafigura.mq.common.OrderMessage;


@Component
public class OrderedConsumer {

    private static Logger log = LoggerFactory.getLogger(OrderedConsumer.class);

    @JmsListener(destination = ORDER_QUEUE)
    public void receiveMessage(@Payload OrderMessage order,
                               @Headers MessageHeaders headers,
                               Message message, Session session) {
        log.info("received <" + order + ">");

        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("######          Message Details           #####");
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("headers: " + headers);
        log.info("message: " + message);
        log.info("session: " + session);
        log.info("Payload: " + new Gson().toJson(order));
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
    }
    

}
