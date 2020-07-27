package com.trafigura.common.vo;

import java.util.HashMap;
import java.util.Map;

public class REQ extends HashMap<String, Object> {
    private static final long serialVersionUID = -3296183596907682561L;
    private String code = "200";
    private String msg = "";
    private Object result;

    public REQ() {
        put("code", "200");
        put("msg", "success");
        put("result", null);
    }

    public static REQ error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static REQ error(String msg) {
        return error(500, msg);
    }

    public static REQ error(int code, String msg) {
    	REQ r = new REQ();
        r.put("code", code);
        r.put("msg", msg);
        r.put("result", null);
        return r;
    }

    public static REQ ok(String msg) {
    	REQ r = new REQ();
        r.put("msg", msg);
        return r;
    }

    public static REQ ok(Map<String, Object> map) {
    	REQ r = new REQ();
        r.putAll(map);
        return r;
    }

    public static REQ ok() {
        return new REQ();
    }

    @Override
    public REQ put(String key, Object value) {
        super.put(key, value);
        return this;
    }


    public REQ putResult(Object value) {
        if (value != null) {
            put("result", value);
        }
        return this;
    }

    public String getCode() {
        return code = (String)this.get("code");
    }

    public String getMsg() {
        return msg = (String)this.get("msg");
    }

    public Object getResult() {
        return result = (Object) this.get("result");
    }

}
