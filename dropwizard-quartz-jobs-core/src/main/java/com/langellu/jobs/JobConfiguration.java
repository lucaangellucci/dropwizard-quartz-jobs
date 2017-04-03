package com.langellu.jobs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class JobConfiguration {

    @JsonProperty("quartz")
    private Map<String, String> quartzConfiguration = new HashMap<>();

    public Map<String, String> getQuartzConfiguration() {
        return quartzConfiguration;
    }

    public void setQuartzConfiguration(Map<String, String> quartzConfiguration) {
        this.quartzConfiguration = quartzConfiguration;
    }
}
