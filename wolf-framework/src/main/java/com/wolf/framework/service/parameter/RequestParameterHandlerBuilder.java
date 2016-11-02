package com.wolf.framework.service.parameter;

import com.wolf.framework.data.DataHandler;
import com.wolf.framework.data.DataHandlerFactory;
import com.wolf.framework.data.DataType;

/**
 *
 * @author aladdin
 */
public class RequestParameterHandlerBuilder {

    private final RequestConfig requestConfig;
    private final ParameterContext parameterContext;

    public RequestParameterHandlerBuilder(
            final RequestConfig inputConfig,
            final ParameterContext parameterContext) {
        this.requestConfig = inputConfig;
        this.parameterContext = parameterContext;
        
    }

    public RequestParameterHandler build() {
        RequestParameterHandler parameterHandler = null;
        final String fieldName = this.requestConfig.name();
        //
        final DataHandlerFactory dataHandlerFactory = this.parameterContext.getDataHandlerFactory();
        //基本数据类型
        DataType dataType = this.requestConfig.dataType();
        long max = this.requestConfig.max();
        long min = this.requestConfig.min();
        DataHandler dataHandler = dataHandlerFactory.getDataHandler(dataType);
        if (dataHandler == null && dataType.equals(DataType.STRING) == false) {
            throw new RuntimeException("Error building InputParameterHandler. Cause: could not find DataHandler:" + dataType.name());
        }
        switch (dataType) {
            case OBJECT:
                throw new RuntimeException("Error building InputParameterHandler. Cause: input not support JSON OBJECT");
            case ARRAY:
                throw new RuntimeException("Error building InputParameterHandler. Cause: input not support JSON ARRAY");
            case STRING:
                parameterHandler = new StringParameterHandlerImpl(fieldName, null, max, min);
                break;
            case DATE:
                parameterHandler = new DateParameterHandlerImpl(fieldName, dataHandler);
                break;
            case DATE_TIME:
                parameterHandler = new DateParameterHandlerImpl(fieldName, dataHandler);
                break;
            case LONG:
                parameterHandler = new NumberParameterHandlerImpl(fieldName, dataHandler, max, min);
                break;
            case DOUBLE:
                parameterHandler = new NumberParameterHandlerImpl(fieldName, dataHandler, max, min);
                break;
            case BOOLEAN:
                parameterHandler = new BooleanParameterHandlerImpl(fieldName, dataHandler);
                break;
            case ENUM:
                String text = this.requestConfig.text();
                String[] enumValues = text.split("|");
                parameterHandler = new EnumParameterHandlerImpl(fieldName, enumValues);
                break;
        }
        return parameterHandler;
    }
}
