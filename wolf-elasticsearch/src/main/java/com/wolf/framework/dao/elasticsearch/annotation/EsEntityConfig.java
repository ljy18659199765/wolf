package com.wolf.framework.dao.elasticsearch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * elasticsearch entity annotation
 *
 * @author jianying9
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EsEntityConfig {

    /**
     * 表
     *
     * @return
     */
    public String table();

    /**
     * 类型,默认与table相同
     *
     * @return
     */
    public String type() default "";

    /**
     * 缓存
     *
     * @return
     */
    public boolean cache() default false;

    /**
     * 区分多环境
     *
     * @return
     */
    public boolean multiComplie() default true;

}
