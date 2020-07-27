package com.trafigura.utils.apijdbclock;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StringUtils;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcLockUtils {

	public static String getClientId() {
		return UUID.randomUUID().toString();
	}

	public static Map<String, String> doPostXMLMap(StringBuffer sb) {
		Map<String, String> kvm = new HashMap<String, String>();
		try {
			kvm = XMLUtil.parseXmlStringToMap(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("kvm {}", kvm);
		return kvm;
	}
	public static Map<String, String> paramsToMap(String params) {
		Map<String, String> map = new LinkedHashMap<>();
		if (!StringUtils.isEmpty(params)) {
			String[] array = params.split("&");
			for (String pair : array) {
				if ("=".equals(pair.trim())) {
					continue;
				}
				String[] entity = pair.split("=");
				if (entity.length == 1) {
					map.put(decode(entity[0]), null);
				} else {
					map.put(decode(entity[0]), decode(entity[1]));
				}
			}
		}
		return map;
	}

	public static String decode(String content) {
		try {
			return URLDecoder.decode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Map<String, String> sortHashMap(Map<String, String> map) {
		Map<String, String> result = new HashMap<String, String>();
		Object[] objects = map.keySet().toArray();
		Arrays.sort(objects);
		for (int i = 0; i < objects.length; i++) {
			String key = objects[i].toString();
			result.put(key, map.get(key));
		}
		return result;
	}
	public static String httpRequestToSha(String params, String key) {
		try {
			Mac sha256 = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
			sha256.init(secretKeySpec);
			byte[] bytes = sha256.doFinal(params.getBytes());
			return Hex.encodeHexString(bytes).toUpperCase();
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return "";
	}
}
