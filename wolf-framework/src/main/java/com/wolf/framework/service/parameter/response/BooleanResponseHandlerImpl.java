package com.wolf.framework.service.parameter.response;

import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.service.parameter.ResponseHandler;

/**
 * boolean类型处理类
 *
 * @author aladdin
 */
public final class BooleanResponseHandlerImpl implements ResponseHandler {

    private final String name;

    public BooleanResponseHandlerImpl(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ResponseDataType getDataType() {
        return ResponseDataType.BOOLEAN;
    }

    @Override
    public Object getResponseValue(Object value) {
        if(Boolean.class.isInstance(value) == false) {
            String errMsg = "response:" + this.name + "'s type is not Boolean.";
            throw new RuntimeException(errMsg);
        }
        return value;
    }
}
