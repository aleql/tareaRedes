import java.net.*;
import java.io.*;
public class bwcs {

    public static SynchronizedStack synchronizedStack = new SynchronizedStack();

    public static String myIntToString5 (int l) {
        String l5 = Integer.toString(l);
        while(l5.length() < 5){
            l5 = "0" + l5;
        }
        return l5;
    }

    public static String getNextId() {
        bwcs.sn++;
        bwcs.sn = bwcs.sn%100000;
        return bwcs.myIntToString5(bwcs.sn);
    }

    public static byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c= new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static int sn = -1;

    public static void main(String args[]) {
        DatagramSocket udp = null;
        ServerSocket ss = null;
        try {
            udp = new DatagramSocket();
            ss = new ServerSocket(2001);
            Socket socketTCP = ss.accept();
            TCPtoUDP tcpUdpThread = new TCPtoUDP("tcpUdpThread", udp, socketTCP);
            UDPtoTCP udpTcpThread = new UDPtoTCP("udpTcpThread", udp, socketTCP);
            tcpUdpThread.start();
            udpTcpThread.start();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (ss != null)
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

    }



