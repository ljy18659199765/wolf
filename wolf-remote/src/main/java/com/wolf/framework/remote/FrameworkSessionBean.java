package com.wolf.framework.remote;

import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.worker.ServiceWorker;
import com.wolf.framework.worker.context.LocalMessageContextImpl;
import java.util.Map;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author aladdin
 */
@Stateless
@Startup
@TransactionAttribute(TransactionAttributeType.NEVER)
public class FrameworkSessionBean implements FrameworkSessionBeanRemote {

    @Override
    public String execute(String act, Map<String, String> parameterMap) {
        String result;
        if (ApplicationContext.CONTEXT.isReady()) {
            ServiceWorker serviceWorker = ApplicationContext.CONTEXT.getServiceWorker(act);
            if (serviceWorker == null) {
                result = "{\"flag\":\"INVALID\",\"error\":\"invalid act value\"}";
            } else {
                LocalMessageContextImpl localMessageContextImpl = new LocalMessageContextImpl(null, act, parameterMap);
                serviceWorker.doWork(localMessageContextImpl);
                result = localMessageContextImpl.getResponseMessage();
            }
        } else {
            result = "{\"flag\":\"FAILURE\",\"error\":\"System is not ready! Wait for a moment...\"}";
        }
        return result;
    }
}
