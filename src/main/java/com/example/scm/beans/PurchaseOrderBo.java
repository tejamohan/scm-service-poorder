package com.example.scm.beans;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseOrderBo {
    private Long poid;
	private String suppliesrName;
	private Long phoneNumber;
	private String email;
	private String parentName;
	private String currency; 
	private String currencyValue;
	private String ponumber;
	private List<PurchaseOrderItemsBo> poItems;
	



}