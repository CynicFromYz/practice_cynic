package com.netty.tomcat;

public class MyServlet extends Servlet {
    @Override
    public void doGet(CynicRequest request, CynicResponse response) {
        response.write(request.getParameter("name"));
    }

    @Override
    public void doPost(CynicRequest request, CynicResponse response) {
        doGet(request, response);
    }
}
