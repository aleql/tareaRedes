import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;

public class TCPtoUDP implements Runnable{
    private Thread thread1;
    private String threadName;
    private DatagramSocket udp;
    private Socket socketTCP;

    public TCPtoUDP(String name, DatagramSocket udp, Socket socketTCP) {
        threadName = name;
        this.udp = udp;
        this.socketTCP = socketTCP;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = null;
            DataOutputStream out = null;

            //enviar paquete 0 primero
            InetAddress host = InetAddress.getByName("localhost");
            int port = 2000;
            int timeout = 1000;
            byte[] udpStart = new byte[0];
            DatagramPacket reqStartUdp = new DatagramPacket(udpStart, udpStart.length, host, port);
            udp.send(reqStartUdp);


            while (true) {

                in = new DataInputStream(socketTCP.getInputStream());
                byte[] buffer5Bytes = new byte[5];
                in.read(buffer5Bytes);
                String ds = new String(buffer5Bytes, "UTF-8");
                if(!isNumeric(ds)){
                    continue;
                } else {
                    String seqN = bwcs.getNextID();
                    if(ds.equals("00000")) {
                        byte[] header = ("D" + seqN).getBytes();
                        reqStartUdp = new DatagramPacket(header, header.length, host, port);
                        stopAndWait(reqStartUdp, timeout, seqN);
                        break;
                    } else {
                        byte[] buffer = new byte[Integer.parseInt(ds)];
                        in.read(buffer);
                        byte[] header = ("D" + seqN).getBytes();
                        byte[] data = bwcs.concat(header, buffer);
                        DatagramPacket dp = new DatagramPacket(data, data.length, host, port);
                        stopAndWait(dp, timeout, seqN);
                    }
                }
            }
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void start () {
        System.out.println("Starting tcp server -> udp client " +  threadName );
        if (thread1 == null) {
            thread1 = new Thread (this, threadName);
            thread1.start ();
        }
    }

    private static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public void stopAndWait(DatagramPacket dp, int timeout, String seqN) throws IOException {
        this.udp.send(dp);
        long tStart = System.currentTimeMillis();
        while(true){
            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            if(tDelta >= timeout) {
                try {
                    this.udp.send(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tStart = System.currentTimeMillis();
            }
            while(!bwcs.synchronizedStack.isEmpty()) {
                String ackN = bwcs.synchronizedStack.pop();
                if(ackN.equals(seqN)){
                    bwcs.increaseId();
                    return;
                }
            }
        }
    }
}
