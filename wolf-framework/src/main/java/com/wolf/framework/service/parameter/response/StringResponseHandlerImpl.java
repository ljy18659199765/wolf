package com.wolf.framework.service.parameter.response;

import com.wolf.framework.service.parameter.ResponseDataType;
import com.wolf.framework.service.parameter.filter.Filter;
import com.wolf.framework.service.parameter.ResponseHandler;

/**
 * 字符类型处理类
 *
 * @author jianying9
 */
public final class StringResponseHandlerImpl implements ResponseHandler {

    private final Filter[] filters;
    private final String name;
    private final ResponseDataType responseDataType;

    public StringResponseHandlerImpl(final String name, ResponseDataType responseDataType, final Filter[] filters) {
        this.filters = filters;
        this.name = name;
        this.responseDataType = responseDataType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ResponseDataType getDataType() {
        return this.responseDataType;
    }

    @Override
    public Object getResponseValue(Object value) {
        String result = null;
        if (String.class.isInstance(value)) {
            result = (String) value;
            if (result.isEmpty() == false && this.filters != null) {
                for (Filter filter : filters) {
                    result = filter.doFilter(result);
                }
            }
        } else {
            String errMsg = "response:" + this.name + "'s type is not " + this.responseDataType.name();
            throw new RuntimeException(errMsg);
        }
        return result;
    }
}
