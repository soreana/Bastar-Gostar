package server.openflow;

import java.io.*;
import java.net.Socket;

public class Switch {
    private final OutputStream out;
    private final InputStream in;
    private final BufferedReader bin;
    private final Socket s;
    private final FeatureRes featureRes;

    private static char[] buff = new char[2048];

    public Switch(Socket socket) throws IOException {
        out = socket.getOutputStream();
        in = socket.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));
        s = socket;
        featureRes = handshake();

        // todo replace this with appropriate log
        System.out.println(featureRes);
    }

    private FeatureRes handshake() throws IOException {
        // hello phase
        OpenflowHelper.readHelloRequest(new BufferedReader(new InputStreamReader(in)), buff);
        out.write(OpenflowHelper.helloReply(buff));

        // FeatureReq and FeatureRes
        out.write(OpenflowHelper.featureReq(buff));
        int payloadSize = OpenflowHelper.readHeader(bin, buff);
        OpenflowHelper.readPayload(bin,buff,payloadSize);

        return OpenflowHelper.parsePayloadAsFeatureRes(buff,payloadSize);
    }

    public void echoReq() throws IOException {
    }

    public void echoRes() {
    }
}
