package server.openflow;

import java.io.*;
import java.net.Socket;

public class SwitchPipe {
    private final OutputStream out;
    private final InputStream in;
    private final BufferedReader bin;
    private final Socket s;
    private final FeatureRes featureRes;

    private static char[] buff = new char[2048];

    public SwitchPipe(Socket socket) throws IOException {
        out = socket.getOutputStream();
        in = socket.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));
        s = socket;
        featureRes = handshake();
    }

    private FeatureRes handshake() throws IOException {
        // hello phase
        OpenflowHelper.readHelloRequest(new BufferedReader(new InputStreamReader(in)), buff);
        out.write(OpenflowHelper.helloReply(buff));

        // FeatureReq and FeatureRes
        out.write(OpenflowHelper.featureReq(buff));
        int payloadSize = OpenflowHelper.readHeader(bin, buff);
        OpenflowHelper.readPayload(bin, buff, payloadSize);

        return OpenflowHelper.parsePayloadAsFeatureRes(buff, payloadSize);
    }

    public void readPortStatus(OFHeader header) throws IOException {
        OpenflowHelper.readPayload(bin, buff, header.payloadSize());
        // todo show payload
    }

    public void readEchoReqPayload(OFHeader header) throws IOException {
        readEchoResPayload(header);
    }

    public OFHeader nextHeader() throws IOException {
        OpenflowHelper.readHeader(bin, buff);
        return new OFHeader(
                buff[0],
                OFType.valueOf(buff[1]),
                OpenflowHelper.packetSize(buff),
                OpenflowHelper.packetXid(buff)
        );
    }

    public void readEchoResPayload(OFHeader header) throws IOException {
        if (header.payloadSize() == 0)
            return;
        OpenflowHelper.readPayload(bin, buff, header.payloadSize());
    }

    public void sendEchoRes() throws IOException {
        out.write(OpenflowHelper.echoRes(buff));
    }
}
