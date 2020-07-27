package com.trafigura.mq.sender.dbsave;

import static com.trafigura.mq.common.config.ActiveMQConfig.ORDER_DB_SAVE_QUEUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.trafigura.mq.common.DBSaveMessage;

@Service
public class DBSaveSender {

    private static Logger log = LoggerFactory.getLogger(DBSaveSender.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(DBSaveMessage myMessage) {
        log.info("sending with convertAndSend() to queue <" + myMessage + ">");
        jmsTemplate.convertAndSend(ORDER_DB_SAVE_QUEUE, myMessage);
    }


}
