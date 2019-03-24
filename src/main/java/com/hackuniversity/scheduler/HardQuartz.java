package com.hackuniversity.scheduler;

import com.hackuniversity.parser.Parser;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HardQuartz {
    private static final String CONTACTS_FROM_GITHUB = "https://raw.githubusercontent.com/GrigorKhachatryan/Hack2019/Egor_try/contacts.txt";
    private static final String PLAN_FROM_GITHUB = "https://raw.githubusercontent.com/GrigorKhachatryan/Hack2019/master/plan.txt";

    public static void main(String[] args) {

        String numbersFromFile = "";
        List<String> plans = new ArrayList<>();

        try {
            URL url = new URL(CONTACTS_FROM_GITHUB);
            Scanner s = new Scanner(url.openStream());
            numbersFromFile = s.nextLine();
            s.close();

            URL url1 = new URL(PLAN_FROM_GITHUB);
            Scanner s1 = new Scanner(url1.openStream());
            String line;
            while ((line = s1.nextLine()) != null) {
                plans.add(line);
                System.out.println(line);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(numbersFromFile);

        SchedulerFactory schedFact = new StdSchedulerFactory();
        try {
            Parser parser = new Parser();
            int k = 1;

            for (String linePlan : plans) {
                String message = parser.parseMessage(linePlan);
                Date date = parser.parseDate(linePlan);
                String cronExpr = parser.parseCronExpr(date);

                Scheduler sched = schedFact.getScheduler();

                JobDetail jobSender = JobBuilder.newJob(SendJob.class)
                        .withIdentity("SendJob", "group".concat(String.valueOf(k)))
                        .usingJobData("jobSays", message)
                        .usingJobData("numbersString", numbersFromFile)
                        .build();

                CronTrigger triggerB = TriggerBuilder.newTrigger()
                        .withIdentity("trigger1", "group".concat(String.valueOf(k)))
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))//last is a day of week; sec, min, hour, day, month, day of week  "0 52 13 23 3 ?"
                        .forJob("SendJob", "group".concat(String.valueOf(k)))
                        .build();

                sched.scheduleJob(jobSender, triggerB);
                sched.start();
                k++;
            }

        } catch (SchedulerException | ParseException e) {
            e.printStackTrace();
        }

    }
}