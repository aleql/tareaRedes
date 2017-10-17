import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.Stack;

public class SynchronizedStack {

    Stack<String> datagramPacketStack = new Stack<>();

    public synchronized void push(DatagramPacket dp) {
        String ackN = "";
        try {
            ackN = new String(dp.getData(), "UTF-8").substring(1,6);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.datagramPacketStack.push(ackN);
    }

    public synchronized String pop() {
        return this.datagramPacketStack.pop();
    }

    public synchronized boolean isEmpty() {
        return datagramPacketStack.isEmpty();
    }

}
