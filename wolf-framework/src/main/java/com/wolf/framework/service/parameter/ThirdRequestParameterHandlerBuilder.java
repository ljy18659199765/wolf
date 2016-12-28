package com.wolf.framework.service.parameter;

import com.wolf.framework.service.parameter.request.BooleanRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.ChinaMobileRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.DateRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.DateTimeRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.DoubleRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.EmailRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.EnumRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.LongArrayRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.LongRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.RegexRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.StringArrayRequestParameterHandlerImpl;
import com.wolf.framework.service.parameter.request.StringRequestParameterHandlerImpl;

/**
 *
 * @author jianying9
 */
public class ThirdRequestParameterHandlerBuilder {

    private final ThirdRequestConfig requestConfig;

    public ThirdRequestParameterHandlerBuilder(final ThirdRequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public RequestParameterHandler build() {
        RequestParameterHandler parameterHandler = null;
        final String fieldName = this.requestConfig.name();
        //
        //基本数据类型
        RequestDataType dataType = this.requestConfig.dataType();
        long max = this.requestConfig.max();
        long min = this.requestConfig.min();
        boolean ignoreEmpty = this.requestConfig.ignoreEmpty();
        String text = this.requestConfig.text();
        switch (dataType) {
            case STRING:
                parameterHandler = new StringRequestParameterHandlerImpl(fieldName, max, min, ignoreEmpty);
                break;
            case DATE:
                parameterHandler = new DateRequestParameterHandlerImpl(fieldName, ignoreEmpty);
                break;
            case DATE_TIME:
                parameterHandler = new DateTimeRequestParameterHandlerImpl(fieldName, ignoreEmpty);
                break;
            case LONG:
                parameterHandler = new LongRequestParameterHandlerImpl(fieldName, max, min, ignoreEmpty);
                break;
            case DOUBLE:
                parameterHandler = new DoubleRequestParameterHandlerImpl(fieldName, max, min, ignoreEmpty);
                break;
            case BOOLEAN:
                parameterHandler = new BooleanRequestParameterHandlerImpl(fieldName, ignoreEmpty);
                break;
            case ENUM:
                String[] enumValues = text.split(",");
                parameterHandler = new EnumRequestParameterHandlerImpl(fieldName, enumValues, ignoreEmpty);
                break;
            case REGEX:
                parameterHandler = new RegexRequestParameterHandlerImpl(fieldName, text, ignoreEmpty);
                break;
            case CHINA_MOBILE:
                parameterHandler = new ChinaMobileRequestParameterHandlerImpl(fieldName, ignoreEmpty);
                break;
            case EMAIL:
                parameterHandler = new EmailRequestParameterHandlerImpl(fieldName, ignoreEmpty);
                break;
            case LONG_ARRAY:
                parameterHandler = new LongArrayRequestParameterHandlerImpl(fieldName, ignoreEmpty);
                break;
            case STRING_ARRAY:
                parameterHandler = new StringArrayRequestParameterHandlerImpl(fieldName, ignoreEmpty);
                break;
        }
        return parameterHandler;
    }
}
