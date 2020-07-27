package com.trafigura.biz.shipm.vo.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DBDeleteRequest {
	
	@NotNull(message = "id is must")
	@Min(value=1, message="id >= 1")
	private Long id;
	
}
