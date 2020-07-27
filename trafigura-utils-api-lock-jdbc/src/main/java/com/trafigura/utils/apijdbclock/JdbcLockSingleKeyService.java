package com.trafigura.utils.apijdbclock;

public interface JdbcLockSingleKeyService {
	
	boolean acquire(String key, String storeId, String token, long expiration);
	
	boolean release(String key, String storeId, String token);
	 
	boolean refresh(String key, String storeId, String token, long expiration);
}
