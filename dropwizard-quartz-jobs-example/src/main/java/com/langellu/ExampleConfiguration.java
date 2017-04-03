package com.langellu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.langellu.jobs.JobConfiguration;
import io.dropwizard.Configuration;

public class ExampleConfiguration extends Configuration {

    @JsonProperty("jobs")
    private JobConfiguration jobConfiguration = new JobConfiguration();

    public JobConfiguration getJobConfiguration() {
        return jobConfiguration;
    }

    public void setJobConfiguration(JobConfiguration jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }
}
