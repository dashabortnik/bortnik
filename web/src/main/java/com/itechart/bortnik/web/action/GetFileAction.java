package com.itechart.bortnik.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetFileAction implements BaseAction{

    public GetFileAction(){}

    //create Logger for current class
    Logger logger = LoggerFactory.getLogger(GetFileAction.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/octet-stream");
        String path = request.getHeader("fileLink").trim();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        logger.debug("File by link {} was requested.", decodedPath);

        if (!path.equals("")){
            try (OutputStream out = response.getOutputStream()) {
                byte[] content = Files.readAllBytes(Paths.get(decodedPath));
                out.write(content);
                logger.info("Requested file was sent to browser.");
            } catch (IOException e){
                logger.error("Error with reading or writing the requested file: ", e);
            }
        }
    }
}
