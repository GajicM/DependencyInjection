package framework.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import server.ServerThread;

@Aspect
public class AspectC {



 /*   @Pointcut("set(public * *.* ) ")
    void verbose() {}

    @Before("verbose()  && args(myObject)")
    public void afterVerbose(Object myObject) {
        System.out.println("-> Aspekt pozvan posle verbose");
        System.out.println(myObject);
    }
*/










    @Pointcut("call( * *..Server.startServer() )")
    void startServer(){

    }

    @Before("startServer()")
    public void beforeStartServer() throws Throwable {
        //
    //    System.out.println("-> Aspekt pre sabiranja");
       // Object returnValue = jp.proceed();

      //  System.out.println("sabrao");


    }

    @Pointcut("execution( testapp.controller.*.new(..)  )")
    void startServerSocket(){

    }
    @After(value = "startServerSocket() && this(myObject)", argNames = "myObject")
    public Object aroundStartServerSocket(Object myObject) throws Throwable {
     //   System.out.println("ovo se zapravo desava");

        System.err.println("aspectC    "+myObject + "aspectC");
       // System.out.println("aloo ovo se zapravo desava");


        return null;
    }


    @Pointcut("call(public void ServerThread.run())")
    void oduzimanje(){

    }

    @After("oduzimanje()")
    public void posleOduzimanja() {
        System.out.println("<- Aspekt pozvan posle oduzimanja");
    }

}
