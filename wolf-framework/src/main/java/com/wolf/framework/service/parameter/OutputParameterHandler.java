package com.wolf.framework.service.parameter;

/**
 *
 * @author aladdin
 */
public interface OutputParameterHandler {

    public String getName();

    public String getDataType();

    public String getDescription();

    public String getJson(String value);

    public String getRandomValue();
}
