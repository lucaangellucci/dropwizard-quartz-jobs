[![Build Status](https://travis-ci.org/lucaangellucci/dropwizard-quartz-jobs.svg?branch=master)](https://travis-ci.org/lucaangellucci/dropwizard-quartz-jobs)

# Dropwizard Quartz Bundle #
    
This bundle allows to use quartz scheduler and to create background jobs in dropwizard Application.

## Usage ##

You can include the plugin into your pom.xml:

After you need to activate the bundle class and register your jobs in run method

```Java
    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        bootstrap.addBundle(jobBundle);
    }
``` 

```Java
    @Override
    public void run(ExampleConfiguration exampleConfiguration, Environment environment) throws Exception {
        PersonDao personDao = new PersonDao();
        jobBundle.getJobManager().registerJobs(new ExampleJob(personDao));
    }
```




 



