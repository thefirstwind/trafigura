package com.trafigura.biz.shipm.vo.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class DBUpdateRequest {
	
	@NotNull(message = "id is must")
	@Min(value=1, message="id >= 1")
	private Long id;
	
	@NotBlank(message = "username is must")
	@Pattern(regexp="[0-9a-zA-Z]{5,8}", message="username is illegal")
	private String username;
	
	@NotNull(message = "quantity is must")
	@Min(value=1, message="quantity >= 1")
	private Long quantity;
	
}
