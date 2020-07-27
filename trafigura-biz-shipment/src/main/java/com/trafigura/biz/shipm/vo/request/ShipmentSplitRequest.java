package com.trafigura.biz.shipm.vo.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ShipmentSplitRequest {
	
	@NotNull(message = "id is must")
	@Min(value=1, message="id >= 1")
	private Long id;
	
	@NotNull(message = "quantityList is must")
	@Size(min = 1, max = 20, message = "size is between 1 and 20")
	private List<Long> quantityList;

}
