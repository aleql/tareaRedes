import java.net.*;
import java.io.*;
import java.util.Arrays;
/*
public class tcpServer implements Runnable {
    private Thread t;
    private String threadName;

    tcpServer( String name) {
        threadName = name;
        System.out.println("Creating tcp server" +  threadName );
    }

    public void run() {
        System.out.println("Running tcp server " + threadName);
        int bufferSize = 1405;
        DataInputStream in = null;
        DataOutputStream out = null;
        DatagramSocket socketUDP = null;
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


                //Creamos conexion con bwss
                //socketUDP = new DatagramSocket();
                //InetAddress host = InetAddress.getLocalHost();
                //int port = 2002;

                while (true) {
                    byte[] buffer = new byte[bufferSize];
                    int b = in.read(buffer);
                    //b es el tamaño del mensaje
                    //s es el strign que represetna el buffer
                    String s = new String(buffer, "UTF-8");
                    System.out.println("Leyendo" + b);

                    //enviamos el packete por udp a bwss
                   // DatagramPacket req = new DatagramPacket(buffer, buffer.length, host, port);
                   // socketUDP.send(req);

                    //espera respuesta??
                    byte[] n = new byte[1000];
                    DatagramPacket rep = new DatagramPacket(n, n.length);
                    socketUDP.receive(rep);

                    //Si el buffer no se lleno y contiene el mensaje 00000, termino


                    //Se lee si entre los ultimos 5 bytes estaba el mensaje de termino

                    byte [] first5Bytes = Arrays.copyOfRange(buffer,0, 5);
                    byte [] last5Bytes = b<5 ? Arrays.copyOfRange(buffer,0, b) : Arrays.copyOfRange(buffer,b-5, b);
                    String eofMessage = new String(last5Bytes, "UTF-8");
                    String sizeOfMessage = new String(first5Bytes, "UTF-8");
                    System.out.println("First 5 bytes" + sizeOfMessage);
                    //System.out.println(eofMessage);
                    System.out.println(s);
                    System.out.println("-------------------------------------------------------------------------");
                    if (eofMessage.equals("00000")) {
                        System.out.println("Recivido EOF");
                        break;
                    }
                }

                //creo datagrama con datos






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
        System.out.println("Starting tcp server " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }


    public static void main(String args[]) {
        tcpServer t1 = new tcpServer("Thread-tcp bwcs");
        t1.start();
    }

}*/