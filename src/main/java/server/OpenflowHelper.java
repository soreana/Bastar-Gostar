package server;

import java.io.BufferedReader;
import java.io.IOException;

public interface OpenflowHelper {
    byte []  helloReplyTemplate = {4,0,0,8,0,0,0,0};
    byte []  featureRequestTemplate = {4,5,0,8,0,0,0,0};

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
        return buff[4]*16777216+buff[5]*65536+buff[6]*256+buff[7];
    }

    static void showHeader(char [] buff){
        System.out.printf("Openflow version: %s\nMessage type: %d\nsize: %d\nxid: %s",
                openflowVersion((int)buff[0]),(int) buff[1],packetSize(buff),packetXid(buff));
    }

    static byte[] helloReply(char [] buff){
        for (int i = 4; i < 8; i++) {
            helloReplyTemplate[i] = (byte) buff[i];
        }
        return helloReplyTemplate;
    }

    static byte[] featureReq(char [] buff){
        for (int i = 4; i < 8; i++) {
            featureRequestTemplate[i] = (byte) buff[i];
        }
        featureRequestTemplate[7]++;
        return featureRequestTemplate;
    }

//    static char [] readFeatureRes(char [] buff){
//        System.arraycopy(buff, 4, featureRequestTemplate, 4, 4);
//        featureRequestTemplate[7]++;
//        return featureRequestTemplate;
//    }

    static void readHeader(BufferedReader in, char[] buff) throws IOException {
        in.read(buff,0,8);
    }

    static void readHelloRequest(BufferedReader in, char[] buff) throws IOException {
        in.read(buff,0,8);
    }
}
