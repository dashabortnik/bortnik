package com.itechart.bortnik.core.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface BaseAction {

    void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

}
