package com.trafigura.biz.shipm.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
public class Locks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "lock_key")
	private String lockKey;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "expire_at")
	private String expireAt;
}
