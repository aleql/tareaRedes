import java.net.*;
import java.io.*;
public class bwcs {
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



