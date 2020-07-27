package com.trafigura.utils.apijdbclock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /**
     * 设置请求锁定时间，默认1秒
     *
     * @return
     */
    int lockTime() default 1;
    
    /**
     * 设置客户端ID
     * @return
     */
    String clientIdParam() default "uopid";

    /**
     * 设置TokenID，如果手工控制认为同一个客户端 
     * 一段时间内只能请求一次，请自行设置该值
     * @return
     */
    String tokenHeader() default "noRepeatToken";
    
    /**
     * true: 在当前方法执行之后，释放，推荐在查询时使用
     * false: 会在 lockTime到期时，释放，推荐在更新时使用
     */
    boolean unlockInEnd() default false;
    
    /**
     * 如果有相同的key，是否要刷新时间戳
     * @return
     */
    boolean refreshBeforeLock() default false;
    
}
