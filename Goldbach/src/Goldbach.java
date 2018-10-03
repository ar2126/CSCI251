/**
 * Class Goldbach is the main program for the Goldbach Conjecture Problem. The Goldbach Conjecture states that any even
 * number greater than 4 can be evaluated by the sum of odd prime numbers greater than 3. The pj2 library is used to
 * facilitate multithreading in this context
 *
 * @author Aidan Rubenstein
 * @version 26-Feb-2018
 */
import edu.rit.pj2.Task;
import edu.rit.pj2.Loop;
import edu.rit.pj2.vbl.IntVbl;
import java.math.BigInteger;

public class Goldbach extends Task{
    int n = 0;
    IntVbl count;   //holds the total number of solutions
    IntVbl minx;    //holds the minimum x value that leads to a solution
    IntVbl maxx;    //holds the maximum x value that leads to a solution
    public void main(final String[] args) throws Exception{
        if(args.length != 1){
            System.out.println("Error: Argument length must be 2");
            System.exit(0);
        }
        //Attempts to parse the command line input. The program prints an error and exits if args[0] is not an int
        try{
            n = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.out.println("Error: Required arguments not an int");
            System.exit(0);
        }

        //If command line argument n is not even or not greater than 4, an error is printed and the program exits
        if(n <= 4 || n % 2 != 0){
            System.out.println("Error: n must be greater than 4 and even");
            System.exit(0);
        }

        else{
            count = new IntVbl.Sum(0);
            minx = new IntVbl.Min(n-3);
            maxx = new IntVbl.Max(3);

            //Tests every iteration from 3 to the target to see if a set x and y are prime and equal n
            parallelFor(3, n/2) .exec(new Loop()
            {
                IntVbl thrSum;
                IntVbl minX;
                IntVbl maxX;
                public void start(){
                    thrSum = threadLocal(count);
                    minX = threadLocal(minx);
                    maxX = threadLocal(maxx);
                }

                public void run(int i){
                    int y = n-i;
                    //Creates a BigInteger object for evaluating if a number is prime or not
                    BigInteger prim1 = new BigInteger(Integer.toString(i));
                    BigInteger prim2 = new BigInteger(Integer.toString(y));
                    if(prim1.isProbablePrime(64) && prim2.isProbablePrime(64) && i <= y){
                        thrSum.item += 1;   //count incremented
                        minX.reduce(i);
                        maxX.reduce(i);
                    }
                }
            });

            //Prints the solution set depending on the number of solutions that exist for the target n value
            if(count.item > 1) {
                System.out.print(count.item + " solutions\n");
                System.out.print(n + " = " + minx.item + " + " + (n - minx.item) + "\n");
                System.out.print(n + " = " + maxx.item + " + " + (n - maxx.item) + "\n");
            }
            else if(count.item == 1){
                System.out.print(count.item + " solution\n");
                System.out.print(n + " = " + minx.item + " + " + (n - minx.item) + "\n");
            }
            else
                System.out.print("No solutions\n");
        }
    }

}
