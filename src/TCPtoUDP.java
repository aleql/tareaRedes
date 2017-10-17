import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.stream.Stream;

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
            int timeout = 5000;
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
                }

                else if(ds.equals("00000")) {
                    reqStartUdp = new DatagramPacket(udpStart, udpStart.length, host, port);
                    udp.send(reqStartUdp);
                    break;
                } else {
                    String seqN = getNextId();
                    byte[] header = ("D" + seqN).getBytes();
                    byte[] buffer = new byte[Integer.parseInt(ds)];
                    in.read(buffer);
                    byte[] data = concat(header, buffer);
                    DatagramPacket dp = new DatagramPacket(data, data.length, host, port);
                    udp.send(dp);
                    long tStart = System.currentTimeMillis();

                    busyWaiting:
                    while(true){
                        long tEnd = System.currentTimeMillis();
                        long tDelta = tEnd - tStart;
                        if(tDelta >= timeout) {
                            udp.send(dp);
                            tStart = System.currentTimeMillis();
                        }
                        while(!bwcs.synchronizedStack.isEmpty()) {
                            String ackN = bwcs.synchronizedStack.pop();
                            if(ackN.equals(seqN)){
                                break busyWaiting;
                            }
                        }
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

    private String getNextId() {
        bwcs.sn++;
        bwcs.sn = bwcs.sn%100000;
        return bwcs.myIntToString5(bwcs.sn);
    }

    private byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c= new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}
