package server.employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            String input ;
            try {
                while( (input = in.readLine() ) != null ){
                    System.out.println( input );
                    out.println(input);
                }
            } catch (IOException e) {
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
