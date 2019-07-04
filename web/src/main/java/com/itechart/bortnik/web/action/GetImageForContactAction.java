package com.itechart.bortnik.web.action;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

public class GetImageForContactAction implements BaseAction {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getHeader("fileLink").trim();
        System.out.println("FILELINK---" + path);
        BufferedImage img = null;

        // if image was uploaded, DB will return its link in path
        // if link is invalid, we get an Exception and assign "null" to path,
        // the 2nd check allows to process both when there was no image ("null" in DB) and when the link was bad
        // if image wasn't uploaded, DB will return "null" as a String

        if(!path.equals("null")){
            try {
                img = ImageIO.read(new File(path));
                System.out.println(img.toString());
            } catch (IOException e) {
                System.out.println("Failed to read the image by given link: " + e.getStackTrace());
                path = "null";
            }
        } else if (path.equals("null")){
            try {
                img = ImageIO.read(getClass().getResource("/img/default.jpg"));
                System.out.println(img.toString());
            } catch (IOException e) {
                System.out.println("Failed to read default image by given link: " + e.getStackTrace());
            }
        }
        response.setContentType("image/jpeg");
        try (OutputStream out = response.getOutputStream()) {
            ImageIO.write(img, "jpg", out);
            System.out.println("Image sent to browser");
        }
    }
}
