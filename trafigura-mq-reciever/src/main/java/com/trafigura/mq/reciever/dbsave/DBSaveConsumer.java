package com.trafigura.mq.reciever.dbsave;

import static com.trafigura.mq.common.config.ActiveMQConfig.ORDER_DB_SAVE_QUEUE;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.jms.Session;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.trafigura.biz.shipm.domain.entity.Shipment;
import com.trafigura.biz.shipm.domain.repository.ShipmentRepository;
import com.trafigura.mq.common.DBSaveMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DBSaveConsumer {

    @Autowired
    private ShipmentRepository shipmentRepository;

    
    @JmsListener(destination = ORDER_DB_SAVE_QUEUE)
    public void receiveMessage(@Payload DBSaveMessage<?> saveMessage,
                               @Headers MessageHeaders headers,
                               Message<?> message, Session session) {
        log.info("received <" + saveMessage + ">");

        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("######          Message Details           #####");
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("headers: " + headers);
        log.info("message: " + message);
        log.info("session: " + session);
        log.info("Payload: " + saveMessage);
        log.info("Payload Json: " + new Gson().toJson(saveMessage));
        log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        
        /**
         * Payload Json: 
         * {"username":"A0001","parentId":0,"quantity":2000,"insTm":1595798463329,"updTm":1595798463329,"delfg":0,"version":0}
         */
        LinkedHashMap<?, ?> hashMap = (LinkedHashMap<?, ?>) saveMessage.getContent();
        Shipment shipment = new Shipment();
       if(hashMap.get("id") != null) {
           shipment.setId(Long.valueOf(hashMap.get("id").toString()));
       }
        shipment.setParentId(Long.valueOf(hashMap.get("parentId").toString()));
        shipment.setUsername(hashMap.get("username").toString());
        shipment.setQuantity(Long.valueOf(hashMap.get("quantity").toString()));
        shipment.setDelfg(Integer.valueOf(hashMap.get("delfg").toString()));
        shipment.setVersion(Integer.valueOf(hashMap.get("version").toString()));
        
        Date instm = new DateTime(Long.valueOf(hashMap.get("insTm").toString())).toDate();
        Date updtm = new DateTime(Long.valueOf(hashMap.get("updTm").toString())).toDate();
        shipment.setInsTm(instm);
        shipment.setUpdTm(updtm);
        
        log.info("shipment: " + new Gson().toJson(shipment));
        shipmentRepository.save(shipment);
    }


}
