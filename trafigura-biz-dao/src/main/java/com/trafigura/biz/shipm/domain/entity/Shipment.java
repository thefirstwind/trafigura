package com.trafigura.biz.shipm.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
public class Shipment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 246295016658941914L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "username is must")
	@Column(name="username")
	private String username;
	
	@NotNull(message = "parent_id is must")
	@Column(name="parent_id")
	private Long parentId;
	
	@NotNull(message = "quantity is must")
	@Column(name="quantity")
	private Long quantity;
	
	@NotNull(message = "ins_tm is must")
	@Column(name="ins_tm")
	private Date insTm;
	
	@NotNull(message = "upd_tm is must")
	@Column(name="upd_tm")
	private Date updTm;
	
	@NotNull(message = "delfg is must")
	@Column(name="delfg")
	private Integer delfg;

	@NotNull(message = "version is must")
	@Column(name="version")
	private Integer version;

}
