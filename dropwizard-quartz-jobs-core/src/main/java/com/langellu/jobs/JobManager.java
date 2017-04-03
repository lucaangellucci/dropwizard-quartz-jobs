package com.langellu.jobs;

import com.langellu.jobs.annotations.Every;
import com.langellu.jobs.annotations.On;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by langellu on 24/03/2017.
 */
public class JobManager implements Managed {

    private static final Logger logger = LoggerFactory.getLogger(JobManager.class);

    private Scheduler scheduler;

    private JobConfiguration jobConfiguration;

    private DropwizardJobFactory jobFactory;

    private List<Job> jobsList = new ArrayList<>();

    public JobManager(JobConfiguration jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }

    @Override
    public void start() throws Exception {
        createScheduler();
        scheduler.start();
        registerAllJobs();
    }

    @Override
    public void stop() throws Exception {
        scheduler.shutdown(true);
    }

    private void createScheduler() throws SchedulerException {
        if(jobConfiguration.getQuartzConfiguration().isEmpty()) {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        }
        else {
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Properties properties = new Properties();
            properties.putAll(jobConfiguration.getQuartzConfiguration());
            schedulerFactory.initialize(properties);
            scheduler = schedulerFactory.getScheduler();
        }
        jobFactory = new DropwizardJobFactory(new Job[]{});
        scheduler.setJobFactory(jobFactory);
    }

    public void registerJobs(Job... jobs) {
        jobsList = Arrays.asList(jobs);
    }

    private void registerAllJobs() throws SchedulerException {

        for(Job job : jobsList) {
            Every everyAnnotation = job.getClass().getAnnotation(Every.class);
            On onAnnotation = job.getClass().getAnnotation(On.class);

            if (everyAnnotation != null && onAnnotation != null) {
                throw new IllegalStateException("Job class cannot be annotated with on and every.");
            }

            if (everyAnnotation != null) {
                registerEveryJob(job, everyAnnotation);
            } else if (onAnnotation != null) {
                registerOnJob(job, onAnnotation);
            }
        }
    }

    private void registerEveryJob(Job job, Every everyAnnotation) throws SchedulerException {
        String period = everyAnnotation.value();
        String key = StringUtils.isNotBlank(everyAnnotation.jobName()) ? everyAnnotation.jobName() : job.getClass().getCanonicalName();
        long delay = everyAnnotation.delay();
        int priority = everyAnnotation.priority();
        int repeatCount = everyAnnotation.repeatCount();

        long periodMilliseconds = parsePeriod(period);

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(periodMilliseconds);

        if(repeatCount > -1) {
            scheduleBuilder.withRepeatCount(repeatCount);
        }
        else {
            scheduleBuilder.repeatForever();
        }

        DateTime start = new DateTime();
        if(delay > 0) {
            start = start.plus(delay);
        }

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(start.toDate())
                .withSchedule(scheduleBuilder)
                .withPriority(priority)
                .build();

        JobKey jobKey = JobKey.jobKey(key);

        createOrUpdateJob(jobKey, job.getClass(), trigger);

        jobFactory.registerInstance(job);
    }

    private long parsePeriod(String period) {
        return 0;
    }

    private void registerOnJob(Job job, On onAnnotation) throws SchedulerException {
        String cron = onAnnotation.value();
        String key = StringUtils.isNotBlank(onAnnotation.jobName()) ? onAnnotation.jobName() : job.getClass().getCanonicalName();
        int priority = onAnnotation.priority();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(scheduleBuilder)
                .withPriority(priority)
                .build();

        JobKey jobKey = JobKey.jobKey(key);

        createOrUpdateJob(jobKey, job.getClass(), trigger);

        jobFactory.registerInstance(job);
    }

    private void createOrUpdateJob(JobKey jobKey, Class<? extends Job> jobClass, Trigger trigger) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
        if(!scheduler.checkExists(jobKey)) {
            scheduler.scheduleJob(jobDetail, trigger);
        }
        else {
            scheduler.deleteJob(jobKey);
            scheduler.scheduleJob(jobDetail, trigger);
        }
        logger.info("Job "+ jobKey.getName() + " scheduled sucessfully.");
    }

    public DropwizardJobFactory getJobFactory() {
        return jobFactory;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
