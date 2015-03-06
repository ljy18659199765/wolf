package com.wolf.framework.service.parameter;

import com.wolf.framework.data.TypeEnum;
import com.wolf.framework.service.parameter.filter.FilterTypeEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author aladdin
 */
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ResponseConfig {

    /**
     * 参数名
     * @return 
     */
    public String name();

    /**
     * 数据类型
     *
     * @return
     */
    public TypeEnum typeEnum();

    /**
     * 描述
     *
     * @return
     */
    public String desc();

    /**
     * 该parameter在输出时过滤行为
     *
     * @return
     */
    public FilterTypeEnum[] filterTypes() default {FilterTypeEnum.ESCAPE, FilterTypeEnum.SECURITY};
}
