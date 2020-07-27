package com.trafigura.biz.shipm.vo.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ShipmentRootIncreaseRequest {

	/**
	 * 根节点ID
	 */
	@NotNull(message = "id is must")
	@Min(1)
	private Long id;
	
	/**
	 * 增加数量
	 */
	@NotNull(message = "plusquan is must")
	@Min(1)
	private Long plusquan;
	
}
