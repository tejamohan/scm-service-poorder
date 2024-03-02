package com.example.scm.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseOrderItemsDao {
	
	private Long poItemsId;
	private String poItemName;
	private String childName;
	private String itemDesc;
	private String hsnCode;
	private double quantity;
	private double unitPrice;
	private double gstRate;
	private int status;
	private double totalPrice;
	private Long poid;

}
