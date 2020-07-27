package com.trafigura.mq.common;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;

public class OrderMessage implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1762260610373852141L;
	private String content;
    private Date timestamp;

    public OrderMessage() {
    }

    public OrderMessage(String content, Date timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Order{" +
                "content='" + content + '\'' +
                ", timestamp=" + new DateTime(timestamp).toString("yyyy-dd-MM HH:mm:ss.ssa") +
                '}';
    }
}
