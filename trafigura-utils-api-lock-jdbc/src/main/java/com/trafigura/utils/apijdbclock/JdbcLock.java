package com.trafigura.utils.apijdbclock;
import java.util.Date;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Component
@AllArgsConstructor
@Data
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class JdbcLock implements JdbcLockSingleKeyService{

	  public static final String ACQUIRE_FORMATTED_QUERY = "INSERT INTO LOCKS (lock_key, token, expire_at) VALUES (?, ?, ?);";
	  public static final String RELEASE_FORMATTED_QUERY = "DELETE FROM LOCKS WHERE lock_key = ? AND token = ?;";
	  public static final String DELETE_EXPIRED_FORMATTED_QUERY = "DELETE FROM LOCKS WHERE ? > expire_at;";
	  public static final String REFRESH_FORMATTED_QUERY = "UPDATE LOCKS SET expire_at = ? WHERE lock_key = ? AND token = ?;";

	  private final JdbcTemplate jdbcTemplate;

	  @Override
	  public boolean acquire(final String key, final String storeId, final String token, final long expiration) {
	    final Date now = new Date();

	    final int expired = jdbcTemplate.update(String.format(DELETE_EXPIRED_FORMATTED_QUERY, storeId), now);
	    log.debug("Expired {} locks", expired);

	    try {
	      final Date expireAt = new Date(now.getTime() + expiration);
	      final int created = jdbcTemplate.update(String.format(ACQUIRE_FORMATTED_QUERY, storeId), key, token, expireAt);
	      if(created == 1) return true;
	      return false;
	    } catch (final DuplicateKeyException e) {
	      return false;
	    }
	  }

	  @Override
	  public boolean release(final String key, final String storeId, final String token) {
	    final int deleted = jdbcTemplate.update(String.format(RELEASE_FORMATTED_QUERY, storeId), key, token);

	    final boolean released = deleted == 1;
	    if (released) {
	      log.debug("Release query successfully affected 1 record for key {} with token {} in store {}", key, token, storeId);
	    } else if (deleted > 0) {
	      log.error("Unexpected result from release for key {} with token {} in store {}, released {}", key, token, storeId, deleted);
	    } else {
	      log.error("Release query did not affect any records for key {} with token {} in store {}", key, token, storeId);
	    }

	    return released;
	  }

	  @Override
	  public boolean refresh(final String key, final String storeId, final String token, final long expiration) {
	    final Date now = new Date();
	    final Date expireAt = new Date(now.getTime() + expiration);

	    final int updated = jdbcTemplate.update(String.format(REFRESH_FORMATTED_QUERY, storeId), expireAt, key, token);
	    final boolean refreshed = updated == 1;
	    if (refreshed) {
	      log.debug("Refresh query successfully affected 1 record for key {} with token {} in store {}", key, token, storeId);
	    } else if (updated > 0) {
	      log.error("Unexpected result from refresh for key {} with token {} in store {}, refreshed {}", key, token, storeId, updated);
	    } else {
	      log.error("Refresh query did not affect any records for key {} with token {} in store {}", key, token, storeId);
	    }

	    return refreshed;
	  }

}
