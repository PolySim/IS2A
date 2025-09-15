package lib;

public abstract class ListeTab {
    int[] tab;
    private int last;
    private int first;


    public ListeTab(int length) {
        this.tab = new int[length];
        this.setFirst(0);
        this.setLast(0);
    }

    public void setLast(int last) {
        this.last = last;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return this.last;
    }

    public int getFirst() {
        return this.first;
    }

    public void add(int element) {
        this.tab[this.getLast()] = element;
        this.setLast(this.getLast() + 1);
    }

    public String toString() {
        String out = "";
        for (int i = this.getFirst(); i<this.getLast();i++) {
            out += this.tab[i] + " ";
        }
        return out;
    }

    // To implement
    public abstract int pop();
}
