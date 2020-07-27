package com.trafigura.biz.shipm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.trafigura.biz.shipm.controller.ShipmentController;
import com.trafigura.biz.shipm.domain.entity.Shipment;
import com.trafigura.biz.shipm.domain.entity.ShipmentNode;
import com.trafigura.biz.shipm.domain.repository.ShipmentRepository;
import com.trafigura.biz.shipm.vo.request.DBDeleteRequest;
import com.trafigura.biz.shipm.vo.request.DBGetRequest;
import com.trafigura.biz.shipm.vo.request.DBInsertRequest;
import com.trafigura.biz.shipm.vo.request.DBUpdateRequest;
import com.trafigura.biz.shipm.vo.request.ShipmentMergeRequest;
import com.trafigura.biz.shipm.vo.request.ShipmentRootIncreaseRequest;
import com.trafigura.biz.shipm.vo.request.ShipmentSplitRequest;
import com.trafigura.mq.common.DBSaveMessage;
import com.trafigura.mq.sender.dbsave.DBSaveSender;
import com.trafigura.mq.sender.dbsave.DBSaveSenderSingleThreadPool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
//@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class ShipmentService {
    @Autowired
    private ShipmentRepository shipmentRepository;
	@Autowired
	private DBSaveSender dbSaveSender;
	
	@Autowired
	private DBSaveSenderSingleThreadPool dbSaveThreadPool;

    /**
     * 
     * @param req
     * @return
     */
    public Shipment dbGet(Long id) {
    	
    	Shipment idCond = new Shipment();
    	idCond.setId(id);
    	idCond.setDelfg(0);
    	Example<Shipment> idSample = Example.of(idCond);
    	if(shipmentRepository.exists(idSample)) {
    		return shipmentRepository.getOne(id);
    	}else {
    		throw new IllegalArgumentException("查询数据不存在,或者被删除");
    	}
    }
    
    /**
     * 
     * @param req
     */
    public Shipment dbInsert(DBInsertRequest req) {
    	Date currentTime = new DateTime().toDate();
    	
    	
    	Long id = shipmentRepository.getLastId()+1;
    	Shipment newRecord = new Shipment();
    	newRecord.setId(id);
    	newRecord.setUsername(req.getUsername());
    	newRecord.setParentId(0L);
    	newRecord.setQuantity(req.getQuantity());
    	newRecord.setInsTm(currentTime);
    	newRecord.setUpdTm(currentTime);
    	newRecord.setDelfg(0);
    	newRecord.setVersion(0);
		DBSaveMessage dbSaveMsg = new DBSaveMessage();
		dbSaveMsg.setContent(newRecord);
		dbSaveMsg.setTimestamp(new DateTime().toDate());
		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);
		
		return newRecord;
    }
    public Shipment dbInsert(Shipment req) {
    	Date currentTime = new DateTime().toDate();
    	Shipment newRecord = new Shipment();
    	newRecord.setUsername(req.getUsername());
    	newRecord.setParentId(req.getParentId());
    	newRecord.setQuantity(req.getQuantity());
    	newRecord.setInsTm(currentTime);
    	newRecord.setUpdTm(currentTime);
    	newRecord.setDelfg(0);
    	newRecord.setVersion(0);
		DBSaveMessage dbSaveMsg = new DBSaveMessage();
		dbSaveMsg.setContent(newRecord);
		dbSaveMsg.setTimestamp(new DateTime().toDate());
		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);
		
		return newRecord;
    }

    
    public List<Shipment> dbInsertBatch(List<DBInsertRequest> reqList) {
    	
    	List<Shipment> result = new ArrayList<Shipment>();
    	for(DBInsertRequest req: reqList) {
        	Date currentTime = new DateTime().toDate();
        	
        	
        	Long id = shipmentRepository.getLastId()+1;
        	Shipment newRecord = new Shipment();
        	newRecord.setId(id);
        	newRecord.setUsername(req.getUsername());
        	newRecord.setParentId(0L);
        	newRecord.setQuantity(req.getQuantity());
        	newRecord.setInsTm(currentTime);
        	newRecord.setUpdTm(currentTime);
        	newRecord.setDelfg(0);
        	newRecord.setVersion(0);
    		DBSaveMessage dbSaveMsg = new DBSaveMessage();
    		dbSaveMsg.setContent(newRecord);
    		dbSaveMsg.setTimestamp(new DateTime().toDate());
    		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);
    		
    		result.add(newRecord);
    	}

		
		return result;
    }


    /**
     * 
     * @param req
     */
    public Shipment dbUpdate(DBUpdateRequest req) {
    	
    	Shipment updRecord = dbGet(req.getId());
    	
    	Date currentTime = new DateTime().toDate();
    	updRecord.setUsername(req.getUsername());
    	updRecord.setQuantity(req.getQuantity());
    	updRecord.setUpdTm(currentTime);
    	updRecord.setVersion(updRecord.getVersion() + 1);
		DBSaveMessage dbSaveMsg = new DBSaveMessage();
		dbSaveMsg.setContent(updRecord);
		dbSaveMsg.setTimestamp(new DateTime().toDate());
		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);
		
		
		
		return updRecord;
    }

    public Shipment dbUpdate(Shipment req) {
    	
    	Shipment updRecord = dbGet(req.getId());
    	
    	Date currentTime = new DateTime().toDate();
    	updRecord.setUsername(req.getUsername());
    	updRecord.setQuantity(req.getQuantity());
    	updRecord.setUpdTm(currentTime);
    	updRecord.setVersion(updRecord.getVersion() + 1);
		DBSaveMessage dbSaveMsg = new DBSaveMessage();
		dbSaveMsg.setContent(updRecord);
		dbSaveMsg.setTimestamp(new DateTime().toDate());
		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);
		
		
		
		return updRecord;
    }

    /**
     * 
     * @param req
     */
    public Shipment dbDelete(DBDeleteRequest req) {
    	
    	Shipment updRecord = dbGet(req.getId());
    	
    	Date currentTime = new DateTime().toDate();
    	updRecord.setUpdTm(currentTime);
    	updRecord.setDelfg(1);
    	updRecord.setVersion(updRecord.getVersion() + 1);
		DBSaveMessage dbSaveMsg = new DBSaveMessage();
		dbSaveMsg.setContent(updRecord);
		dbSaveMsg.setTimestamp(new DateTime().toDate());
		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);

		return updRecord;

    }
    
    public Shipment dbDelete(Long id) {
    	
    	Shipment updRecord = dbGet(id);
    	
    	Date currentTime = new DateTime().toDate();
    	updRecord.setUpdTm(currentTime);
    	updRecord.setDelfg(1);
    	updRecord.setVersion(updRecord.getVersion() + 1);
		DBSaveMessage dbSaveMsg = new DBSaveMessage();
		dbSaveMsg.setContent(updRecord);
		dbSaveMsg.setTimestamp(new DateTime().toDate());
		dbSaveThreadPool.execute(dbSaveSender, dbSaveMsg);

		return updRecord;

    }

    
    /**
     * 库存拆分
     * @param req
     * @return
     */
    
    public ShipmentNode shipmentSplit(ShipmentSplitRequest req) {
    	
    	ShipmentNode result = new ShipmentNode();
    	Shipment rootNode = dbGet(req.getId());
    	result.setId(rootNode.getId());
    	result.setUsername(rootNode.getUsername());
    	result.setParentId(rootNode.getParentId());
    	result.setInsTm(rootNode.getInsTm());
    	result.setUpdTm(rootNode.getUpdTm());
    	result.setDelfg(rootNode.getDelfg());
    	result.setVersion(rootNode.getVersion());
    	
    	
    	Long sum = 0L;
    	for(Long quantity: req.getQuantityList()) {
    		if(quantity == 0L) {
    			throw new IllegalArgumentException("拆分数量不能是0");
    		}
    		sum = sum + quantity;
    	}
    	// 判断传入的所有数量是否和当前库存一致
    	if(sum != rootNode.getQuantity()) {
    		throw new IllegalArgumentException("当前数据总和 与库存数量不一致");
    	}
    	
    	// 查询是否存在所有以rootId为 父节点的记录, 如果存在全部删除
    	Shipment findChildrenCond = new Shipment();
    	findChildrenCond.setParentId(rootNode.getId());
    	findChildrenCond.setDelfg(0);
    	Example<Shipment> findChildrenExample = Example.of(findChildrenCond);
    	List<Shipment> findChildren = shipmentRepository.findAll(findChildrenExample);
    	if(findChildren != null && findChildren.size() > 0) {
    		for(Shipment findChild: findChildren) {
    			dbDelete(findChild.getId());
    		}
    	}

    	// 查询当前rootNode是最上级节点（仅支持2级拆分）
    	if(rootNode.getParentId() != 0L) {
    		// 父级ID非0的表示不是top层的节点，也就是拆分过的节点
    		throw new IllegalArgumentException("不支持拆分过的子节点继续拆分");
    	}
    	
		if(result.getChildren() == null) {
			result.setChildren(new LinkedList<ShipmentNode>());
		}

    	//开始拆分
    	for(Long quantity: req.getQuantityList()) {
    		Shipment newNode = new Shipment();
    		newNode.setQuantity(quantity);
    		newNode.setParentId(rootNode.getId());
    		newNode.setUsername(rootNode.getUsername());
    		dbInsert(newNode);
    		
    		ShipmentNode child = new ShipmentNode();
    		child.setId(newNode.getId());
    		child.setUsername(newNode.getUsername());
    		child.setParentId(newNode.getParentId());
    		child.setInsTm(newNode.getInsTm());
    		child.setUpdTm(newNode.getUpdTm());
    		child.setDelfg(newNode.getDelfg());
    		child.setVersion(newNode.getVersion());
    		
    		result.getChildren().add(child);
    	}

    	return result;
    	
    }
    
    /**
     * 库存合并
     * @param req
     * @return
     */
    public ShipmentNode shipmentMerge(ShipmentMergeRequest req) {
    	
    	ShipmentNode result = new ShipmentNode();
    	Shipment parentNode = new Shipment();
    	Long parentId = 0L;
    	Long sum = 0L;
    	
    	Shipment newRecode = new Shipment();
    	
    	Map<Long,Long> existIds = new HashMap<Long,Long>();
    	for(Long id: req.getIdList()) {
        	Shipment childNode = dbGet(id);
        	
        	// 父节点为 0的，是顶底节点 不能合并
        	if(childNode.getParentId() == 0L) {
    			throw new IllegalArgumentException("顶级节点不能合并");
        	}
        	if(existIds.containsKey(id)) {
    			throw new IllegalArgumentException("合并ID不能重复");
        	}
        	existIds.put(id, id);
    		
        	if(parentId == 0L) {
        		parentId = childNode.getParentId();
        		newRecode.setParentId(childNode.getParentId());
        		newRecode.setUsername(childNode.getUsername());
        		newRecode.setDelfg(0);
        		
        		// 设置父级节点
            	parentNode = dbGet(parentId);
        		result.setId(parentNode.getId());
        		result.setUsername(parentNode.getUsername());
        		result.setParentId(parentNode.getParentId());
        		result.setInsTm(parentNode.getInsTm());
        		result.setUpdTm(parentNode.getUpdTm());
        		result.setDelfg(parentNode.getDelfg());
        		result.setVersion(parentNode.getVersion());
        		
        		
        	}else if(parentId != childNode.getParentId()) {
        		// 不同父级节点不能合并
        		throw new IllegalArgumentException("存在不同的父级节点，不能合并");
        	}
        	// 进行后续的合并
        	sum = sum + childNode.getQuantity();
        	newRecode.setQuantity(sum);
        	
        	// 删除原有节点
        	dbDelete(childNode.getId());
    	}
    	
    	// 如果合并数量 和原父节点相同，直接删除所有节点即可
    	if(sum != parentNode.getQuantity()) {
    		// 插入新节点
    		dbInsert(newRecode);
    	}

    	// 查询是否存在所有以rootId为 父节点的记录, 如果存在全部删除
    	Shipment findChildrenCond = new Shipment();
    	findChildrenCond.setParentId(result.getId());
    	findChildrenCond.setDelfg(0);
    	Example<Shipment> findChildrenExample = Example.of(findChildrenCond);
    	List<Shipment> findChildren = shipmentRepository.findAll(findChildrenExample);
		if(result.getChildren() == null) {
			result.setChildren(new LinkedList<ShipmentNode>());
		}

    	if(findChildren != null && findChildren.size() > 0) {
    		for(Shipment findChild: findChildren) {
        		ShipmentNode child = new ShipmentNode();
        		child.setId(findChild.getId());
        		child.setUsername(findChild.getUsername());
        		child.setParentId(findChild.getParentId());
        		child.setInsTm(findChild.getInsTm());
        		child.setUpdTm(findChild.getUpdTm());
        		child.setDelfg(findChild.getDelfg());
        		child.setVersion(findChild.getVersion());
        		result.getChildren().add(child);
    		}
    	}
    	return result;
    	
    }
    
    /**
     * 修改根部库存数量
     * @param req
     * @return
     */
    public ShipmentNode shipmentRootIncrease(ShipmentRootIncreaseRequest req) {
    	
    	ShipmentNode result = new ShipmentNode();
    	
    	Shipment parentNode = dbGet(req.getId());
    	
    	// 查询是否是父节点
    	Shipment parentCond = new Shipment();
    	parentCond.setParentId(req.getId());
    	parentCond.setDelfg(0);
    	Example<Shipment> parentCondExample = Example.of(parentCond);
    	List<Shipment> childNodes = shipmentRepository.findAll(parentCondExample);
    	
    	List<Shipment> updChildNodes = new ArrayList<Shipment>();
    	
    	if(childNodes.size() > 0) {
    		// 有子节点

			// 按平均数分配
			// 平均分配，只要将最后一个节点的数量，设置为 总数 - 累加 即可

			// 库存增量
			Long plusquan = req.getPlusquan();
			// 最终应得总和
			Long goals = parentNode.getQuantity() + plusquan;
			// 计算所有子节点的平均数
			Long average = plusquan / childNodes.size();
			// 累加库存
			Long sum = 0L;
			if(result.getChildren() == null) {
				result.setChildren(new LinkedList<ShipmentNode>());
			}

			
			for(int i = 0; i < childNodes.size(); i++) {
				Shipment item = new Shipment();
				item.setId(childNodes.get(i).getId());
				item.setParentId(childNodes.get(i).getParentId());
				item.setUsername(childNodes.get(i).getUsername());
				item.setQuantity(childNodes.get(i).getQuantity());
				
				item.setQuantity(item.getQuantity() + average);
				// 剩余多少没有加计算出来
				plusquan = plusquan - average;

				sum = sum + item.getQuantity();
				
				// 最后节点加多余的数量
				if(i == childNodes.size() - 1) {
					item.setQuantity(item.getQuantity() + (goals - sum));
				}
				updChildNodes.add(item);

			}
		    		
    		for(Shipment updChild: updChildNodes) {
    			dbUpdate(updChild);
    			
        		ShipmentNode child = new ShipmentNode();
        		child.setId(updChild.getId());
        		child.setUsername(updChild.getUsername());
        		child.setParentId(updChild.getParentId());
        		child.setInsTm(updChild.getInsTm());
        		child.setUpdTm(updChild.getUpdTm());
        		child.setDelfg(updChild.getDelfg());
        		child.setVersion(updChild.getVersion());
    			result.getChildren().add(child);
    		}
    	}
    	
    	// 更新父节点数量
    	parentNode.setQuantity(parentNode.getQuantity() + req.getPlusquan());
    	dbUpdate(parentNode);
    	
    	result.setId(parentNode.getId());
    	result.setUsername(parentNode.getUsername());
    	result.setParentId(parentNode.getParentId());
    	result.setQuantity(parentNode.getQuantity());
    	result.setInsTm(parentNode.getInsTm());
    	result.setUpdTm(parentNode.getUpdTm());
    	result.setDelfg(parentNode.getDelfg());
    	result.setVersion(parentNode.getVersion());

    	return result;
    	
    }}
