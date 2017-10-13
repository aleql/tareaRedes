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
        try {
            while (true) {
                byte[] bufferDatagram = new byte[1400];
                DatagramPacket req = new DatagramPacket(bufferDatagram, bufferDatagram.length);

                udp.receive(req);

                byte[] data = req.getData();

                String length;
                byte[] udpStart;
                DataOutputStream out = new DataOutputStream(socketTCP.getOutputStream());


                length = myIntToString5(req.getLength());
                udpStart = length.getBytes();



                if (length.equals("00000")) {
                    out.write(udpStart);
                    break;

                } else {


                    //Se concatena el arreglo con el largo junto a los datos y se mandan como un solo paquete
                    byte[] sendData = Arrays.copyOfRange(data,0, req.getLength());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                    outputStream.write(udpStart);
                    outputStream.write(sendData);
                    byte[] finalMessage = outputStream.toByteArray( );
                    out.write(finalMessage);


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

    public static String myIntToString5 (int l) {
        String l5 = Integer.toString(l);
        while(l5.length() < 5){
            l5 = "0" + l5;
        }
        return l5;
    }
}
