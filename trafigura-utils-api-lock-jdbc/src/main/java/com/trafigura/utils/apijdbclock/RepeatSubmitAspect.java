package com.trafigura.utils.apijdbclock;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

	final String aspectkey = "RepeatSubmitAspectKey";
	
	@Autowired
	private JdbcLock jdbcLock;

	@Pointcut("@annotation(repeatSubmit)")
	public void pointCut(RepeatSubmit repeatSubmit) {
	}

	@Around("pointCut(repeatSubmit)")
	public Object around(ProceedingJoinPoint pjp, RepeatSubmit repeatSubmit) throws Throwable {
		long lockSeconds = repeatSubmit.lockTime() * 1000;

		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		Assert.notNull(request, "request can not null");

		// 此处可以用token
		String token = request.getHeader(repeatSubmit.tokenHeader());

		String path = request.getServletPath();
		String lockKey = null;
		String clientId = null;
		String clientIdParam = repeatSubmit.clientIdParam();

		Map<String, String> requestMap = new HashMap<String, String>();
		if (request.getMethod().equals("GET")) {
			String queryString = doGetStr(request);
			if (token == null || StringUtils.isEmpty(token)) {
				token = JdbcLockUtils.httpRequestToSha(queryString, aspectkey);
				lockKey = getKey(token, path);
			}
			log.info("get json: {}", queryString);
			requestMap = JdbcLockUtils.paramsToMap(queryString);
		} else if (request.getMethod().equals("POST")) {
			if (MediaType.APPLICATION_XML_VALUE.equals(request.getContentType())) {
				log.info("get pjp.args: {}", pjp.getArgs());
				StringBuffer xmlStr = new StringBuffer();
				try {
					String str = "";
					BufferedReader br = request.getReader();
					while ((str = br.readLine()) != null) {
						xmlStr.append(str);
					}
					log.info("get pjp.args: {}", xmlStr.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				requestMap = JdbcLockUtils.doPostXMLMap(xmlStr);
				
				Map<String, String> sortedMap = JdbcLockUtils.sortHashMap(requestMap);
				if (token == null || StringUtils.isEmpty(token)) {
					token = JdbcLockUtils.httpRequestToSha(new Gson().toJson(sortedMap), aspectkey);
					lockKey = getKey(token, path);
				}

			}
			if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
				Object[] args = pjp.getArgs();
				String json = new Gson().toJson(args[0]);
//				try {
//					requestMap = new Gson().fromJson(json,new TypeReference<Map<String,String>>(){}.getType());
//				}catch(IllegalStateException e) {
//					e.printStackTrace();
//				}
//				Map<String, String> sortedMap = JdbcLockUtils.sortHashMap(requestMap);
				if (token == null || StringUtils.isEmpty(token)) {
					token = JdbcLockUtils.httpRequestToSha(json, aspectkey);
					lockKey = getKey(token, path);
				}

			}

		} else {
			return null;
		}

		if (requestMap.containsKey(clientIdParam) && requestMap.get(clientIdParam) != null) {
			clientId = requestMap.get(clientIdParam).toString();
		} else {
			clientId = JdbcLockUtils.getClientId();
		}
		Boolean isSuccess = jdbcLock.acquire(lockKey, clientId, token, lockSeconds);
		if (repeatSubmit.refreshBeforeLock()) {
			isSuccess = jdbcLock.refresh(lockKey, clientId,  token, lockSeconds);
		}
		log.info("tryLock key = [{}], clientId = [{}]", lockKey, clientId);

		if (isSuccess) {
			log.info("tryLock success, key = [{}], clientId = [{}]", lockKey, clientId);
			// 获取锁成功
			try {
				// 执行进程
				return pjp.proceed();
			} finally {
				// 解锁
				if (repeatSubmit.unlockInEnd()) {
					jdbcLock.release(lockKey, clientId, token);
				}
				log.info("releaseLock success, key = [{}], clientId = [{}]", lockKey, clientId);
			}
		} else {
			// 获取锁失败，认为是重复提交的请求
			log.info("tryLock fail, key = [{}]", lockKey);
			throw new IllegalArgumentException( "【系统限制】当前业务" + repeatSubmit.lockTime() + "秒内不能重复请求！");
		}

	}

	private String getKey(String token, String path) {
		return token + ":" + path;
	}

	private String doGetStr(HttpServletRequest req) {
		return req.getQueryString();
	}

}
