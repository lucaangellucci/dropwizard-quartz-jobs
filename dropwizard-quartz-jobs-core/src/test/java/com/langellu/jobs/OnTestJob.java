package com.langellu.jobs;

import com.langellu.jobs.annotations.On;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.CountDownLatch;

/**
 * Created by langellu on 27/03/2017.
 */
@On("0/1 * * * * ?")
public class OnTestJob implements Job{

    private CountDownLatch latch;

    public OnTestJob(int latch) {
        this.latch = new CountDownLatch(latch);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
