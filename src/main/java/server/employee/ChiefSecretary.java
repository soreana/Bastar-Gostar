package server.employee;

import server.openflow.OFHeader;
import server.openflow.SwitchPipe;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sinaikashipazha on 12/22/15.
 */

public class ChiefSecretary {

    private class Secretary implements Runnable {
        private Thread thread;
        private SwitchPipe swp;

        Secretary() {
            thread = new Thread(this);
        }

        void setClient(SwitchPipe swp) {
            this.swp = swp;
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
                    header = swp.nextHeader();
                    System.out.println(header);

                    switch (header.ofType) {
                        case HELLO:
                            // todo switch connection lost refresh switch connection
                            break;
                        case ECHO_REQ:
                            swp.readEchoReqPayload(header);
                            swp.sendEchoRes();
                            break;
                        case ECHO_RES:
                            swp.readEchoResPayload(header);
                            break;
                        case PORT_STATUS:
                            swp.readPortStatus(header);
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

    public void giveBrieflessSecretaryThisClient(SwitchPipe swp) {
        for (Secretary secretary : pool)
            if (secretary.getThreadState() == Thread.State.NEW) {
                secretary.setClient(swp);
                return;
            }

        Secretary secretary = new Secretary();
        pool.add(secretary);
        secretary.setClient(swp);
    }
}
