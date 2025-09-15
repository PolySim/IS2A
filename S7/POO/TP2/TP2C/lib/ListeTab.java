package lib;

public abstract class ListeTab {
    private int[] tab;
    private int last;
    private int size;

    public ListeTab() {
        this.setSize(2);
        this.tab = new int[this.getSize()];
        this.setLast(0);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public void setTab(int[] tab) {
        this.tab = tab;
    }

    public int[] getTab() {
        return this.tab;
    }

    public int getSize() {
        return this.size;
    }

    public int getLast() {
        return this.last;
    }

    public void add(int element) {
        if (this.isFull()) {
            this.expands();
        }
        this.tab[this.last % this.tab.length] = element;
        this.last += 1;
    }

    // To implement
    public abstract void expands();
    public abstract boolean isFull();
    public abstract int pop();

}
