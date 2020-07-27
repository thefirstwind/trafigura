package com.trafigura.biz.shipm;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by xingxiaoning on 2020/07/26.
 */
@ComponentScan(basePackages = {
	"com.trafigura.biz", 
	"com.trafigura.common", 
	"com.trafigura.utils",
	"com.trafigura.mq.common",
	"com.trafigura.mq.sender",
	"com.trafigura.mq.reciever"
})
@SpringCloudApplication
@RestController
public class MicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
    
    @RequestMapping(value ="/", method = RequestMethod.GET)
    public String index(){
        return "welcome";
    }
    @RequestMapping(value ="/openIndex/hello", method = RequestMethod.GET)
    public String openIndex(){
        return "openIndex";
    }


}
