package com.langellu.jobs;

import org.junit.Test;
import org.quartz.JobKey;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by langellu on 24/03/2017.
 */
public class JobManagerTest {


    private JobManager jobManager;
    private OnTestJob onTestJob = new OnTestJob(1);

    @Test
    public void testThatOnJobExecuted() throws Exception {
        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setQuartzConfiguration(new HashMap<>());
        jobManager = new JobManager(jobConfiguration);
        jobManager.registerJobs(onTestJob);

        jobManager.start();

        assertTrue(jobManager.getScheduler().checkExists(JobKey.jobKey(onTestJob.getClass().getCanonicalName())));

        assertThat(onTestJob.getLatch().await(2, TimeUnit.SECONDS), is(true));

        jobManager.stop();
    }


}
