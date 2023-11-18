package sbccunittest;

// 01/14/2020

import static org.junit.Assert.*;
import static sbcc.Core.*;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import org.junit.*;
import org.w3c.dom.ranges.*;

import rockcountdown.*;
import sbcc.*;

public class SongListTester {

	public static boolean isZeroScore = false;
	public static String scorePreamble = "";

	public static int totalScore = 0;
	public static int extraCredit = 0;

	public static InputStream defaultSystemIn;
	public static PrintStream defaultSystemOut;
	public static PrintStream defaultSystemErr;

	public static String newLine = "\r\n"; // System.getProperty("line.separator");

	@BeforeClass
	public static void beforeTesting() {
		totalScore = 0;
		extraCredit = 0;
	}


	@AfterClass
	public static void afterTesting() {
		if (isZeroScore) {
			totalScore = 0;
		}
		println(scorePreamble + "Estimated score (w/o late penalties, etc.) = " + totalScore);
	}


	@Before
	public void setUp() throws Exception {
		defaultSystemIn = System.in;
		defaultSystemOut = System.out;
		defaultSystemErr = System.err;
	}


	@After
	public void tearDown() throws Exception {
		System.setIn(defaultSystemIn);
		System.setOut(defaultSystemOut);
		System.setErr(defaultSystemErr);
	}


	@Test
	public void testNewSongFromFields() throws Exception {
		var song = new Song(1, "Some Title", "Some Artist");
		assertEquals(1, song.getRank());
		assertEquals("Some Artist", song.getArtist());
		song.setTitle("Some other title");
		assertEquals("Some other title", song.getTitle());
		totalScore += 5;
	}


	@Test
	public void testNewSongFromTabDelimitedString() throws Exception {
		Song song = new Song("12\tA Change Is Gonna Come\tSam Cooke");
		assertEquals(12, song.getRank());
		assertEquals("A Change Is Gonna Come", song.getTitle());
		assertEquals("Sam Cooke", song.getArtist());
		totalScore += 5;
	}


	@Test
	public void testReverseList() throws Exception {
		ArrayList<String> songList = new ArrayList<String>();
		Collections.addAll(songList, songArray);

		ArrayList<String> reverseTitleList = new ArrayList<String>();
		Collections.addAll(reverseTitleList, reverseTitleArray);

		int randomNdxToDelete = (int) (Math.random() * songList.size());
		songList.remove(randomNdxToDelete);
		reverseTitleList.remove(reverseTitleList.size() - randomNdxToDelete - 1);

		var sb = new StringBuilder();
		for (String song : songList)
			sb.append(song).append(newLine);
		try {
			writeFile("rs_top_30_songs.txt", sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendToStdinOfTestee("rs_top_30_songs.txt\n");
		final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myOut));

		Main.main(null);
		String output = myOut.toString();
		System.setOut(defaultSystemOut);

		sb = new StringBuilder();
		for (String song : reverseTitleList)
			sb.append(song).append(newLine);

		sb.append(newLine + "Complete" + newLine);
		String expectedOutput = sb.toString();

		// Convert to common end-of-line system.
		output = output.replace("\r\n", "\n");
		expectedOutput = expectedOutput.replace("\r\n", "\n");

		assertEquals(expectedOutput, output);
		totalScore += 6;
	}


	@Test
	public void testUsesTemplateProjectCorrectly()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		verifyCoreVersion();
		long count = Files.find(Paths.get("lib"), Integer.MAX_VALUE,
				(path, basicFileAttributes) -> path.toFile().getName().matches("sbcccore.*.*.*.jar")).count();
		assertTrue(
				"This project doesn't appear to be a copy of JavaCoreTemplate because the sbcccore library was not found in the lib directory.  See https://github.com/ProfessorStrenn/workspace-cs106#workspace-cs106 for help on using the JavaCoreTemplate.",
				count > 0);
		totalScore += 2;

		String src = readFile(Paths.get("src", "rockcountdown", "Main.java").toString());
		if (!src.contains("import static sbcc.Core.*;"))
			fail("static import of sbcc.Core not found.  To add sbcc.Core functionality to your project, see https://github.com/ProfessorStrenn/workspace-cs106#static-import-of-core-functionality.");
		if (src.contains("System.out.println"))
			fail("System.out.println()'s need to be replaced with just println().");
		totalScore += 2;
	}


	private void verifyCoreVersion()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		try {
			var method = Core.class.getMethod("getSbccCoreVersion");

			String ver = (String) method.invoke(null);
			var parts = ver.split("\\.");
			if (parseInt(parts[0]) < 1 || parseInt(parts[1]) < 0 || parseInt(parts[2]) < 3)
				throw new RangeException((short) 0, "sbcccore version is " + ver + ", but must be at least 1.0.3");

		} catch (RangeException | NoSuchMethodException e) {
			isZeroScore = true;
			scorePreamble = "RESUBMISSION REQUIRED (see unit test results).  ";
			fail("RESUBMISSION REQUIRED.  This project appears to be based on a previous semester's project template.  See https://github.com/ProfessorStrenn/workspace-cs106#workspace-cs106 for help on using the JavaCoreTemplate.");
		}
	}


	public void sendToStdinOfTestee(String message) {
		System.setIn(new ByteArrayInputStream(message.getBytes()));
	}

	private final String[] songArray = { "1	Like a Rolling Stone	Bob Dylan", "2	Satisfaction	The Rolling Stones",
			"3	Imagine	John Lennon", "4	What's Going On	Marvin Gaye", "5	Respect	Aretha Franklin",
			"6	Good Vibrations	The Beach Boys", "7	Johnny B Goode	Chuck Berry", "8	Hey Jude	The Beatles",
			"9	Smells Like Teen Spirit	Nirvana", "10	What'd I Say	Ray Charles", "11	My Generation	The Who",
			"12	A Change Is Gonna Come	Sam Cooke", "13	Yesterday	The Beatles", "14	Blowin' in the Wind	Bob Dylan",
			"15	London Calling	The Clash", "16	I Want to Hold Your Hand	The Beatles",
			"17	Purple Haze	Jimi Hendrix", "18	Maybellene	Chuck Berry", "19	Hound Dog	Elvis Presley",
			"20	Let It Be	The Beatles", "21	Born to Run	Bruce Springsteen", "22	Be My Baby	The Ronettes",
			"23	In My Life	The Beatles", "24	People Get Ready	The Impressions",
			"25	God Only Knows	The Beach Boys", "26	(Sittin on) the Dock of the Bay	Otis Redding",
			"27	Layla	Derek and the Dominos", "28	A Day in the Life	The Beatles", "29	Help!	The Beatles",
			"30	I Walk the Line	Johnny Cash" };

	private final String[] reverseTitleArray = { "30	I Walk the Line", "29	Help!", "28	A Day in the Life",
			"27	Layla", "26	(Sittin on) the Dock of the Bay", "25	God Only Knows", "24	People Get Ready",
			"23	In My Life", "22	Be My Baby", "21	Born to Run", "20	Let It Be", "19	Hound Dog", "18	Maybellene",
			"17	Purple Haze", "16	I Want to Hold Your Hand", "15	London Calling", "14	Blowin' in the Wind",
			"13	Yesterday", "12	A Change Is Gonna Come", "11	My Generation", "10	What'd I Say",
			"9	Smells Like Teen Spirit", "8	Hey Jude", "7	Johnny B Goode", "6	Good Vibrations", "5	Respect",
			"4	What's Going On", "3	Imagine", "2	Satisfaction", "1	Like a Rolling Stone" };

}
