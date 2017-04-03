package com.langellu;

import com.langellu.jobs.annotations.On;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by langellu on 27/03/2017.
 */
@On(value = "0/1 * * * * ?", jobName = "jobTest1")
public class ExampleJob implements Job {

    private PersonDao personDao;

    public ExampleJob(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Execute jobs "+ personDao.getClass().getCanonicalName());
        System.out.println(jobExecutionContext.getJobDetail().getKey().getName());
    }
}
