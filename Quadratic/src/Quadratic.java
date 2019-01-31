/**
 * Class Quadratic is the main program for the Method of Finite Differences. The Method of Finite Differences
 * evaluates polynomial functions as the result of addition between multiple functions in the form of threads
 *
 * @author Aidan Rubenstein
 * @version 12-Feb-2018
 */

public class Quadratic {
    /*
    * Main method of the Quadratic program
    */
    public static void main(String[] args) {
        int a;
        int b;
        int c;
        int x;
        int input[] = new int[4];

        if (args.length != 4) {
            System.out.println("Error: Insufficient arguments");
            System.exit(0);
        }

        for (int i = 0; i < args.length; i++) {
            try {
                input[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                System.out.println("Error: One or more parameters not an int");
                System.exit(0);
            }
        }
        a = input[0];
        b = input[1];
        c = input[2];
        x = input[3];
        if (x < 0) {
            System.out.println("Error: max value is less than 0");
            System.exit(0);
        }
        Monitor m = new Monitor(x);
        new Thread(new Output(m,x)).start();
        for(int j=1; j <= x; j++){
            new Thread(new Addition(m, j, true)).start();
            new Thread(new Addition(m, j, false)).start();

        }
        new Thread(new Init(a,b,c,x,m)).start();
    }
}
