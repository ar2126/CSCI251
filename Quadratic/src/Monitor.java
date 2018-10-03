/**
 * Class Monitor contains the monitoring functions which store values and notify threads once called
 *
 * @author Aidan Rubenstein
 * @version 12-Feb-2018
 */
public class Monitor {
    private Integer[] F;
    private Integer[] G;
    private int max;
    private Integer H;
    /*
    * Constructor for Monitor which stores the value of F and G at a given indices
    */
    public Monitor(int max){
        F = new Integer[max+1];
        G = new Integer[max+1];
        this.max = max;
    }

    /*
    * Puts an evaluated sum into F and notifies all Threads
    *
    * @param x the current iteration of max
    * @param value the value to be stored in array F
    */
    public synchronized void putF (int x, int value){
        if(x < 0 || x > max){
            System.out.println("Error: x is not between 0 and max");
            System.exit(0);
        }
        else{
            if(F[x] != null) {
                System.out.println("Error: value of F has already been put");
                System.exit(0);
            }
            else{
                F[x] = value;
                notifyAll();
            }
        }

    }

    /*
    * Puts an evaluated sum into G and notifies all Threads
    *
    * @param x the current iteration of max
    * @param value the value to be stored in array G
    */
    public synchronized void putG (int x, int value){
        if(x < 0 || x > max){
            System.out.println("Error: x is not between 0 and max");
            System.exit(0);
        }
        else{
            if(G[x] != null) {
                System.out.println("Error: value of G has already been put");
                System.exit(0);
            }
            else{
                G[x] = value;
                notifyAll();
            }
        }
    }

    /*
    * Puts the current value of H into H[] and notifies all Threads
    *
    * @param value the value to be stored in array F
    */
    public synchronized void putH (int value){
        if(H != null) {
            System.out.println("Error: value of H has already been put");
            System.exit(0);
        }
        else{
            H = value;
            notifyAll();
        }
    }

    /*
    * Gives the evaluation of F at the specific instance out of the max. Will wait until there is a tangible value in
    * the array
    *
    * @param x the current iteration of max
    * @returns F[x] the value of F at index x
    * @exception InterruptedException thrown if an InterruptedException occurs
    */
    public synchronized int getF (int x) throws InterruptedException{
        if(x < 0 || x > max){
            System.out.println("Error: x is not between 0 and max");
            System.exit(0);
            return 0;
        }
        else {
            while (F[x] == null)
                wait();
            return F[x];
        }
    }
    /*
    * Gives the evaluation of G at the specific instance out of the max. Will wait until there is a tangible value in
    * the array
    *
    * @param x the current iteration of max
    * @returns G[x] the value of G at index x
    * @exception InterruptedException thrown if an InterruptedException occurs
    */
    public synchronized int getG (int x)throws InterruptedException{
        if(x < 0 || x > max){
            System.out.println("Error: x is not between 0 and max");
            System.exit(0);
            return 0;
        }
        else {
            while (G[x] == null)
                wait();
            return G[x];
        }
    }
    /*
    * Gives the evaluation of H
    *
    * @returns H the value of H
    * @exception InterruptedException thrown if an InterruptedException occurs
    */
    public synchronized int getH () throws InterruptedException{
        while(H == null)
            wait();
        return H;
    }

}
