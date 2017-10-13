import java.net.*;
import java.io.*;
import java.util.Arrays;

public class tcpServer_udpClient implements Runnable {
    private Thread t;
    private String threadName;
    private DatagramSocket socketUDP;

    public tcpServer_udpClient(String name, DatagramSocket socketUDP) {
        threadName = name;
        socketUDP = socketUDP;
        System.out.println("Creating tcp server -> udp client " +  threadName );
    }

    public void run() {
        System.out.println("Running tcp server -> udp client" + threadName);
        int bufferSize = 1405;
        DataInputStream in = null;
        DataOutputStream out = null;
        ServerSocket serverSocket = null;
        try {
            try { // crea socket conexion para que se conecten a mi
                serverSocket = new ServerSocket(2001);
            } catch (IOException e1) {
                System.out.println("No se pudo crear socket en el puerto : " + e1.getMessage());
                e1.printStackTrace();
            }
            while(true) {
                Socket socketTCP = null;
                try { //acepta conexion del socket
                    socketTCP = serverSocket.accept();
                } catch (IOException e1) {
                    System.out.println("No se pudo aceptar la conexion del socket : " + e1.getMessage());
                    e1.printStackTrace();
                }
                try { // recive mensaje
                    in = new DataInputStream(socketTCP.getInputStream());
                } catch (IOException e1) {
                    System.out.println("No se pudo crear input stream : " + e1.getMessage());
                    e1.printStackTrace();
                }
                try {
                    out = new DataOutputStream(socketTCP.getOutputStream());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    System.out.println("No se pudo recibir output stream : " + e1.getMessage());
                }


                //ByteBuffer buffer = ByteBuffer.allocate(bufferSize);;
                //byte[] buffer = new byte[bufferSize];


                //Creamos conexion


                InetAddress host = InetAddress.getByName("localhost");
                int port = 2000;


                //enviar paquete 0 primero
                String startMessage = "00000";
                byte[] udpStart = startMessage.getBytes();
                DatagramPacket reqStartUdp = new DatagramPacket(udpStart, udpStart.length, host, port);
                socketUDP.send(reqStartUdp);

                System.out.println("--/**-*/*-/*-/*/-");

                int c = 0;

                while (true) {
                    c++;
                    byte[] buffer = new byte[bufferSize];
                    int b = in.read(buffer);
                    //b es el tamaño del mensaje
                    //s es el strign que represetna el buffer
                    String s = new String(buffer, "UTF-8");
                    System.out.println("Leyendo" + b + "de :" + c + " paquetes");


                    byte [] last5Bytes = b<5 ? Arrays.copyOfRange(buffer,0, b) : Arrays.copyOfRange(buffer,b-5, b);
                    String eofMessage = new String(last5Bytes, "UTF-8");
                    //System.out.println(eofMessage);
                    if (eofMessage.equals("00000")) {

                        //no envia ultimos 5 bytes
                        DatagramPacket req = new DatagramPacket(Arrays.copyOfRange(buffer,5, b), b - 5, host, port);
                        socketUDP.send(req);

                        System.out.println("Recivido EOF");
                        break;
                    } else {

                        //enviamos el packete por udp a bwss
                        byte[] sendBuffer = Arrays.copyOfRange(buffer,5, b);
                        DatagramPacket req = new DatagramPacket(sendBuffer, sendBuffer.length - 5, host, port);
                        socketUDP.send(req);




                    }






                }



            }
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (serverSocket != null)
                try {
                    serverSocket.close();
                } catch(IOException e) {
                    System.out.println("no se pudo cerrar el socket servidor: " + e.getMessage());
                }
        }

    }



    public void start () {
        System.out.println("Starting tcp server -> udp client " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }


    /*
    public static void main(String args[]) {
        tcpServer_udpClient t1 = new tcpServer_udpClient("Thread-1");
        t1.start();
    }
    */
    }