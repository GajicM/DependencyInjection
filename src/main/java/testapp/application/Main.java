package testapp.application;

import framework.ClassLoader;
import server.Server;

public class Main {
    public static void main(String[] args) {
        ClassLoader.packageName="testapp";
        Server.startServer("testapp");
    }
}
