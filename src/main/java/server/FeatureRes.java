package server;

import java.util.Arrays;

public class FeatureRes {
    public final long dpdi;
    public final int n_buffers;
    public final byte n_tables;
    public final byte auxiliary_id;
    public final int capabilities;
    public final int reserved;

    public FeatureRes(long dpdi, int n_buffers, byte n_tables, byte auxiliary_id, int capabilities, int reserved) {
        this.dpdi = dpdi;
        this.n_buffers = n_buffers;
        this.n_tables = n_tables;
        this.auxiliary_id = auxiliary_id;
        this.capabilities = capabilities;
        this.reserved = reserved;
    }

    public FeatureRes(char[] dpdi, char[] n_buffers, char n_tables, char auxiliary_id, char[] capabilities, char[] reserved) {

        this.dpdi = (long) (OpenflowHelper.getInt(Arrays.copyOfRange(dpdi,0,4))*Math.pow(256,4)+
                    OpenflowHelper.getInt(Arrays.copyOfRange(dpdi,4,8)));

        this.n_buffers = OpenflowHelper.getInt(n_buffers);
        this.n_tables = (byte) n_tables;
        this.auxiliary_id = (byte) auxiliary_id;
        this.capabilities = OpenflowHelper.getInt(capabilities);
        this.reserved = OpenflowHelper.getInt(reserved);
    }

    private static String capabilities( int capabilities){
        StringBuilder sb = new StringBuilder();
        if((capabilities & 0x00000001) != 0)
            sb.append("FLOW_STATS | ");
        if ((capabilities & 0x00000002) != 0 )
            sb.append("TABLE_STATS | ");
        if ((capabilities & 0x00000004) != 0 )
            sb.append("PORT_STATS | ");
        if ((capabilities & 0x00000008) != 0 )
            sb.append("GROUP_STATS | ");
        if ((capabilities & 0x00000020) != 0 )
            sb.append("IP_REASM | ");
        if ((capabilities & 0x00000040) != 0 )
            sb.append("QUEUE_STATS | ");
        if ((capabilities & 0x00000100) != 0 )
            sb.append("PORT_BLOCKED | ");

        return sb.substring(0,sb.length()-2);
    }

    @Override
    public String toString() {
        return String.format("dpdi: %d\nn_buffers: %d\nn_tables: %d\nauxiliary_id: %d\ncapabilities: %s\nreserved: %d",
                dpdi,n_buffers,n_tables,auxiliary_id,capabilities(capabilities),reserved);
    }
}
