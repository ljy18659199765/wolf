package com.wolf.framework.service.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 第二层请求参数
 *
 * @author jianying9
 */
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SecondRequestConfig {

    /**
     * 参数名
     * @return 
     */
    public String name();

    /**
     * 是否必填,默认ture
     * @return 
     */
    public boolean required() default true;
    
    /**
     * 非必填是,忽略空字符串数据
     * @return 
     */
    public boolean ignoreEmpty() default true;

    /**
     * 数据类型
     *
     * @return
     */
    public RequestDataType dataType();
    
    /**
     * 最大
     * @return 
     */
    public long max() default Long.MAX_VALUE;
    
    /**
     * 最小
     * @return 
     */
    public long min() default 0;
    
    /**
     * 自定义文本
     * @return 
     */
    public String text() default "";
    
    /**
     * 第三层参数请求
     * @return 
     */
    public ThirdRequestConfig[] thirdRequestConfigs() default {};
    
    /**
     * 描述
     *
     * @return
     */
    public String desc();
}
