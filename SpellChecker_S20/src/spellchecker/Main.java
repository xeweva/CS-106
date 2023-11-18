package spellchecker;

import static sbcc.Core.*;
import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;

/**
 * 
 * @author Max Dokukin
 *
 */
public class Main {

	public static void main(String[] args) {

		try {

			SpellChecker basicSpellChecker = new BasicSpellChecker();

			String dictionaryImportPath = "small_dictionary.txt";
			String dictionaryPath = "small_dictionary.pre";
			String documentPath = "small_document_four_unknown.txt";

			basicSpellChecker.importDictionary(dictionaryImportPath);
			basicSpellChecker.saveDictionary(dictionaryPath);

			basicSpellChecker.loadDocument(documentPath);

			String[] result;

			// Find first unknown
			result = basicSpellChecker.spellCheck(false);
			if (result == null)
				fail("Failed to find the first unknown word in " + documentPath + " when the dictionary is "
						+ dictionaryImportPath);
			else {
				assertEquals("explosins", result[0]);
				assertEquals("87", result[1]);
				assertEquals("ever", result[2]);
				assertEquals("explosions", result[3]);
			}

			// Replace it with the successor word
			int startIndex = Integer.parseInt(result[1]);
			int endIndex = startIndex + result[0].length();

			println("orig txt : " + basicSpellChecker.getText());
			basicSpellChecker.replaceText(startIndex, endIndex, result[3]);
			println("1st cor : " + basicSpellChecker.getText());

			// find the 2nd unknown (the word "which")
			result = basicSpellChecker.spellCheck(true);
			if (result == null)
				fail("Failed to find the second unknown word in " + documentPath + " when the dictionary is "
						+ dictionaryImportPath);
			else {

				println(result[0] + "  " + result[1] + "  " + result[2] + "  " + result[3]);

				assertEquals("which", result[0]);
				assertEquals("130", result[1]);
				assertEquals("use", result[2]);
				assertEquals("with", result[3]);
			}

			// Add this word to the dictionary
			String wordToAdd = result[0];
			basicSpellChecker.addWordToDictionary(result[0]);

			// find the 3rd unknown (the word "vast")
			result = basicSpellChecker.spellCheck(true);
			if (result == null)
				fail("Failed to find the third unknown word in " + documentPath + " when the dictionary is "
						+ dictionaryImportPath);
			else {
				assertEquals("vast", result[0]);
				assertEquals("275", result[1]);
				assertEquals("use", result[2]);
				assertEquals("which", result[3]);
			}

			// Find third unknown
			result = basicSpellChecker.spellCheck(true);
			if (result == null)
				fail("Failed to find the fourth unknown word in " + documentPath + " when the dictionary is "
						+ dictionaryImportPath);
			else {
				assertEquals("cuosmos", result[0]);
				assertEquals("280", result[1]);
				assertEquals("cosmos", result[2]);
				assertEquals("dozen", result[3]);
			}

			// Replace it with the predecessor word
			startIndex = Integer.parseInt(result[1]);
			endIndex = startIndex + result[0].length();
			basicSpellChecker.replaceText(startIndex, endIndex, result[2]);

			// Verify document is correct
			String expectedText = readFile("small_document_four_unknown_corrected.txt");
			String actualText = basicSpellChecker.getText();
			assertEquals(expectedText, actualText);

			// Verify the saved document is correct
			basicSpellChecker.saveDocument("small_document_four_unknown_after_spellchecking.txt");
			actualText = readFile("small_document_four_unknown_after_spellchecking.txt");
			assertEquals(expectedText, actualText);

			// Verify the dictionary is correct
			basicSpellChecker.saveDictionary("small_dictionary_after_spellchecking.pre");
			String dictText = readFile("small_dictionary_after_spellchecking.pre");

			if (!dictText.contains(wordToAdd))
				fail("Dictionary file didn't contain " + wordToAdd + ".");

		} catch (Exception e) {

			println(e);
		}
	}

}
