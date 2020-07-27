package com.trafigura.mq.sender.dbsave;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.trafigura.mq.common.DBSaveMessage;


@Service
public class DBSaveSenderSingleThreadPool {

	private static volatile ExecutorService singleThreadExecutor = null;
	
	public DBSaveSenderSingleThreadPool() {
		singleThreadExecutor = getInstance();
	}
	public ExecutorService getInstance() {
		if(singleThreadExecutor == null) {
			synchronized(DBSaveSenderSingleThreadPool.class) {
				if(singleThreadExecutor == null) {
					singleThreadExecutor = Executors.newSingleThreadExecutor();
				}
			}
		}
		return singleThreadExecutor;
	}
	
	public void execute(DBSaveSender dbSaveSender, DBSaveMessage saveMsg) {
		Runnable task = new DBSaveSenderThread(dbSaveSender, saveMsg);
		singleThreadExecutor.execute(task);
	}
}
