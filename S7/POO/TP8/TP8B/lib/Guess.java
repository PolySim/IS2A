package lib;

import java.util.*;
import exceptions.*;

public class Guess {
    static Random ran = new Random();
    private int good;
    private int boundMin;
    private int boundMax;
    private int nbGuess;

    public Guess(int boundMin, int boundMax, int nbGuess) {
        this.boundMin = boundMin;
        this.boundMax = boundMax;
        this.nbGuess = nbGuess;
        this.good = ran.nextInt(this.boundMax - this.boundMin -1 ) + this.boundMin + 1;
    }

    public int getBoundMin() {
        return this.boundMin;
    }

    public int getBoundMax() {
        return this.boundMax;
    }

    public int getNbGuess() {
        return this.nbGuess;
    }

    public void test(int n) throws WinException, LoseException {
        if (this.nbGuess <= 0) {
            throw new LoseException();
        }
        if (n == this.good) {
            throw new WinException();
        } else {
            if (n < this.good && this.boundMin < n) {
                this.boundMin = n;
            }
            if (n > this.good && this.boundMax > n) {
                this.boundMax = n;
            }
            this.nbGuess -= 1;
            if (this.nbGuess <= 0) {
                throw new LoseException();
            }
        }
    }

    public String toString() {
        return this.boundMin + " < ?? < " + this.boundMax;
    }

    public String completeString() {
        return this.boundMin + " < "+this.good+" < " + this.boundMax;
    }

    public int getSolution() {
        return this.good;
    }




}
