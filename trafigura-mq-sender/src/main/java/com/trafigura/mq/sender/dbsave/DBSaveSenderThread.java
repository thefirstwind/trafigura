package com.trafigura.mq.sender.dbsave;

import com.trafigura.mq.common.DBSaveMessage;

public class DBSaveSenderThread implements Runnable {
	
	private DBSaveSender saveSend = null;
	
	private DBSaveMessage<?> saveMessage = null;

	public DBSaveSenderThread(DBSaveSender _orderSend, DBSaveMessage<?> _message) {
		this.saveSend = _orderSend;
		this.saveMessage = _message;
	}

	@Override
	public void run() {
		this.saveSend.send(saveMessage);
	}
}
