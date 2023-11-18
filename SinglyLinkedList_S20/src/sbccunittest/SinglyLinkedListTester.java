package sbccunittest;

import static java.lang.Math.*;
import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import static sbcc.Core.*;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.*;
import org.junit.*;
import org.w3c.dom.ranges.*;

import sbcc.*;
import singlylinkedlist.*;

/**
 * 02/05/2020
 * 
 * @author sstrenn
 *
 */
public class SinglyLinkedListTester {

	public static String newline = System.getProperty("line.separator");

	public static int totalScore = 0;

	public static int extraCredit = 0;
	public static boolean isZeroScore = false;
	public static String scorePreamble = "";

	@BeforeClass
	public static void beforeTesting() {
		totalScore = 0;
		extraCredit = 0;
	}


	@AfterClass
	public static void afterTesting() {

		if (isZeroScore) {
			totalScore = 0;
			extraCredit = 0;
		}
		println(scorePreamble + "Estimated score (w/o late penalties, etc.) = " + totalScore);
		println("Estimated extra credit (assuming on time submission) = " + extraCredit);

		// If the project follows the naming convention, save the results in a folder on
		// the desktop. (Alex Kohanim)
		try {
			String directory = substringAfterLast(System.getProperty("user.dir"), File.separator);
			String userName = substringBefore(directory, "_").trim();
			String projectName = substringBetween(directory, "_", "_").trim();
			String home = System.getProperty("user.home");
			Files.createDirectories(
					Paths.get(home + File.separator + "Desktop" + File.separator + projectName.toLowerCase()));

			File f = new File(home + File.separator + "Desktop" + File.separator + projectName.toLowerCase()
					+ File.separator + "out.csv");

			FileWriter fw = new FileWriter(f); // the true will append the new data
			fw.write(userName + "," + totalScore + "," + extraCredit + "\r\n");// appends the string to the file
			fw.close();

		} catch (Exception e) {
		}
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
		totalScore += 1;

		String src = readFile(Paths.get("src", "singlylinkedlist", "Main.java").toString());
		if (!src.contains("import static sbcc.Core.*;"))
			fail("static import of sbcc.Core not found.  To add sbcc.Core functionality to your project, see https://github.com/ProfessorStrenn/workspace-cs106#static-import-of-core-functionality.");
		if (src.contains("System.out.println"))
			fail("System.out.println()'s need to be replaced with just println().");
		totalScore += 1;
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


	@Test
	public void testInsertOne() {

		// Create a BSLL
		var list = new BasicSinglyLinkedList();
		assertNull("When a new SinglyLinkedList is created, it's head should be null.", list.getFirst());
		assertEquals("When a new SinglyLinkedList is created, it's count must be 0.", 0, list.size());

		totalScore += 1;

		// Insert a value and verify that there is a single node whose next points to null.
		list.insert(null, "sbcc");
		var n = list.getFirst();
		assertNotNull("After inserting a value, the head should not be null.", n);
		assertEquals("sbcc", n.value);
		assertEquals(1, list.size());
		assertEquals("When the first value is inserted, its next pointer should be null.", null, list.getFirst().next);

		totalScore += 1;
	}


	@Test
	public void testInsert() {
		var list = new BasicSinglyLinkedList();

		// Insert items so that the order is a, b, c
		var na = list.insert(null, "a");
		var nc = list.insert(na, "c");
		var nb = list.insert(na, "b");

		// Verify size, values, and ordering of nodes
		assertEquals(3, list.size());
		assertEquals(na, list.getFirst());
		assertEquals("a", list.getFirst().value);

		assertEquals(nb, list.getFirst().next);
		assertEquals("b", list.getFirst().next.value);

		assertEquals(nc, list.getFirst().next.next);
		assertEquals("c", list.getFirst().next.next.value);

		assertEquals(null, list.getFirst().next.next.next);

		assertEquals(nc, list.getLast());

		totalScore += 5;
	}


	@Test
	public void testSpeedOfInsertAtCursor() {
		var list = new BasicSinglyLinkedList();

		// Verify that inserts after the start can be done in constant time.
		int numToInsert = 25000;
		int maxTime = 250;
		var start = nanoTime();
		var n = list.insert(null, "0");
		for (int i = numToInsert - 1; i > 0; i--)
			list.insert(n, i + "");
		var diff = (nanoTime() - start) / 1.0e6;

		// Verify values
		n = list.getFirst();
		for (int i = 0; i < numToInsert; i++) {
			assertEquals(i + "", n.value);
			n = n.next;
		}
		totalScore += 3;

		// Verify time
		assertTrue("Inserting " + numToInsert + " items must take less than " + maxTime + " ms.", diff < maxTime);

		totalScore += 2;
	}


	@Test
	public void testAppend() {
		int numWords = 5;

		// Build a list of words
		var words = new ArrayList<String>();
		for (int i = 0; i < numWords; i++)
			words.add(RandomStringUtils.randomAlphabetic(3));

		// Add them to the BSLL
		var list = new BasicSinglyLinkedList();
		for (var word : words)
			list.append(word);

		assertEquals(numWords, list.size());

		// Verify that each word in the BSLL matches
		var n = list.getFirst();
		for (var word : words) {
			assertEquals(word, n.value);
			n = n.next;
		}

		totalScore += 5;
	}


	@Test
	public void testToCsvString() {
		int numWords = 5;

		// Build a list of words
		var words = new ArrayList<String>();
		for (int i = 0; i < numWords; i++)
			words.add(RandomStringUtils.randomAlphabetic(3));

		var expectedCsv = join(words, ',');

		// Add the words to the BSLL
		var list = new BasicSinglyLinkedList();
		for (var word : words)
			list.append(word);

		assertEquals(expectedCsv, list.toCsvString());

		totalScore += 3;
	}


	@Test
	public void testToList() {

		int numWords = 5;

		// Build a list of words
		var words = new ArrayList<String>();
		for (int i = 0; i < numWords; i++)
			words.add(RandomStringUtils.randomAlphabetic(3));

		// Add them to the BSLL
		var list = new BasicSinglyLinkedList();
		for (var word : words)
			list.append(word);

		assertEquals(words, list.toList());
		totalScore += 3;
	}


	@Test
	public void testFind() {
		int numWords = 100;
		int randNdx = (int) Math.max(random() * numWords - 1, 0.0);
		String key = "blahdiblah";

		// Build a list of words
		var words = new ArrayList<String>();
		for (int i = 0; i < numWords; i++)
			words.add(RandomStringUtils.randomAlphabetic(3));

		// Put the key at a random location
		words.set(randNdx, key);

		// Add the words to a BSLL
		var list = new BasicSinglyLinkedList();
		var n = list.getFirst();
		for (var word : words)
			n = list.insert(n, word);

		// Find and verify the word. Also make sure that the next word in the BSLL
		// matches what is expected.
		n = list.find(list.getFirst(), key);
		assertEquals(key, n.value);
		assertEquals(words.get(randNdx + 1), n.next.value);

		totalScore += 4;

	}


	@Test
	public void testRemove() {
		var list = new BasicSinglyLinkedList();

		// Build a list
		var n = list.getFirst();
		int numItems = 5;
		for (int i = 0; i < numItems; i++)
			n = list.insert(n, i + "");

		// Remove first
		n = list.getFirst();
		assertEquals("0", n.value);
		var oldSecond = n.next;
		list.remove(n);
		assertEquals(numItems - 1, list.size());
		assertEquals(oldSecond, list.getFirst()); // Verify that the old second is now first

		totalScore += 2;

		// Remove last. Start by following the list to the last node
		n = list.getFirst();
		var p = n;
		for (int i = 0; i < numItems - 2; i++) {
			n = n.next;
			if (i > 0)
				p = p.next;
		}
		assertEquals((numItems - 1) + "", n.value); // Ensure the last node has the right value
		list.remove(n);
		assertEquals(numItems - 2, list.size());
		assertEquals(null, p.next); // Make sure that the old penultimate node now points at null.

		totalScore += 2;

		// Remove middle nodes by repeatedly deleting the second node
		var f = list.getFirst();
		n = f.next;
		int i = numItems - 2;
		while (i > 1) {
			var third = n.next;
			list.remove(n);
			assertEquals(--i, list.size());
			assertEquals(third, f.next); // Verify that the first node now points at the old 3rd node
			n = f.next;
		}
		totalScore += 2;

		assertEquals(list.getFirst(), list.getLast()); // Verify that the first node is the last node
		list.remove(list.getLast());
		assertEquals(0, list.size());
		assertEquals(null, list.getFirst());

		// Remove from empty list - nothing should happen, particularly no exceptions.
		for (i = 0; i < 5; i++)
			list.remove(list.getFirst());

		totalScore += 2;
	}


	@Test
	public void testLoadFile() {
		var lines = IntStream.range(0, 100000).mapToObj(i -> "line " + i).collect(Collectors.toList());
		Collections.shuffle(lines);
		try {
			writeFileAsLines("lines.txt", lines);
		} catch (IOException e) {
			fail(e.getMessage());
		}

		var list = new BasicSinglyLinkedList();
		var start = nanoTime();
		try {
			list.loadFile("lines.txt");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		var diff = (nanoTime() - start) / 1.0e6;

		assertEquals(lines, list.toList()); // Verify contents

		totalScore += 5;

		if (diff < 1500)
			extraCredit += 2;
		else
			println("To receive extra credit, loadFile must take less than 1500ms, but it took " + diff + " ms");
	}


	@Test
	public void testSaveFile() {
		var lines = IntStream.range(0, 40000).mapToObj(i -> "line " + i).collect(Collectors.toList());
		Collections.shuffle(lines);

		var list = new BasicSinglyLinkedList();
		var n = list.getFirst();
		for (var line : lines)
			n = list.insert(n, line);

		try {
			list.saveFile("saved_lines.txt");
		} catch (IOException e) {
			fail(e.getMessage());
		}

		try {
			var linesIn = readFileAsLines("saved_lines.txt");
			assertEquals(lines, linesIn); // Verify contents

		} catch (IOException e) {
			fail(e.getMessage());
		}

		totalScore += 4;
	}


	@Test
	public void testPmd() {
		try {
			execPmd("." + File.separator + "src" + File.separator + "singlylinkedlist", "cs106.ruleset");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		totalScore += 4;

	}


	private static void execPmd(String srcFolder, String rulePath) throws Exception {

		File srcDir = new File(srcFolder);
		File ruleFile = new File(rulePath);

		verifySrcAndRulesExist(srcDir, ruleFile);

		ProcessBuilder pb;
		if (getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			String pmdBatPath = ".\\pmd_min\\bin\\pmd.bat";
			String curPath = Paths.get(".").toAbsolutePath().toString();

			// Handle CS lab situation where the current dir is a UNC path
			if (curPath.startsWith("\\\\NEBULA\\cloud$")) {
				curPath = "N:\\" + substringAfter(curPath, "cloud$\\");
				pmdBatPath = curPath + pmdBatPath.substring(1);
			}
			pb = new ProcessBuilder(pmdBatPath, "-f", "text", "-d", srcDir.getAbsolutePath(), "-R",
					ruleFile.getAbsolutePath());
		} else {
			pb = new ProcessBuilder("./pmd_min/bin/run.sh", "pmd", "-d", srcDir.getAbsolutePath(), "-R",
					ruleFile.getAbsolutePath());
		}
		Process process = pb.start();
		int errCode = process.waitFor();

		switch (errCode) {

		case 1:
			out.println("PMD Check: -5 pts");
			String errorOutput = getOutput(process.getErrorStream());
			fail("Command Error:  " + errorOutput);
			break;

		case 4:
			out.println("PMD Check: -5 pts");
			String output = getOutput(process.getInputStream());
			fail(trimFullClassPaths(output));
			break;

		}

	}


	private static String trimFullClassPaths(String output) {
		// Shorten output to just the short class name, line, and error.
		String[] lines = output.split(getProperty("line.separator"));
		StringBuilder sb = new StringBuilder();
		for (String line : lines)
			sb.append(substringAfterLast(line, File.separator)).append(lineSeparator());

		String trimmedOutput = sb.toString();
		return trimmedOutput;
	}


	private static void verifySrcAndRulesExist(File fileFolderToCheck, File ruleFile) throws Exception {
		if (!fileFolderToCheck.exists())
			throw new FileNotFoundException(
					"The folder to check '" + fileFolderToCheck.getAbsolutePath() + "' does not exist.");

		if (!fileFolderToCheck.isDirectory())
			throw new FileNotFoundException(
					"The folder to check '" + fileFolderToCheck.getAbsolutePath() + "' is not a directory.");

		if (!ruleFile.exists())
			throw new FileNotFoundException(
					"The rule set file '" + ruleFile.getAbsolutePath() + "' could not be found.");
	}


	private static String getOutput(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + getProperty("line.separator"));
			}
		} finally {
			br.close();
		}
		return sb.toString();

	}

}
