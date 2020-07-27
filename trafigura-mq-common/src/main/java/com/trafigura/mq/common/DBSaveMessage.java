package com.trafigura.mq.common;

import java.io.Serializable;
import java.util.Date;

public class DBSaveMessage<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1762260610373852141L;
	private T content;
    private Date timestamp;

    public DBSaveMessage() {
    }

    public DBSaveMessage(T content, Date timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
