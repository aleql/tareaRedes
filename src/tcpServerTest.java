import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;


/*

public class tcpServerTest implements Runnable {
    private Thread t;
    private String threadName;

    tcpServerTest( String name) {
        threadName = name;
        System.out.println("Creating tcp server test  " +  threadName );
    }


    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public void run() {
        System.out.println("Running tcp server test" + threadName);
        int bufferSize = 1400;
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



                while (true) {
                    byte[] buffer = new byte[bufferSize];
                    int b = in.read(buffer);
                    String s = new String(buffer, "UTF-8");
                    System.out.println("Leyendo" + b);
                    //System.out.println("string " + s);
                    //int b = in.read();
                    //System.out.println("Tamaño buffer : " + b);
                    if (s.contains("00000") && b<bufferSize) {
                        System.out.println("Buffer llenado:");
                        break;
                    }
                    //buffer.put( (byte) b);
                }
                while (true) {
                    String b = in.readLine();
                    System.out.println("Leyendo" + b);
                    //int b = in.read();
                    //System.out.println("Leyendo : " + b);
                    if (b.endsWith("00000")) {
                        System.out.println("Buffer llenado:");
                        break;
                    }
                    //buffer.put( (byte) b);


                //datos recividos
                //byte[] buffer = ByteBuffer.allocate(4).putInt(in.read()).array();




                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                System.out.println();

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

    public static void main(String args[]) {
        tcpServerTest t1 = new tcpServerTest("Thread-1");
        t1.start();
    }
}*/