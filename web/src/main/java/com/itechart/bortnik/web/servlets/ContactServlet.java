package com.itechart.bortnik.web.servlets;

import com.itechart.bortnik.core.schedule.EmailToAdminJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
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
                    .withSchedule(dailyAtHourAndMinute(16, 33))
                    .forJob(job)
                    .build();
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
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

        String requestUri = request.getRequestURI();
        System.out.println("REQUEST URI---" + requestUri);

        if (requestUri.matches("\\/brt\\/api\\/contacts.*")) {
            System.out.println("MATCH");
            String[] urlArray = request.getRequestURI().split("contacts", 2);
            String urlPart = request.getMethod() + "_";

            if (urlArray.length > 1) {
                urlPart += urlArray[1];
                logger.info("Incoming request: {}", urlPart);
            }
            try {
                controller.processString(urlPart, request, response);
            } catch (ServletException e) {
                logger.error("Servlet exception in parseUrl method from /api request: ", e);
            } catch (IOException e) {
                logger.error("Exception in parseUrl method from /api request: ", e);
            }

        } else {
            System.out.println("NO MATCH");
            RequestDispatcher rd = request.getRequestDispatcher("/index.html");
            try {
                logger.info("Forwarding request with URI {} to index.html.", requestUri);
                rd.forward(request, response);
            } catch (ServletException e) {
                logger.error("Servlet exception in request dispatcher: ", e);
            } catch (IOException e) {
                logger.error("IO exception in request dispatcher: ", e);
            }
        }
    }
}
