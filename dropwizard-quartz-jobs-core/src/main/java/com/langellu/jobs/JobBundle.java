package com.langellu.jobs;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public abstract class JobBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private JobManager jobManager;

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        jobManager = new JobManager(getJobsConfiguration(configuration));
        environment.lifecycle().manage(jobManager);
    }

    @Override
    public void initialize(Bootstrap bootstrap) {
        //nothing to do
    }

    public abstract JobConfiguration getJobsConfiguration(T configuration);

    public JobManager getJobManager() {
        return jobManager;
    }
}
