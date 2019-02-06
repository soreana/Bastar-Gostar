package server.openflow;

import java.util.HashMap;
import java.util.Map;

public enum OFType {
    HELLO(0),
    PORT_STATUS(12),
    ECHO_REQ(2),
    ECHO_RES(3),
    NotImplemented(-1);

    private final int value;
    private static Map<Integer,OFType> values = new HashMap<>();

    static {
        for(OFType of : OFType.values())
            values.put(of.value,of);
    }

    OFType(int value) {
        this.value = value;
    }

    public static OFType valueOf(int i){
        OFType tmp = values.get(i);

        if(tmp == null)
            return NotImplemented;
        else
            return tmp;
    }

}
