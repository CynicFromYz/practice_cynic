package com.netty.tomcat;

public abstract class Servlet {


    public abstract void doGet(CynicRequest request, CynicResponse response);

    public abstract void doPost(CynicRequest request, CynicResponse response);
}
