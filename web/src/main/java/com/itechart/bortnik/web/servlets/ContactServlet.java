package com.itechart.bortnik.web.servlets;

import com.itechart.bortnik.web.schedule.EmailToAdminJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

@MultipartConfig
public class ContactServlet extends HttpServlet {

    private Controller controller;

    public ContactServlet() {
        controller = new Controller();
        initializeEmailTrigger();
    }

    private void initializeEmailTrigger() {
        try {
            JobDetail job = JobBuilder.newJob(EmailToAdminJob.class).withIdentity("emailToAdminJob",
                    "group1").build();

            Trigger trigger = newTrigger()
                    .withIdentity("emailToAdminTrigger", "group1")
                    .withSchedule(dailyAtHourAndMinute(10, 0))
                    .forJob(job)
                    .build();
            //every day at 10 am; http://www.cronmaker.com/
//          Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("cronTrigger", "group1")
//                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 1/1 * ? *")).build();
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(ContactServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        parseUrl(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        parseUrl(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        parseUrl(request, response);
    }

    private void parseUrl(HttpServletRequest request, HttpServletResponse response) {
        //URI starts with /contacts, which gives us an empty String in the beginning if regex = "/contacts"
        String[] urlArray = request.getRequestURI().split("contacts", 2);
        String urlPart = request.getMethod() + "_";

        if (urlArray.length > 1) {
            urlPart += urlArray[1];
            logger.info("Incoming request: {}", urlPart);
        }
        try {
            controller.processString(urlPart, request, response);
        } catch (ServletException e) {
            e.printStackTrace();
            logger.error("Exception in parseUrl method: ", e);
        } catch (IOException e) {
            logger.error("Exception in parseUrl method: ", e);
        }
    }

}
