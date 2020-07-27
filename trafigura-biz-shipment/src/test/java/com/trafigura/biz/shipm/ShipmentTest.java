package com.trafigura.biz.shipm;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trafigura.biz.shipm.domain.entity.Shipment;
import com.trafigura.biz.shipm.service.ShipmentService;
import com.trafigura.biz.shipm.vo.request.DBDeleteRequest;
import com.trafigura.biz.shipm.vo.request.DBGetRequest;
import com.trafigura.biz.shipm.vo.request.DBInsertRequest;
import com.trafigura.biz.shipm.vo.request.DBUpdateRequest;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroServiceApplication.class)
@Slf4j
public class ShipmentTest {
	
	@Autowired
	private ShipmentService shipmService;
	
	@Test
	@Transactional
	public void test01DBGet() {
		DBGetRequest req = new DBGetRequest();
		req.setId(1L);
		Shipment shipm = shipmService.dbGet(req.getId());
		log.info("shipm {}", shipm);
		Assert.assertEquals("user1", shipm.getUsername());
	}
	@Test(expected=IllegalArgumentException.class)
	@Transactional
	public void test01DBGet2() {
		DBGetRequest req = new DBGetRequest();
		req.setId(9L);
		Shipment shipm = shipmService.dbGet(req.getId());
		log.info("shipm {}", shipm);
		Assert.assertEquals("user1", shipm.getUsername());
	}

	@Test
	@Transactional
	public void test03DBInsert() {
		DBInsertRequest req = new DBInsertRequest();
		req.setUsername("xxn");
		req.setParentId(1L);
		req.setQuantity(200L);
		Shipment res1 = shipmService.dbInsert(req);
		
		Assert.assertEquals(true, res1 != null);
		
		/**
		 * 消息传输 要等一等
		 */
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		DBGetRequest req2 = new DBGetRequest();
		req2.setId(res1.getId());
		Shipment res2 = shipmService.dbGet(req2.getId());
		Assert.assertEquals("xxn", res2.getUsername());
	}
	@Test
	@Transactional
	public void test04DBUpdate() {
		DBUpdateRequest req = new DBUpdateRequest();
		req.setId(1L);
		req.setUsername("xxn2");
		req.setQuantity(200L);
		Shipment res1 = shipmService.dbUpdate(req);
		
		Assert.assertEquals(true, res1 != null);
		
		/**
		 * 消息传输 要等一等
		 */
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		DBGetRequest req2 = new DBGetRequest();
		req2.setId(res1.getId());
		Shipment res2 = shipmService.dbGet(req2.getId());
		Assert.assertEquals("xxn2", res2.getUsername());
	}
	@Test
	@Transactional
	public void test05DBUpdate() {
		DBDeleteRequest req = new DBDeleteRequest();
		req.setId(1L);
		Shipment res1 = shipmService.dbDelete(req);
		
		Assert.assertEquals(true, res1 != null);
		
		/**
		 * 消息传输 要等一等
		 */
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		DBGetRequest req2 = new DBGetRequest();
		req2.setId(res1.getId());
		Shipment res2 = shipmService.dbGet(req2.getId());
		Assert.assertTrue(1 == res2.getDelfg());
	}
	
	@After
	public  void testDone() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
