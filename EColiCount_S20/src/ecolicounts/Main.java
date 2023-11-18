package ecolicounts;

import static sbcc.Core.*;

import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * 
 * @author Max Dokukin
 *
 */
public class Main {

	public static void main(String[] args) {

		try {

			String data = readFile("altered.txt"); // changed from "ecoli.txt" for unit test

			long startTime = System.nanoTime();

			int genes[] = new int[4];
			for (int i = 0; i < data.length(); i++) {
				char c = data.charAt(i);
				genes[(c == 'A' ? 0 : c == 'C' ? 1 : c == 'G' ? 2 : 3)]++;
			}

			long finishTime = System.nanoTime();

			println("done in : " + (finishTime - startTime) / 1.0e6 + "ms\r\n" + "#A = " + genes[0] + "\r\n#C = "
					+ genes[1] + "\r\n#G = " + genes[2] + "\r\n#T = " + genes[3]);

		} catch (Exception e) {

			println(e);
		}
	}
}
