package myImplement;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class QuartScheduler {
    public static void main(String[] args) throws SchedulerException, IOException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule() //使用SimpleTrigger
                        .withIntervalInSeconds(10) //每隔一秒执行一次
                        .repeatForever())
                .build();

        JobDetail jobDetail = JobBuilder.newJob(MainJob.class)
                .withIdentity("访问公文通", "group1")
                .usingJobData("name", "quartz")
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        System.in.read();
        scheduler.shutdown();
    }
}
