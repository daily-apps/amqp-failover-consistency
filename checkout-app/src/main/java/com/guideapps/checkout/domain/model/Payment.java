package com.guideapps.checkout.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment implements Serializable {

	private static final long serialVersionUID = 3146405289004840930L;

	@Id
    @GeneratedValue
    private Long id;
	
	@NotNull
	@Column(nullable = false)
	private Long productId;
	
	@NotNull
	@Column(nullable = false)
	private Long sellerId;
	
	@NotNull
	@Column(nullable = false)
	private Integer quantity;
	
	@NotNull
	@Column(nullable = false)
    private PaymentStatus status;
	
} 
