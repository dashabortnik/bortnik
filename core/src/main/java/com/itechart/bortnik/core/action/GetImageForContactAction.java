package com.itechart.bortnik.core.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

public class GetImageForContactAction implements BaseAction {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getHeader("fileLink");
        System.out.println("FILELINK: " + path);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
            System.out.println(img.toString());
        } catch (IOException e) {
            System.out.println("Failed to read the image by given link: " + e.getStackTrace());
        }
        response.setContentType("image/jpeg");
        try (OutputStream out = response.getOutputStream()) {
            ImageIO.write(img, "jpg", out);
            System.out.println("DONE");
        }
    }
}
