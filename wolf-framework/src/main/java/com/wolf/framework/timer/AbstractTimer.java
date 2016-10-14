package com.wolf.framework.timer;

import com.wolf.framework.config.FrameworkLogger;
import com.wolf.framework.context.ApplicationContext;
import com.wolf.framework.logger.LogFactory;
import com.wolf.framework.worker.ServiceWorker;
import com.wolf.framework.worker.context.LocalWorkerContextImpl;
import com.wolf.framework.worker.context.WorkerContext;
import java.util.Map;
import org.slf4j.Logger;

/**
 *
 * @author aladdin
 */
public class AbstractTimer {

    protected String executeService(final String route, final Map<String, String> parameterMap) {
        String result = "";
        if (ApplicationContext.CONTEXT.isReady()) {
            ServiceWorker serviceWorker = ApplicationContext.CONTEXT.getServiceWorker(route);
            if (serviceWorker == null) {
                Logger logger = LogFactory.getLogger(FrameworkLogger.FRAMEWORK);
                logger.error("timer:Can not find route:".concat(route));
            } else {
                WorkerContext workerContext = new LocalWorkerContextImpl(null, route, parameterMap, serviceWorker);
                serviceWorker.doWork(workerContext);
                result = workerContext.getWorkerResponse().getResponseMessage();
            }
        } else {
            Logger logger = LogFactory.getLogger(FrameworkLogger.FRAMEWORK);
            logger.warn("timer:System is not ready! Wait for next time.");
        }
        return result;
    }
}
