package server;

import server.employee.ChiefSecretary;
import server.openflow.SwitchPipe;

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
            chiefSecretary.giveBrieflessSecretaryThisClient(new SwitchPipe(serverSocket.accept()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
