package sbccunittest;

import static java.lang.System.*;
import static org.junit.Assert.*;
import static sbcc.Core.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import org.junit.*;

import flights.*;

// Updated 02/06/2020

public class FlightsTester {

	public static int totalScore = 0;

	public static InputStream defaultSystemIn;
	public static PrintStream defaultSystemOut;
	public static PrintStream defaultSystemErr;

	public String separator = System.getProperty("line.separator");

	private PrintStream originalOut = System.out;

	private OutputStream os = new ByteArrayOutputStream();

	@BeforeClass // Runs Before any test is done
	public static void beforeTesting() throws IOException {
		totalScore = 0;
		writeFileAsLines("flights.txt", Arrays.asList(file));
	}


	@AfterClass
	public static void afterTesting() {
		println("Estimated score (w/o late penalties, etc.) = " + totalScore);
	}


	@Before
	public void setUp() throws Exception {
		PrintStream ps = new PrintStream(os);
		System.setOut(ps);
		defaultSystemIn = System.in;
		defaultSystemOut = System.out;
		defaultSystemErr = System.err;
	}


	@After
	public void tearDown() throws Exception {
		System.setIn(defaultSystemIn);
		System.setOut(originalOut);

	}


	@Test
	public void testUsesTemplateProjectCorrectly() throws IOException {
		Stream<Path> count = Files.find(Paths.get("lib"), Integer.MAX_VALUE,
				(path, basicFileAttributes) -> path.toFile().getName().matches("sbcccore.*.*.*.jar"));

		String src = readFile(Paths.get("src", "flights", "Main.java").toString());
		if (!src.contains("import static sbcc.Core.*;"))
			fail("static import of sbcc.Core not found.  To add sbcc.Core functionality to your project, see https://github.com/ProfessorStrenn/workspace-cs106-v3/blob/master/README.md#static-import-of-core-functionality.");
		if (src.contains("System.out.println"))
			fail("System.out.println()'s need to be replaced with just println().");

		int wordCount = 0;
		Pattern p = Pattern.compile("loadFlights");
		Matcher m = p.matcher(src);
		while (m.find()) {
			wordCount++;
		}
		if (wordCount <= 1)
			fail("You Class has to use loadFlights in it's main and have a separate function called ");

		totalScore += 1;

		count.close();
	}


	@Test
	public void testUserProgram() throws IOException {

		sendToStdinOfTestee("flights.txt");

		Main.main(null);
		var expectedLines = expected.split("\\n");
		var actualLines = os.toString().split("\\n");
		for (int i = 0; i < expectedLines.length; i++)
			assertEquals(expectedLines[i].trim(), actualLines[i].trim());
		totalScore += 5;

	}


	public void sendToStdinOfTestee(String message) {
		System.setIn(new ByteArrayInputStream(message.getBytes()));
	}

	private final static String[] file = { "SBA => DEN, United, $393", "SBA => DEN, American, $589",
			"LAX => DEN, United, $67", "LAX => DEN, American, $73", "BUR => SFO, American, $360",
			"BUR => SFO, United, $110", "LAX => DEN, Frontier, $75", "BUR => SFO, United, $155" };

	private final static String expected = "AIRLINE         FROM    TO      PRICE" + lineSeparator()
			+ "-------         ----    --      -----" + lineSeparator() + "United          SBA     DEN     $393"
			+ lineSeparator() + "American        SBA     DEN     $589" + lineSeparator()
			+ "United          LAX     DEN     $67" + lineSeparator() + "American        LAX     DEN     $73"
			+ lineSeparator() + "American        BUR     SFO     $360" + lineSeparator()
			+ "United          BUR     SFO     $110" + lineSeparator() + "Frontier        LAX     DEN     $75"
			+ lineSeparator() + "United          BUR     SFO     $155";

}