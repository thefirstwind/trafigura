package com.trafigura.biz.shipm.vo.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ShipmentMergeRequest {

	@NotNull(message = "idList is must")
	@Size(min = 2, max = 20, message = "size is between 1 and 20")
	private List<Long> idList;
	
	/**
	 * id 无任何意义，只用来做幂等校验的id使用
	 */
	@NotNull(message = "id is must")
	@Min(1)
	private Long id;

}
