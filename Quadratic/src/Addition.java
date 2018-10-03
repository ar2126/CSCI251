/**
 * Class Addition performs the actual addition detailed in the method of finite differences using values inputted from
 * Quadratic.java and stored in Monitor.java
 *
 * @author Aidan Rubenstein
 * @version 12-Feb-2018
 */
public class Addition extends Thread {
    boolean forg;
    Monitor m;
    int i;
    /*
    * Constructor for Addition which assigns all variables defined in Addition
    *
    * @param m an instance of the Monitor class
    * @param i the current iteration out of max
    * @param forg a boolean expression to determine whether or not the addition is being performed on F or G
    */
    public Addition(Monitor m, int i, boolean forg){
        this.m = m;
        this.i = i;
        this.forg = forg;
    }

    /*
    * Puts the result of addition between F and G into Monitor as the new F if the forg variable (checks to see which
    * operation to perform first) is true, otherwise puts the result of G and H into Monitor as the new G
    *
    */
    @Override
    public void run(){

        try{
            if(forg)
                m.putF(i,m.getF(i-1)+m.getG(i-1));
            else
                m.putG(i,m.getG(i-1)+m.getH());
        } catch(InterruptedException e){

        }

    }
}
