package com.example.scm.service;

import com.example.scm.beans.PurchaseOrderBo;
import com.example.scm.beans.UserLogin;

public interface PurchseOrderService {
	
	public String savePoData(PurchaseOrderBo bo);
	public String updatePoData(PurchaseOrderBo bo);
	public boolean validateCredentials(String username, String password);
	boolean validateCredentials(String username, String password, String phoneNumber) throws Exception;
	public String saveCredentials(UserLogin login);
}
