package server;

import server.employee.ChiefSecretary;
import server.openflow.OFHeader;
import server.openflow.Switch;

import java.io.*;
import java.net.ServerSocket;

/**
 * Created by sinaikashipazha on 12/21/15.
 */

public class Server {
    private static ChiefSecretary chiefSecretary = ChiefSecretary.getInstance();


    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            // Skip Mininet connection
            serverSocket.accept();

//            while (true)
//                chiefSecretary.giveBrieflessSecretaryThisClient();
            Switch sw = new Switch(serverSocket.accept());
            for (int i = 0; i < 5; i++) {
                OFHeader header = sw.nextHeader();
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
