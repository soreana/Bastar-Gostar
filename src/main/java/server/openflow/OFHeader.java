package server.openflow;

public class OFHeader {
    public final int version;
    public final OFType ofType;
    public final int size ;
    public final long xid;

    public OFHeader(int version,OFType ofType, int size, long xid){
        this.version = version;
        this.ofType = ofType;
        this.size = size;
        this.xid = xid;
    }

    public int payloadSize() {
        return size -8;
    }

    @Override
    public String toString() {
        return String.format("Openflow version: %s\nMessage type: %s\nsize: %d\nxid: %d\n",
                OpenflowHelper.openflowVersion(version),ofType,size,xid);
    }
}
