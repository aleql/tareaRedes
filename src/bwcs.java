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

    public static int sn;

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



