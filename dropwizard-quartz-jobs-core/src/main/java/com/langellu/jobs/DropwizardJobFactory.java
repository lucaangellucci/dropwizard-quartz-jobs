package com.langellu.jobs;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by langellu on 24/03/2017.
 */
public class DropwizardJobFactory implements JobFactory {

    private Map<String, Job> jobMap;

    public DropwizardJobFactory(Job[] jobs) {
        initialize(jobs);
    }

    private void initialize(Job[] jobs) {
        jobMap = new HashMap();
        for(Job j : jobs) {
            jobMap.put(j.getClass().getCanonicalName(), j);
        }
    }

    public void registerInstance(Job job) {
        jobMap.put(job.getClass().getCanonicalName(), job);
    }

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = triggerFiredBundle.getJobDetail();
        if(jobMap.containsKey(jobDetail.getJobClass().getCanonicalName())) {
            return jobMap.get(jobDetail.getJobClass().getCanonicalName());
        }
        throw new SchedulerException("Job instance not found");
    }
}
