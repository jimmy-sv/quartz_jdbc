package com.jimmy.quarz.jobclass;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobA implements Job {

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        Date date = new Date();
        System.out.println("JobB is running (" +dateFormat.format(date)+")");
    }

}