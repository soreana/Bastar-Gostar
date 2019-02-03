package server;

import server.employee.ChiefSecretary;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sinaikashipazha on 12/21/15.
 */
public class Server {
    private static ChiefSecretary chiefSecretary = ChiefSecretary.getInstance();

    private static OutputStream out;
    private static InputStream in;
    private static BufferedReader bin;
    private static char[] buff = new char[2048];

    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
//                chiefSecretary.giveBrieflessSecretaryThisClient(serverSocket.accept());
            serverSocket.accept();
            Socket s = serverSocket.accept();

            out = s.getOutputStream();
            in = s.getInputStream();
            bin = new BufferedReader(new InputStreamReader(in));

            // handle hello
            OpenflowHelper.readHelloRequest(new BufferedReader(new InputStreamReader(in)), buff);
            out.write(OpenflowHelper.helloReply(buff));

            out.write(OpenflowHelper.featureReq(buff));

            OpenflowHelper.readHeader(bin, buff);
            OpenflowHelper.showHeader(buff);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
