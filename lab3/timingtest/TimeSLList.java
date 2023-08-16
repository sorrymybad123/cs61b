package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        SLList test = new SLList<>();
        int M = 10000;
        int N = 1000;
        AList N_list = new AList<Integer>();
        AList times = new AList<Double>();
        AList M_list = new AList<Integer>();
        while (N <= 128000) {
            N_list.addLast(N);
            M_list.addLast(M);
            for (int i = 0; i < N; i += 1) {
                test.addLast(1);
            }
            N *= 2;
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < M; i += 1){
                test.getLast();
            }
            double timeinseconds = sw.elapsedTime();
            times.addLast(timeinseconds);
        }
        printTimingTable(N_list, times, M_list);


    }

}
