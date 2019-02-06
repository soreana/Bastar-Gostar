package server.employee;

import server.openflow.OFHeader;
import server.openflow.Switch;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sinaikashipazha on 12/22/15.
 */

public class ChiefSecretary {

    private class Secretary implements Runnable {
        private Thread thread;
        private Switch sw;

        Secretary() {
            thread = new Thread(this);
        }

        void setClient(Switch sw) {
            this.sw = sw;
            thread.start();
        }

        Thread.State getThreadState() {
            return thread.getState();
        }


        @Override
        public void run() {
            OFHeader header;
            try {
                while (true) {
                    header = sw.nextHeader();
                    System.out.println(header);

                    switch (header.ofType) {
                        case HELLO:
                            // todo switch connection lost refresh switch connection
                            break;
                        case ECHO_REQ:
                            sw.readEchoReqPayload(header);
                            sw.sendEchoRes();
                            break;
                        case ECHO_RES:
                            sw.readEchoResPayload(header);
                            break;
                        case PORT_STATUS:
                            sw.readPortStatus(header);
                            break;
                        case NotImplemented:
                            // todo send to controller.
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Secretary> pool;
    private static ChiefSecretary ourInstance = new ChiefSecretary();

    public static ChiefSecretary getInstance() {
        return ourInstance;
    }

    private ChiefSecretary() {
        pool = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            pool.add(new Secretary());
    }

    public void giveBrieflessSecretaryThisClient(Switch sw) {
        for (Secretary secretary : pool)
            if (secretary.getThreadState() == Thread.State.NEW) {
                secretary.setClient(sw);
                return;
            }

        Secretary secretary = new Secretary();
        pool.add(secretary);
        secretary.setClient(sw);
    }
}
