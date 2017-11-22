
public class Par<T extends Comparable<T>, R>
        implements Comparable<Par<T, R>> {
    private T head;
    private R tail;

    public Par(T head, R tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public String toString() {
        return "{" + head.toString() + " , " + tail.toString() + "}";
    }


    public T getHead() {
        return head;
    }

    public R getTail() {
        return tail;
    }

    public void setTail(R k) { tail = k; }

    public void setHead(T h) { head = h; }

    @Override
    public int compareTo(Par<T, R> o) {
        int compareHead = o.getHead().compareTo(head);
        if (compareHead != 0) return compareHead;
        return 0;
    }


}