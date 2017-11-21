import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


public class TCPtoUDP implements Runnable {
    private Thread thread1;
    private String threadName;
    private DatagramSocket udp;
    private Socket socketTCP;
    static int defaultWindowsSize = 10;
    static int windowSize = 10;
    static boolean isFirst = true;
    static ArrayList<Par<String, DatagramPacket>> datagramPacketArray = new ArrayList<>();
    static int timeout = 1000;
    static String seqN = bwcs.getNextID();
    static long tStart = 0;


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
            byte[] udpStart = new byte[0];
            DatagramPacket reqStartUdp = new DatagramPacket(udpStart, udpStart.length, host, port);
            udp.send(reqStartUdp);

            while (true) {
                in = new DataInputStream(socketTCP.getInputStream());
                byte[] buffer5Bytes = new byte[5];
                in.read(buffer5Bytes);
                String ds = new String(buffer5Bytes, "UTF-8");
                if (!isNumeric(ds)) {
                    continue;
                } else {
                    seqN = bwcs.getNextID();
                    if (ds.equals("00000")) {
                        byte[] header = ("D" + seqN).getBytes();
                        reqStartUdp = new DatagramPacket(header, header.length, host, port);
                        goBackN(reqStartUdp);
                        break;
                    } else {
                        byte[] buffer = new byte[Integer.parseInt(ds)];
                        in.read(buffer);
                        byte[] header = ("D" + seqN).getBytes();
                        byte[] data = bwcs.concat(header, buffer);
                        DatagramPacket dp = new DatagramPacket(data, data.length, host, port);
                        goBackN(dp);
                    }
                }
            }
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting tcp server -> udp client " + threadName);
        if (thread1 == null) {
            thread1 = new Thread(this, threadName);
            thread1.start();
        }
    }

    private static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public void goBackN(DatagramPacket dp) throws IOException {
        System.out.println(seqN);
        if (isFirst) {
            tStart = System.currentTimeMillis();
            isFirst = false;
        }
        if (windowSize == 0) {
            waitsend();
            tStart = System.currentTimeMillis();
        }
        this.udp.send(dp);
        datagramPacketArray.add(new Par(seqN, dp));
        --windowSize;
        bwcs.increaseId(1);
        if (new String(dp.getData(), "UTF-8").equals("D00000")) {
            while (windowSize > 0) {
                waitsend();
            }
        }
    }

    private void retransmit() throws IOException {
        isFirst = true;
        windowSize = defaultWindowsSize;
        while (!datagramPacketArray.isEmpty()) {
            this.udp.send(datagramPacketArray.remove(0).getTail());
        }
    }

    private void waitsend() throws IOException {
        while (true) {
            while (!bwcs.synchronizedStack.isEmpty()) {
                String ackN = bwcs.synchronizedStack.pop();
                if (ackN.compareTo(seqN) <= 0) {
                    tStart = System.currentTimeMillis();


                    if (!datagramPacketArray.isEmpty()) {
                        String seqTemp = datagramPacketArray.get(0).getHead();
                        while (ackN.compareTo(seqTemp) > 0) {
                            if (datagramPacketArray.isEmpty()) {
                                break;
                            }

                            datagramPacketArray.remove(0);
                            seqTemp = datagramPacketArray.get(0).getHead();
                            windowSize += 1;
                            //bwcs.increaseId(1);
                        }
                        tStart = System.currentTimeMillis();
                    }
                    if (windowSize > 0) {
                        return;
                    }
                    tStart = System.currentTimeMillis();
                }
            }

            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            if (tDelta >= timeout) {
                tStart = System.currentTimeMillis();
                retransmit();
                //>:()
            }
        }
    }

}
