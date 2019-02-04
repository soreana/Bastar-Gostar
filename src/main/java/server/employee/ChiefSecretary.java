package server.employee;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by sinaikashipazha on 12/22/15.
 */

public class ChiefSecretary {

    private class Secretary implements Runnable {
        private Thread thread;
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private char [] buff = new char[2048];

        public Secretary(){
            thread = new Thread( this );
        }

        public void setClient(Socket client){
            this.client = client;
            try {
                out = new PrintWriter( client.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader( client.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            thread.start();
        }

        public Thread.State getThreadState(){
            return thread.getState();
        }


        @Override
        public void run() {

            try {
                // read Hello
                System.out.println("************ start ****************");


//                OpenflowHelper.showHeader(OpenflowHelper.featureReq(buff));

//                out.write(OpenflowHelper.featureReq(buff));

//                OpenflowHelper.readFeatureRes(buff);

//                OpenflowHelper.showHeader(buff);

                System.out.println("\n************ end ****************");

                Thread.sleep(1000);

            } catch ( InterruptedException e) {
                e.printStackTrace();
            }

            // remove client
            client = null;
            in  = null;
            out = null;
        }
    }

    private ArrayList<Secretary> pool ;
    private static ChiefSecretary ourInstance = new ChiefSecretary();

    public static ChiefSecretary getInstance() {
        return ourInstance;
    }

    private ChiefSecretary() {
        pool = new ArrayList<>();
        for (int i=0 ; i<10 ; i++)
            pool.add(new Secretary());
    }

    public void giveBrieflessSecretaryThisClient( Socket client ){
        for(Secretary secretary : pool)
            if( secretary.getThreadState() == Thread.State.NEW){
                secretary.setClient(client);
                return;
            }

        Secretary secretary = new Secretary();
        pool.add(secretary);
        secretary.setClient(client);
    }
}
