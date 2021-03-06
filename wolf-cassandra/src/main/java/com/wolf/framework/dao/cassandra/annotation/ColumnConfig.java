package com.wolf.framework.dao.cassandra.annotation;

import com.wolf.framework.dao.ColumnType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于描述entity中各个field的信息
 *
 * @author aladdin
 */
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ColumnConfig {

    /**
     * 列类型
     *
     * @return
     */
    public ColumnType columnType() default ColumnType.COLUMN;
    
    /**
     * 列映射
     * @return 
     */
    public String dataMap() default "";
    
    /**
     * 数据为空时，取默认值
     * @return 
     */
    public String defaultValue() default "";

    /**
     * 描述
     *
     * @return
     */
    public String desc();
}
