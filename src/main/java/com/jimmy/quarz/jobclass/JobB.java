package com.jimmy.quarz.jobclass;

import org.quartz.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@DisallowConcurrentExecution
public class JobB implements Job {

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        Date date = new Date();

        System.out.println("JobB is running (" +dateFormat.format(date)+")");
        JobDataMap data = context.getJobDetail().getJobDataMap();
        String message = data.getString("message");
        System.out.println(message);
        throw new RuntimeException();
        /*try{Thread.sleep(1000);}
        catch (Exception exp){*//*NOP*//*}*/

    }

}