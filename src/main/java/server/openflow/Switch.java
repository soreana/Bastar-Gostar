package server.openflow;

import java.io.*;
import java.net.Socket;

public class Switch {
    private static OutputStream out;
    private static InputStream in;
    private static BufferedReader bin;
    private static char[] buff = new char[2048];

    public Switch(Socket s) throws IOException {
        out = s.getOutputStream();
        in = s.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));
    }

    public void handshake() throws IOException {
        // hello phase
        OpenflowHelper.readHelloRequest(new BufferedReader(new InputStreamReader(in)), buff);
        out.write(OpenflowHelper.helloReply(buff));

        out.write(OpenflowHelper.featureReq(buff));
        int payloadSize = OpenflowHelper.readHeader(bin, buff);
        OpenflowHelper.readPayload(bin,buff,payloadSize);

        System.out.println(OpenflowHelper.parsePayloadAsFeatureRes(buff,payloadSize));
    }
}
