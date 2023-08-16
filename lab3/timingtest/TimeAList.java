package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList N_test = new AList<Integer>();
        AList time = new AList<Double>();
        int N = 1000;
        AList test = new AList<Integer>();
        while(N <= 128000) {
            N_test.addLast(N);
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < N; i += 1) {
                test.addLast(1);
            }
            N *= 2;
            double timeinseconds = sw.elapsedTime();
            time.addLast(timeinseconds);
        }
        printTimingTable(N_test, time, N_test);
    }
}
