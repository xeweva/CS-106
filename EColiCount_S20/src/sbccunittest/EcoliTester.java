package sbccunittest;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static sbcc.Core.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.junit.*;

import ecolicounts.*;

// 1/23/2018

public class EcoliTester {

	public static int totalScore = 0;

	public static InputStream defaultSystemIn;

	public static PrintStream defaultSystemOut;

	public static PrintStream defaultSystemErr;

	public static String newLine = "\r\n"; // System.getProperty("line.separator");


	@BeforeClass
	public static void setUpClass() {
		totalScore = 0;
	}


	@Before
	public void setUp() throws Exception {
		defaultSystemIn = System.in;
		defaultSystemOut = System.out;
		defaultSystemErr = System.err;
	}


	@Test
	public void fileExists() {
		File file = new File("ecoli.txt");
		assertTrue("ecoli.txt file needs to exist", file.exists());
		totalScore += 1;
	}


	@Test
	public void testCount() {

		try {

			String ecoli = readFile("ecoli.txt");

			HashMap<String, Integer> hmap = new HashMap<String, Integer>();

			hmap.put("A", (int) (Math.random() * 5));
			hmap.put("C", (int) (Math.random() * 5));
			hmap.put("G", (int) (Math.random() * 5));
			hmap.put("T", (int) (Math.random() * 5));

			Set<Entry<String, Integer>> set = hmap.entrySet();

			StringBuilder sb = new StringBuilder();
			sb.append("#A = " + (1142136 - hmap.get("A")) + "\n").append("#C = " + (1179433 - hmap.get("C")) + "\n")
					.append("#G = " + (1176775 - hmap.get("G")) + "\n")
					.append("#T = " + (1140877 - hmap.get("T")) + "\n");

			String expectedOutput = sb.toString();
			sb.delete(0, sb.length());

			String part = ecoli.substring(4639171);
			sb.append(part);
			for (Entry<String, Integer> s : set) {
				for (int i = 0; i < s.getValue(); i++) {
					sb.deleteCharAt(sb.indexOf(s.getKey()));
				}
			}

			ecoli = ecoli.substring(0, 4639171) + sb.toString();

			writeFile("altered.txt", ecoli);
			sendToStdinOfTestee("altered.txt\n");
			final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myOut));

			Main.main(null);
			String output = myOut.toString();
			System.setOut(defaultSystemOut);

			// Convert to common end-of-line system.
			output = output.replace("\r\n", "\n");
			expectedOutput = expectedOutput.replace("\r\n", "\n");

			assertEquals(expectedOutput, output);
			totalScore += 9;

		} catch (IOException e) {
			println(e.getMessage());
		}

	}


	public void sendToStdinOfTestee(String message) {
		System.setIn(new ByteArrayInputStream(message.getBytes()));
	}


	@After
	public void tearDown() throws Exception {
		System.setIn(defaultSystemIn);
		System.setOut(defaultSystemOut);
		System.setErr(defaultSystemErr);
	}


	@AfterClass
	public static void afterTesting() {
		println("Estimated score (w/o late penalties, etc.) = " + totalScore);
	}

}
