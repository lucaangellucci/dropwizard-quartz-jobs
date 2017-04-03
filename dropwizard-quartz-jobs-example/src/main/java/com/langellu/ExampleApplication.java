package com.langellu;

import com.langellu.jobs.JobBundle;
import com.langellu.jobs.JobConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by langellu on 27/03/2017.
 */
public class ExampleApplication extends Application<ExampleConfiguration> {

    private final JobBundle jobBundle = new JobBundle<ExampleConfiguration>() {
        @Override
        public JobConfiguration getJobsConfiguration(ExampleConfiguration configuration) {
            return configuration.getJobConfiguration();
        }
    };

    @Override
    public String getName() {
        return "QuartzExample";
    }

    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        bootstrap.addBundle(jobBundle);
    }

    @Override
    public void run(ExampleConfiguration exampleConfiguration, Environment environment) throws Exception {
        PersonDao personDao = new PersonDao();
        jobBundle.getJobManager().registerJobs(new ExampleJob(personDao));
    }

    public static void main(String[] args) throws Exception {
        new ExampleApplication().run(args);
    }
}
