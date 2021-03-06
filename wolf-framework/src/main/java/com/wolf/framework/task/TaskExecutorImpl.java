package com.wolf.framework.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author aladdin
 */
public class TaskExecutorImpl implements TaskExecutor {

    private final ThreadPoolExecutor threadPoolExecutor;
    
    private final ScheduledExecutorService scheduledExecutorService;

    public TaskExecutorImpl(int corePoolSize, int maxPoolSize) {
        if (maxPoolSize < 0) {
            maxPoolSize = 20;
        }
        if (maxPoolSize > 1000) {
            maxPoolSize = 1000;
        }
        if (corePoolSize < 0) {
            corePoolSize = 5;
        }
        if (corePoolSize > maxPoolSize) {
            corePoolSize = maxPoolSize;
        }
        RejectedExecutionHandler rejectedExecutionHandler = new TaskRejectedExecutionHandlerImpl();
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue();
        this.threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                10000,
                TimeUnit.MILLISECONDS,
                linkedBlockingQueue,
                Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void submit(Task task) {
        this.threadPoolExecutor.execute(task);
    }

    @Override
    public void submit(List<Task> taskList) {
        for (Task task : taskList) {
            this.threadPoolExecutor.execute(task);
        }
    }

    @Override
    public void syncSubmit(Task task) {
        String result = "";
        Future<String> futureTask = this.threadPoolExecutor.submit(task, result);
        try {
            futureTask.get();
        } catch (InterruptedException | ExecutionException ex) {
        }
    }

    @Override
    public void syncSubmit(List<Task> taskList) {
        String result = "";
        Future<String> futureTask;
        List<Future<String>> futureTaskList = new ArrayList(taskList.size());
        for (Task task : taskList) {
            futureTask = this.threadPoolExecutor.submit(task, result);
            futureTaskList.add(futureTask);
        }
        try {
            for (Future<String> future : futureTaskList) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException ex) {
        }
    }

    @Override
    public void schedule(Task task, long delay) {
        this.scheduledExecutorService.schedule(task, delay, TimeUnit.MILLISECONDS);
    }
}
