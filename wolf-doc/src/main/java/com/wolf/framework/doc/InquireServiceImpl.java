package com.wolf.framework.doc;

import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.data.DataType;
import com.wolf.framework.service.ListService;
import com.wolf.framework.service.ServiceConfig;
import com.wolf.framework.service.context.ServiceContext;
import com.wolf.framework.service.parameter.ResponseConfig;
import com.wolf.framework.service.request.ListServiceRequest;
import com.wolf.framework.service.response.ListServiceResponse;
import com.wolf.framework.worker.ServiceWorker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author jianying9
 */
@ServiceConfig(
        route = "/wolf/service",
        requestConfigs = {},
        responseConfigs = {
            @ResponseConfig(name = "routeName", dataType = DataType.CHAR, desc = ""),
            @ResponseConfig(name = "groupName", dataType = DataType.CHAR, desc = ""),
            @ResponseConfig(name = "desc", dataType = DataType.CHAR, desc = "")
        },
        responseCodes = {},
        validateSession = false,
        validateSecurity = false,
        desc = "")
public class InquireServiceImpl implements ListService {

    @Override
    public void execute(ListServiceRequest listServiceRequest, ListServiceResponse listServiceResponse) {
        Map<String, ServiceWorker> serviceWorkerMap = ApplicationContext.CONTEXT.getServiceWorkerMap();
        Set<Map.Entry<String, ServiceWorker>> entrySet = serviceWorkerMap.entrySet();
        //过滤系统接口
        List<ServiceWorker> serviceWorkerList = new ArrayList<ServiceWorker>(serviceWorkerMap.size());
        ServiceContext serviceContext;
        for (Entry<String, ServiceWorker> entryService : entrySet) {
            serviceContext = entryService.getValue().getServiceContext();
            if (serviceContext.group().equals("wolf") == false) {
                serviceWorkerList.add(entryService.getValue());
            }
        }
        //排序
        Collections.sort(serviceWorkerList, new ServiceWorkerSort());
        //输出
        Map<String, String> resultMap;
        List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>(serviceWorkerList.size());
        for (ServiceWorker serviceWorker : serviceWorkerList) {
            serviceContext = serviceWorker.getServiceContext();
            resultMap = new HashMap<String, String>(4, 1);
            resultMap.put("routeName", serviceContext.route());
            resultMap.put("groupName", serviceContext.group());
            resultMap.put("desc", serviceContext.desc());
            resultMapList.add(resultMap);
        }
        listServiceResponse.setDataMapList(resultMapList);
        listServiceResponse.setNextSize(resultMapList.size());
        listServiceResponse.success();
    }

    private class ServiceWorkerSort implements Comparator<ServiceWorker> {

        @Override
        public int compare(ServiceWorker o1, ServiceWorker o2) {
            ServiceContext s1 = o1.getServiceContext();
            ServiceContext s2 = o2.getServiceContext();
            int result = s1.group().compareTo(s2.group());
            if (result == 0) {
                result = s1.route().compareTo(s2.route());
            }
            return result;
        }
    }
}
