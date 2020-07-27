package com.trafigura.biz.shipm.domain.entity;

import java.util.LinkedList;

import lombok.Data;

@Data
public class ShipmentNode extends Shipment{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2411115745937152547L;
	
	private LinkedList<ShipmentNode> children;
}
