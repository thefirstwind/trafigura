package com.trafigura.biz.shipm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.trafigura.biz.shipm.domain.entity.Shipment;
import com.trafigura.biz.shipm.domain.repository.ShipmentRepository;
import com.trafigura.biz.shipm.service.ShipmentService;
import com.trafigura.biz.shipm.vo.request.DBDeleteRequest;
import com.trafigura.biz.shipm.vo.request.DBGetRequest;
import com.trafigura.biz.shipm.vo.request.DBInsertRequest;
import com.trafigura.biz.shipm.vo.request.DBUpdateRequest;
import com.trafigura.biz.shipm.vo.request.ShipmentMergeRequest;
import com.trafigura.biz.shipm.vo.request.ShipmentRootIncreaseRequest;
import com.trafigura.biz.shipm.vo.request.ShipmentSplitRequest;
import com.trafigura.common.vo.REQ;
import com.trafigura.utils.apijdbclock.RepeatSubmit;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ShipmentController {
    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private ShipmentService shipmentService;

    /**
     * 基础操作 获取一条数据
     * @param req
     * @return
     */
    // 每30秒才能请求一次  lockTime：间隔时间
    @RepeatSubmit(lockTime = 30, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/db/get", method = RequestMethod.POST)
    public REQ dbGet(@Valid @RequestBody DBGetRequest req, BindingResult bindingResult){
    	log.info("ShipmentController dbGet {}", new Gson().toJson(req));
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return REQ.ok().putResult(shipmentService.dbGet(req.getId()));
    }
    
    /**
     * 基础操作 插入一条数据
     * @return
     */
    // 每30秒才能更新一次
    @RepeatSubmit(lockTime = 30, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/db/insert", method = RequestMethod.POST)
    public REQ dbInsert(@Valid @RequestBody DBInsertRequest req, BindingResult bindingResult){
    	log.info("ShipmentController dbInsert {}", new Gson().toJson(req));
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return REQ.ok().putResult(shipmentService.dbInsert(req));
    }
    /**
     * 基础操作 批量插入多条数据
     * @return
     */
    // 每30秒才能更新一次
    @RepeatSubmit(lockTime = 30, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/db/insertbatch", method = RequestMethod.POST)
    public REQ insertbatch(@Valid @RequestBody List<DBInsertRequest> req, BindingResult bindingResult){
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
    	shipmentService.dbInsertBatch(req);
        return REQ.ok();
    }

    /**
     * 基础操作 更新一条数据
     * @param req
     * @return
     */
    // 每30秒才能更新一次
    @RepeatSubmit(lockTime = 30, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/db/update", method = RequestMethod.POST)
    public REQ dbUpdate(@Valid @RequestBody DBUpdateRequest req, BindingResult bindingResult){
    	log.info("ShipmentController dbUpdate {}", new Gson().toJson(req));
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return REQ.ok().putResult(shipmentService.dbUpdate(req));
    }

    /**
     * 基础操作 删除一条数据
     * @param req
     * @return
     */
    // 每30秒才能更新一次
    @RepeatSubmit(lockTime = 30, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/db/delete", method = RequestMethod.POST)
    public REQ dbDelete(@Valid @RequestBody DBDeleteRequest req, BindingResult bindingResult){
    	log.info("ShipmentController dbUpdate {}", new Gson().toJson(req));
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return REQ.ok().putResult(shipmentService.dbDelete(req));

    }

    /**
     * 业务功能 库存拆分
     * @param req
     * @return
     */
    @RepeatSubmit(lockTime = 10, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/shipment/split", method = RequestMethod.POST)
    public REQ shipmentSplit(@Valid @RequestBody ShipmentSplitRequest req, BindingResult bindingResult){
    	log.info("ShipmentController shipmentSplit {}", new Gson().toJson(req, ShipmentSplitRequest.class));
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return REQ.ok().putResult(shipmentService.shipmentSplit(req));
    }

    /**
     * 业务功能 库存合并
     * @param req
     * @return
     */
    // 每10秒才能更新一次
    @RepeatSubmit(lockTime = 10, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/shipment/merge", method = RequestMethod.POST)
    public REQ shipmentMerge(@Valid @RequestBody ShipmentMergeRequest req, BindingResult bindingResult){
    	log.info("ShipmentController shipmentMerge {}", new Gson().toJson(req, ShipmentMergeRequest.class));
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return REQ.ok().putResult(shipmentService.shipmentMerge(req));
    }

    /**
     * 业务功能 跟库存增长
     * @param req
     * @return
     */
    // 每10秒才能更新一次
    @RepeatSubmit(lockTime = 10, clientIdParam = "id", unlockInEnd = false)
    @RequestMapping(value ="/shipment/root/increase", method = RequestMethod.POST)
    public REQ shipmentRootIncrease(@RequestBody ShipmentRootIncreaseRequest req, BindingResult bindingResult){
    	
    	log.info("ShipmentController shipmentRootIncrease {}", new Gson().toJson(req, ShipmentRootIncreaseRequest.class));
    	if (bindingResult.hasErrors()) {
    		log.error(" {}", bindingResult.getAllErrors().toArray());
            return REQ.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return REQ.ok().putResult(shipmentService.shipmentRootIncrease(req));
    }

}
