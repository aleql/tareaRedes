import java.net.*;
import java.io.*;

/*
public class udpServer_tcpClient implements Runnable {
    private Thread t;
    private String threadName;
    private DatagramSocket socketUDP;

    public udpServer_tcpClient(String name,DatagramSocket socketUDP) {
        threadName = name;
        socketUDP = socketUDP;
        System.out.println("Creating udp server -> tcp client " +  threadName );
    }socketUDP.receive(req);

    public void run() {
        System.out.println("Running udp server -> tcp client  " + threadName);
        Socket socketTCP = null;
        try {
           try {
                socketUDP = new DatagramSocket(2000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            byte[] bufferDatagram = new byte[1400];

            //socketUDP = new DatagramSocket(6789); //otro puerto

            while (true) {
                DatagramPacket req = new DatagramPacket(bufferDatagram, bufferDatagram.length);
                try { //recive datagrama
                    socketUDP.receive(req);
                } catch (IOException e) {
                    System.out.println("IO: " + e.getMessage());
                }

                //obtiene datos del datagrama
                byte[] buffer = req.getData();
                int buffSize = buffer.length;
                InetAddress host = req.getAddress();
                int port = req.getPort();


                String s = "";
                try {
                    s = new String(buffer, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                System.out.println("Se recivio mensaje: " + s);


                //manda respuesta en tcp
                socketTCP = new Socket(host, 2001);
                DataInputStream in = new DataInputStream(socketTCP.getInputStream());
                DataOutputStream out = new DataOutputStream(socketTCP.getOutputStream());
                try {//escribe el buffer
                    out.write(buffer);
                } catch (IOException e1) {
                    System.out.println("no se pudo escribir en el socket tcp " + e1.getMessage());
                    e1.printStackTrace();
                }

                //espera confirmacion
                int data = 0;
                try {
                    data = in.read();
                } catch (IOException e1) {
                    System.out.println("no se recibio confirmacion del paquete " + e1.getMessage());
                    e1.printStackTrace();
                }
                System.out.println("Received " + data);
            }


        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (UnknownHostException e) {
                System.out.println("Sock: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
        System.out.println("IO: " +e.getMessage());}

        finally {
            if (socketTCP != null) {
                try {
                    socketTCP.close();
                } catch (IOException e) {
                    System.out.println("no se pudo cerrar el socket tcp " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (socketUDP != null) {
                socketUDP.close();
            }
        }
        }



    public void start () {
        System.out.println("Starting udp server -> tcp client " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    public static void main(String args[]) {
        udpServer_tcpClient t2 = new udpServer_tcpClient("Thread-2");
        t2.start();
    }
}
*/