package com.itechart.bortnik.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

public class GetImageForContactAction implements BaseAction {

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(GetImageForContactAction.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getHeader("fileLink").trim();
        logger.debug("Image by link {} was requested.", path);
        BufferedImage img = null;

        // if image was uploaded, DB will return its link in path
        // if image wasn't uploaded, DB will return "null" as a String or null

        if(path==null || "null".equals(path) || path.isEmpty()){
            try {
                img = ImageIO.read(getClass().getResource("/img/default.jpg"));

                logger.info("Default image was retrieved successfully.");
            } catch (IOException e) {
                logger.error("Failed to read default image by given link: ", e);
            }
        } else {
            try {
                img = ImageIO.read(new File(path));
                logger.info("Requested image was retrieved successfully.");
            } catch (IOException e) {
                img = ImageIO.read(getClass().getResource("/img/default.jpg"));
                logger.error("Failed to read the image by given link: ", e);
            }
        }
        response.setContentType("image/jpeg");
        try (OutputStream out = response.getOutputStream()) {
            ImageIO.write(img, "jpg", out);
            logger.info("Retrieved image was sent to browser");
        } catch (IOException e){
            logger.error("Failed to send image to browser: ", e);
        }
    }
}
