package server.openflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public interface OpenflowHelper {
    byte [] helloReplyTemplate = {4,0,0,8,0,0,0,0};
    byte [] featureRequestTemplate = {4,5,0,8,0,0,0,0};
    byte [] echoResTemplate = {4,3,0,8,0,0,0,0};

    static String openflowVersion(int i){
        if(i==5)
            return "1.4";
        else if(i==4)
            return "1.3";
        else if(i==3)
            return "1.2";
        else if(i==2)
            return "1.1";
        else if(i==1)
            return "1.0";
        else
            return "Undefined";
    }

    static int packetSize(char [] buff){
        return ((int) buff[2])*256 +(int) buff[3];
    }

    static int packetXid(char [] buff){
        return getInt(Arrays.copyOfRange(buff,4,8));
    }

    static int getInt(char[] buff){
        return buff[0]*16777216+buff[1]*65536+buff[2]*256+buff[3];
    }

    static void showHeader(char [] buff){
        System.out.printf("Openflow version: %s\nMessage type: %d\nsize: %d\nxid: %s\n",
                openflowVersion((int)buff[0]),(int) buff[1],packetSize(buff),packetXid(buff));
    }

    static void showHeader(byte[] buff) {
        showHeader(new String(buff).toCharArray());
    }

    static byte[] helloReply(char [] buff){
        return buildPacketFromTemplate(buff, helloReplyTemplate);
    }

    static byte[] buildPacketFromTemplate(char[] buff, byte[] template) {
        for (int i = 4; i < 8; i++) {
            template[i] = (byte) buff[i];
        }
        template[7] ++;
        buff[7] ++;
        return template;
    }

    static byte[] featureReq(char [] buff){
        return buildPacketFromTemplate(buff, featureRequestTemplate);
    }

    static byte[] echoRes(char [] buff){
        return buildPacketFromTemplate(buff, echoResTemplate);
    }

    static void readPayload(BufferedReader bin , char [] buff,int len) throws IOException {
        bin.read(buff,0,len);
    }

    static int readHeader(BufferedReader bin, char[] buff) throws IOException {
        bin.read(buff,0,8);
        return packetSize(buff)-8;
    }

    static void readHelloRequest(BufferedReader bin, char[] buff) throws IOException {
        bin.read(buff,0,8);
    }

    static FeatureRes parsePayloadAsFeatureRes(char[] buff, int payloadSize) {
        return new FeatureRes(
                Arrays.copyOfRange(buff,0,8),
                Arrays.copyOfRange(buff,8,12),
                buff[12],buff[13],
                Arrays.copyOfRange(buff,16,20),
                Arrays.copyOfRange(buff,20,24)
        );
    }
}
