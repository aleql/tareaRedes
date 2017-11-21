import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.*;
import java.util.Arrays;


public class UDPtoTCP implements Runnable{
    private Thread thread1;
    private String threadName;
    private DatagramSocket udp;
    private Socket socketTCP ;

    public UDPtoTCP(String name, DatagramSocket udp, Socket socketTCP ) {
        threadName = name;
        this.udp = udp;
        this.socketTCP = socketTCP;
    }

    @Override
    public void run() {
        int expectedSeqN = -1;
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(socketTCP.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                byte[] bufferDatagram = new byte[1406];
                DatagramPacket req = new DatagramPacket(bufferDatagram, bufferDatagram.length);
                udp.receive(req);
                String msgType = new String(req.getData(), "UTF-8").substring(0,1);

                if (msgType.equals("A")) {
                    bwcs.synchronizedStack.push(req);
                    continue;
                }

                int length;
                length = req.getLength();
                String seqN = new String(req.getData(), "UTF-8").substring(1,6);

                if(expectedSeqN == -1){
                    expectedSeqN = Integer.parseInt(seqN);
                }

                //mandar ACK
                InetAddress host = InetAddress.getByName("localhost");
                int port = 2000;
                byte[] header = ("A" + seqN).getBytes();

                if (!(expectedSeqN == Integer.parseInt(seqN)) ){
                    header = ("A" + expectedSeqN).getBytes();
                }
                DatagramPacket dp = new DatagramPacket(header, header.length, host, port);

                if (expectedSeqN == Integer.parseInt(seqN)) {
                    udp.send(dp);
                    //Se concatena el arreglo con el largo junto a los datos y se mandan como un solo paquete
                    byte[] datagramData = Arrays.copyOfRange(req.getData(),6, req.getLength());
                    byte[] lengtHeader = bwcs.myIntToString5(length-6).getBytes();
                    byte[] sendData = bwcs.concat(lengtHeader,datagramData);

                    out.write(sendData);

                    expectedSeqN++;
                    System.out.println(length);
                    if (length == 6) {
                        break;
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
        System.out.println("Starting udp server -> tcp client " +  threadName );
        if (thread1 == null) {
            thread1 = new Thread (this, threadName);
            thread1.start ();
        }
    }
}
