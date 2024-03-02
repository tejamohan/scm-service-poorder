package com.example.scm.repo;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.scm.beans.PurchaseOrderDao;
import com.example.scm.beans.PurchaseOrderItemsBo;
import com.example.scm.beans.UserLogin;

@Repository
public class PurchaseOrderDaoImpl implements PurchaseOrderDaoData{
	
	public static final String insert_query="INSERT INTO scmsupply (suppliersname,phonenumber,email,currency,currencyValue,ponumber)VALUES('','','','','','')";
	


	int rows=0;


    @Autowired
	private JdbcTemplate jdbcTemplate;
	
    String poNumber;
	 
    Long parentId;
    
	@Override
	public Long savePo(PurchaseOrderDao dao) {
		
		
		
		  jdbcTemplate.execute("START TRANSACTION");

	        try {
	            // Insert parent
	        	
	        	String poNumber=getLastRecord();
	    		dao.setPonumber(poNumber);
	    		
	        
	            String insertParentSql = "INSERT INTO poorder (parentName,suppliesrName,ponumber,phoneNumber,email,currencyValue,currency) VALUES (?,?,?,?,?,?,?)";
	            jdbcTemplate.update(insertParentSql, dao.getParentName(),dao.getSuppliesrName(),dao.getPonumber(),dao.getPhoneNumber(),dao.getEmail(),dao.getCurrencyValue(),dao.getCurrency());
                
	            
	        
	            // Retrieve the generated parent_id
	             parentId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

	            // Insert children
	            String insertChildSql = "INSERT INTO poitems (poid, childName,poi_item_name,poi_item_desc,poi_hsn_code,quantity,unit_price,gst_rate,totalprice) VALUES (?, ?,?,?,?,?,?,?,?)";
	            for (PurchaseOrderItemsBo child : dao.getPoItems()) {
	                jdbcTemplate.update(insertChildSql, parentId, child.getChildName(),child.getPoItemName(),child.getItemDesc(),child.getHsnCode(),child.getQuantity(),child.getUnitPrice(),child.getGstRate(),child.getTotalPrice());
	            }

	            // Commit the transaction
	            jdbcTemplate.execute("COMMIT");
	        } catch (Exception e) {
	            // Rollback the transaction if an exception occurs
	            jdbcTemplate.execute("ROLLBACK");
	            throw e; // Re-throw the exception after rolling back
	        }
			return parentId;
		
	    }
		
		
	 @Override	
	 public boolean validateCredentials(String username, String password) {
	        String query = "SELECT COUNT(*) FROM user_credentials WHERE username = ? AND password = ?";
	        int count = jdbcTemplate.queryForObject(query, Integer.class, username, password);
	        return count == 1;
	    }

	 
	  
	  public boolean verifyOTP(String username, String otp) {
	        String query = "SELECT COUNT(*) FROM user_credentials WHERE username = ? AND otp = ?";
	        int count = jdbcTemplate.queryForObject(query, Integer.class, username, otp);
	        return count == 1;
	    }
	  
	 
	  

	  
	  
		
		
		
		
		
		
		
		
		
		
//		PurchaseOrderBo bo=new PurchaseOrderBo();
//		BeanUtils.copyProperties(dao, bo);
//		
//		String poNumber=getLastRecord();
//		bo.setPonumber(poNumber);
//		
//		
//        jdbcTemplate.update(insert_query, bo);      	
//        
//        Long personId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
//        dao.setPoid(personId);
//        
//        
//        dao.getPoItems().stream().forEach((s)->{
//        	
//        	 rows=jdbcTemplate.update("INSERT INTO poitems (poi_item_name, poi_item_desc, poi_hsn_code,quantity,unit_price,gst_rate,status,poid,totalprice) VALUES (?, ?, ?,?,?,?,?,?,?)",
//                     s);
//        });
//        return rows>0?"data inserted Successfully":"data not inserted";
//        }

           

	@Override
	public String updatePoDetails(PurchaseOrderDao dao) {
	    int rows=jdbcTemplate.update(insert_query,dao);
		return rows>0?"data updated Successfully":"data not updated";
	}
	  public static String calculateFinancialYear(Date currentDate) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(currentDate);

	        // Assume financial year starts in April and ends in March
	        int startMonth = Calendar.APRIL;  // Calendar.APRIL is 3
	        int endMonth = Calendar.MARCH;    // Calendar.MARCH is 2

	        int currentYear = calendar.get(Calendar.YEAR);
	        int currentMonth = calendar.get(Calendar.MONTH);

	        if (currentMonth >= startMonth) {
	            // We are in or after the financial year start month
	            return currentYear + "-" + (currentYear + 1);
	        } else {
	            // We are before the financial year start month
	            return (currentYear - 1) + "-" + currentYear;
	        }
	    }
	  public static  String incrementValue(String value) {
	        // Parse the value as an integer
	        int intValue = Integer.parseInt(value);

	        // Increment the value
	        intValue++;

	        // Format the incremented value back to a string with leading zeros
	        return String.format("%03d", intValue);
	    }
	  
	 
		
	
	  public  String getLastRecord() {
	        String sql = "SELECT ponumber FROM poorder ORDER BY poid DESC LIMIT 1";
	        // For databases like MySQL and PostgreSQL, use "LIMIT"
	        // For databases like SQL Server, use "TOP 1" instead of "LIMIT 1"
	        
	        //String initialValue = "001";
	       // String incrementedValue = incrementValue(initialValue);
	    	String incNumber=null;
	    	
	    	
           
            PurchaseOrderDao result=null;
            try {
	    	result = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PurchaseOrderDao.class));
                   
            }catch(EmptyResultDataAccessException  e) {
           
           
            	
            
            }
	      
	        Date currentDate = new Date();
	        String financialYear = calculateFinancialYear(currentDate);
	        
	        
	        if(result==null) {
	        	
	          poNumber="LWT/PO/"+financialYear+"/"+001;
	        
	        
	        return poNumber;
	        }else {
	        	poNumber="LWT/PO/"+financialYear+"/"+001;
	        	  String[] parts = poNumber.split("/");

	              // Get the last part (001)
	              String lastPart = parts[parts.length - 1];

	              // Convert the last part to an integer, increment it, and format it back to a three-digit string
	              int incrementedValue = Integer.parseInt(lastPart) + 1;
	              String newLastPart = String.format("%03d", incrementedValue);

	              // Build the new string
	              StringBuilder result1 = new StringBuilder();
	              for (int i = 0; i < parts.length - 1; i++) {
	                  result1.append(parts[i]).append("/");
	              }
	              result1.append(newLastPart);

	        	return result1.toString();
	        }
	       
	       
	        
	
	    }
	  
	  
	  
	

	@Override
	public boolean updateOTP(String otp,String username) {
		
		
            // Save OTP in database
            String query = "UPDATE user_credentials SET otp = ? WHERE username = ?";
            jdbcTemplate.update(query, otp, username);
        
		
		return true;
	}


	@Override
	public String insertUser(UserLogin user) {
	    UserLogin user1=new UserLogin();
	
	    BeanUtils.copyProperties(user, user1);
		int count=0;
		 String INSERT_USER_SQL =
			        "INSERT INTO user_credentials (username, password, phone_number, otp, otp_generated_time) " +
			        "VALUES (?, ?, ?, ?, ?)";
		count=	jdbcTemplate.update(INSERT_USER_SQL, user1.getUsername(),user1.getPassword(),user1.getPhoneNumber(),user1.getOtp(),user1.getOtpGeneratedTime());
		

		return count==0?"data not inserted":"data inserted";
	} 
		 
       
    
			
	 

}
