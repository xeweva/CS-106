package spellchecker;

import static sbcc.Core.*;

import java.util.*;
import java.util.regex.*;

public class BasicSpellChecker implements SpellChecker {

	BasicDictionary dictionary = new BasicDictionary();

	String document = null;

	int wordInd = 0;

	@Override
	public void importDictionary(String filename) throws Exception {

		dictionary.importFile(filename);
	}


	@Override
	public void loadDictionary(String filename) throws Exception {

		dictionary.load(filename);
	}


	@Override
	public void saveDictionary(String filename) throws Exception {

		dictionary.save(filename);
	}


	@Override
	public void loadDocument(String filename) throws Exception {

		document = readFile(filename);
	}


	@Override
	public void saveDocument(String filename) throws Exception {

		writeFile(filename, document);
	}


	@Override
	public String getText() {

		return document;
	}


	/**
	 * Starts/continues a spell check of the text. Use the regular expression below to match words (it's not great, but
	 * it's simple and works OK for basic text).
	 * 
	 * <pre>
	 * \b[\w']+\b
	 * </pre>
	 * 
	 * The method returns when the first unknown word is located or when the end of the document is reached (whichever
	 * comes first). The index of the character after the unknown word is retained for use as the starting index for
	 * subsequent calls to spellCheck where continueFromPrevious is true.
	 * 
	 * @param continueFromPrevious If false, a new spell check is started from the first character of the document. If
	 *                                 true, the spell check continues from it's current location.
	 * @return If no unknown word is found this method returns null. Otherwise, it returns a String array organized as
	 *             follows:
	 * 
	 *             <pre>
	 *         [0] = Unknown word 
	 *         [1] = Index of start of unknown word.  The index is the position within the entire document.
	 *         [2] = Preceeding word in the dictionary .  If the unknown word is before all words in the dictionary, this element should be "".
	 *         [3] = Succeeding word in the dictionary.  If the unknown word is after all words in the dictionary, this element should be "".
	 *              e.g. 
	 *         [0] = "spelm"
	 *         [1] = "224"
	 *         [2] = "spells" 
	 *         [3] = "spelt"
	 *             </pre>
	 */
	@Override
	public String[] spellCheck(boolean continueFromPrevious) {

		var p = Pattern.compile("[\\w']+");
		Matcher docWords = p.matcher(document);

		if (!continueFromPrevious)
			wordInd = 0;
		else
			while (docWords.find() && docWords.start() < wordInd)
				;

		String[] suggestions = null;
		String curWord = null;

		while (suggestions == null && docWords.find()) {

			wordInd = docWords.start(); // get current word position
			curWord = docWords.group().toLowerCase();

			suggestions = dictionary.find(curWord.toLowerCase());
		}

		if (suggestions == null)
			return null;

		else {

			String[] error = { curWord, Integer.toString(wordInd), suggestions[0], suggestions[1] };

			return error;
		}
	}


	@Override
	public void addWordToDictionary(String word) {

		dictionary.add(word);
	}


	/**
	 * Replaces text in the document from startIndex to just before endIndex with the given replacementText.
	 * 
	 * NOTE: Be sure to update your spell checker index by adding the difference between the length of the replacement
	 * text and the length of the text that was replaced.
	 * 
	 * @param startIndex      Index of the first character to replace.
	 * @param endIndex        Index of the character <b>after</b> the last character to replace.
	 * @param replacementText
	 */
	@Override
	public void replaceText(int startIndex, int endIndex, String replacementText) {

		document = document.substring(0, startIndex) + replacementText + document.substring(endIndex);

		wordInd += replacementText.length() - (endIndex - startIndex);
	}

}
