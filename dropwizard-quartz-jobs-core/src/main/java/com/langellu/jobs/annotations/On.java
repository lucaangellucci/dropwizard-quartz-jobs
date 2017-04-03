package com.langellu.jobs.annotations;

import org.quartz.Trigger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by langellu on 24/03/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface On {

    String value();

    String jobName() default "";

    int priority() default Trigger.DEFAULT_PRIORITY;
}
