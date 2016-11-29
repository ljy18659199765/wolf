package com.wolf.framework.service.parameter.request;

import com.wolf.framework.data.DataType;
import com.wolf.framework.service.parameter.RequestParameterHandler;

/**
 * 字符类型处理类
 *
 * @author aladdin
 */
public final class StringRequestParameterHandlerImpl implements RequestParameterHandler {

    private final long max;
    private final long min;
    private final String name;
    private final String errorInfo = " must be char";
    

    public StringRequestParameterHandlerImpl(final String name, long max, long min) {
        this.name = name;
        max = max < 0 ? 0 : max;
        min = min < 0 ? 0 : min;
        this.max = max > min ? max : min;
        this.min = max > min ? min : max;
    }
    
    private String gerErrorInfo() {
        StringBuilder err = new StringBuilder(64);
        err.append(this.errorInfo).append('[')
                .append(this.min).append(',').append(this.max).append(']');
        return err.toString();
    }
    
    @Override
    public String validate(String value) {
        String msg = "";
        if(value.length() > this.max | value.length() < this.min) {
            msg = this.gerErrorInfo();
        }
        return msg;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DataType getDataType() {
        return DataType.STRING;
    }
}