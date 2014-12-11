package com.jimmy.quarz;

import com.jimmy.quarz.scheduler.*;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jimmy.quarz.scheduler.CreateJobFactory.createJob;

public class QuarzApp {
    final static Logger logger = LoggerFactory.getLogger(QuarzApp.class);
    public static void main(String[] args) {

        ClassPathXmlApplicationContext context =  new ClassPathXmlApplicationContext("quarz-spring.xml");

        String jobName = "jobB";
        String jobGroup = "group1";
        String triggerName = "dummyTriggerName2";
        String triggerGroup = "group1";
        String cronSched = "0/5 * * * * ?";
        String jobClassName = "com.jimmy.quarz.jobclass.JobB";
        Map params = new HashMap(1);
        params.put("message","Job message ...");

        logger.info("Begin try");

        Scheduler scheduler = context.getBean("scheduler",Scheduler.class);

        SchcedService service = new SchcedService(scheduler);

        JobTemplate jobTemplate = JobTemplateBuilder.newJob()
                .withJobName(jobName)
                .withJobGroup(jobGroup)
                .withTriggerName(triggerName)
                .withTriggerGroup(triggerGroup)
                .withCronSched(cronSched)
                .withJobClass(jobClassName)
                .withMap(params)
                .build();


        try{

            List<JobTemplate> jobs = service.getJobs();
            for(JobTemplate job : jobs){
                System.out.println("Job name: "+job.getJobName());
                System.out.println("Job group: "+job.getJobGroup());
                System.out.println("Job class: "+job.getJobClassName());
                System.out.println("Triger name: "+job.getTriggerName());
                System.out.println("Triger group: "+job.getTriggerGroup());
                System.out.println("Cron name: "+job.getCronSched());
                System.out.println("Job status: "+job.getJobStatus());

            }

            /*JobTrigerHolder jobTrigerHolder = CreateJobFactory.createJob(jobTemplate);*/

            /*service.addJob(jobTrigerHolder);*/


          /* Thread.sleep(7000);*/
            service.runJob(jobTemplate);
            //System.out.println("JobB  paused");
            //service.pauseJob(jobTemplate);
            //Thread.sleep(10000);
            //System.out.println("JobB resume");
            //service.resumeJob(jobTemplate);
            Thread.sleep(10000);
            scheduler.shutdown(false);
        }
        /*catch (ClassNotFoundException exp) {exp.printStackTrace();}*/
        catch (SchedulerException exp) {exp.printStackTrace();}
        catch (Exception exp){exp.printStackTrace();}




    }

}
