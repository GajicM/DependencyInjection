package server;

import framework.DIENGINE3;
import framework.DIEngine;
import framework.DiEngine2;
import framework.RouteLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import static framework.DIEngine.doDependencyInjection;
import static framework.DIEngine.figureOutAutowired;

public class Server {

    public static final int TCP_PORT = 8080;

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

      //  DIEngine.instantiateSingletons();
      //  RouteLoader.loadRoutes();
 //       System.out.println(DIEngine.instantietedSingletons);
   //     System.out.println(RouteLoader.mappedRoutes);
     //   figureOutAutowired();
      //  doDependencyInjection();

     //   DiEngine2.DependencyInjectionEngine();




       // DIENGINE3.doAll();
       // RouteLoader.loadRoutes();
      //  startServer();


    }

    public static void startServer(String path){
        DIENGINE3.doAll();
        RouteLoader.loadRoutes();
        try {
            ServerSocket serverSocket = new ServerSocket(TCP_PORT);
            System.out.println("Server is running at http://localhost:"+TCP_PORT);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new Thread(new ServerThread(socket,RouteLoader.mappedRoutes)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
