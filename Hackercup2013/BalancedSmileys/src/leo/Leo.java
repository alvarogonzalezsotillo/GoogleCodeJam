package leo;
import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Leo {

	public static boolean isBalanced(int openPars, final String text) {
		for (int i = 0; i < text.length(); i++) {
			final char ch = text.charAt(i);
			if (ch == '(') {
				openPars++;
			} else if (ch == ')') {
				openPars--;
				if (openPars < 0) {
					break;
				}
			} else if (ch == ':') {
				if (text.length() - i > 1
						&& (text.charAt(i + 1) == '(' || text.charAt(i + 1) == ')')) {
					if (isBalanced(openPars, text.substring(2 + i)))
						return true;
				}
			} else if (!(ch >= 'a' && ch <= 'z' || ch == ' '))
				return false;
		}
		return openPars == 0;
	}


	final static String FILENAME = "balanced_smileystxt";
	final static String FILENAME_IN = FILENAME + ".txt";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 1; // use 1 to solve them sequentially

	// VM arguments: -ea -Xms4096M -Xmx4096M

	public static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		public Solver() {
			this.testId = -1;
			this.text = "";
		}
		public Solver(final Scanner in, final int testId) {
			this.testId = testId;

			// TODO: Read input

			text = in.nextLine();
		}

		// TODO: Define input variables

		final String text;


		@Override
		public String call() throws Exception {
			final long now = System.nanoTime();

			final String res = isBalanced(0, text) ? "YES" : "NO";

			System.err.println(String.format("%4d ms %s",
					(System.nanoTime() - now) / 1000000, res));
			return res;
		}

		// PROBLEM SOLUTION ENDS HERE
		// -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		final int testId;
	}

	static void debug(final Object... os) {
		System.err.println(deepToString(os));
	};

	public static void main(final String[] args) throws FileNotFoundException,
			InterruptedException {
		final long now = System.nanoTime();
		System.setIn(new FileInputStream(FILENAME_IN));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		final Scanner in = new Scanner(System.in);
		final int numTests = in.nextInt();
		in.nextLine();
		System.err.println(String.format("TESTS: %d", numTests));
		final List<Solver> solvers = new ArrayList<Solver>();
		for (int i = 0; i < numTests; i++) {
			solvers.add(new Solver(in, i + 1));
		}
		final ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		final List<Future<String>> solutions = executor.invokeAll(solvers);
		for (int i = 0; i < numTests; i++) {
			try {
				System.out.println(String.format("Case #%d: %s",
						solvers.get(i).testId, solutions.get(i).get()));
			} catch (final Exception e) {
				System.out.println(String.format("Case #%d: EXCEPTION !!!!!",
						solvers.get(i).testId));
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!",
						solvers.get(i).testId));
				e.printStackTrace(System.err);
			}
		}
		executor.shutdown();
		System.err.println(String.format("TOTAL: %d ms",
				(System.nanoTime() - now) / 1000000));
	}

}