package com.jimmy.quarz.scheduler;


import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SchcedService {
    final static Logger logger = LoggerFactory.getLogger(SchcedService.class);

    private Scheduler scheduler;


    public SchcedService(Scheduler scheduler){
        this.scheduler = scheduler;
    }


    public void addJob(JobTrigerHolder jobTrigerHolder) throws SchedulerException {
        if(scheduler.checkExists(jobTrigerHolder.getJob().getKey()) == false){
                scheduler.scheduleJob(jobTrigerHolder.getJob(),jobTrigerHolder.getTrigger());
        }
    }


    public void removeJob(JobTemplate jobTemplate) throws SchedulerException {
        scheduler.deleteJob(new JobKey(jobTemplate.getJobName(),
                jobTemplate.getJobGroup()));
    }


    public void runJob(JobTemplate jobTemplate) throws SchedulerException {
        scheduler.triggerJob(new JobKey(jobTemplate.getJobName(),
                jobTemplate.getJobGroup()));
    }


    public void pauseJob(JobTemplate jobTemplate) throws SchedulerException {
            scheduler.pauseTrigger(new TriggerKey(jobTemplate.getTriggerName(),
                    jobTemplate.getTriggerGroup()));
    }


    public void resumeJob(JobTemplate jobTemplate)throws SchedulerException {
        scheduler.resumeTrigger(new TriggerKey(jobTemplate.getTriggerName(),
                jobTemplate.getTriggerGroup()));
    }


    public List<JobTemplate> getJobs() throws SchedulerException{

        List<JobTemplate> listJob = new ArrayList<JobTemplate>(15);

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();
                String jobClass = scheduler.getJobDetail(jobKey).getJobClass().getName();

                String triggerName = null;
                String triggerGrouop = null;
                String cronExpr = null;
                Map<String,String> dataMap = null;
                String jobStatus = null;

                JobDataMap jobMap = scheduler.getJobDetail(jobKey).getJobDataMap();

                if(jobMap != null && jobMap.size()>0){
                    String[] jobMapKeys =  jobMap.getKeys();
                    dataMap = new HashMap(jobMapKeys.length);
                    for (int i=0 ; i < jobMapKeys.length ; i++){
                        dataMap.put(jobMapKeys[i], jobMap.getString(jobMapKeys[i]));
                    }
                }

                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);

                for(Trigger trigger : triggers){

                    triggerName = trigger.getKey().getName();
                    triggerGrouop = trigger.getKey().getGroup();

                    jobStatus =  scheduler.getTriggerState(trigger.getKey()).name().toLowerCase();

                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        cronExpr = cronTrigger.getCronExpression();

                    }
                }

                JobTemplate jobTemplate = JobTemplateBuilder.newJob()
                        .withJobName(jobName)
                        .withJobGroup(jobGroup)
                        .withTriggerName(triggerName)
                        .withTriggerGroup(triggerGrouop)
                        .withJobClass(jobClass)
                        .withMap(dataMap)
                        .withCronSched(cronExpr)
                        .withJobStatus(jobStatus)
                        .build();
                listJob.add(jobTemplate);
            }
        }

        return listJob;
    }


}
