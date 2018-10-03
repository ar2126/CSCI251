/**
 * Class Output gathers the result of addition between H, G, and F and displays the proper print result of each step
 * from a given instance to max
 *
 * @author Aidan Rubenstein
 * @version 12-Feb-2018
 */
public class Output extends Thread {
    int H;
    int G;
    int F;
    int x;
    Monitor m;
    /*
    * Constructor for Output which assigns all variables defined in Output
    *
    * @param m an instance of class Monitor
    * @param x the max value specified in the program arguments
    */
    public Output(Monitor m, int x){
        this.x = x;
        this.m = m;
    }

    /*
   * Gathers the result of each function at an instance i and displays the result as a print statement
   *
   */
    @Override
    public void run(){
        try{
            for(int i = 0; i <= x; i++) {
                H = m.getH();
                G = m.getG(i);
                F = m.getF(i);
                System.out.print(i + "\t" + H + "\t" + G + "\t" + F + "\n");
            }
        } catch (InterruptedException e){

        }
    }
}
