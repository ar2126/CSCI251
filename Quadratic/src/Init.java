/**
 * Class Init initializes all command line inputs and completes initial evaluation of F[0], G[0], and H
 * after checking for correctness in Quatratic.java
 *
 * @author Aidan Rubenstein
 * @version 12-Feb-2018
 */
public class Init extends Thread{
    int a;
    int b;
    int c;
    int x;
    Monitor m;
    /*
    * Constructor for Init which assigns all variables defined in Init
    *
    * @param a the value for a in the polynomial expression
    * @param b the value for b in the polynomial expression
    * @param c the value for c in the polynomial expression
    * @param x the max value that is to be calculated in the polynomial expression
    * @param m an instance of the Monitor.java class
    */
    public Init(int a, int b, int c, int x, Monitor m){
        this.a = a;
        this.b = b;
        this.c = c;
        this.x = x;
        this.m = m;
    }

    /*
    * Runs the first evaluation of F, G, and H when x == 0
    *
    */
    @Override
    public void run(){
        m.putF(0, c);
        m.putG(0, a + b);
        m.putH(2 * a);
    }

}
