package spellchecker;

import static sbcc.Core.*;

import java.util.*;

public class BasicDictionary implements Dictionary {

	BinarySearchTree tree = new BinarySearchTree();

	ArrayList<String> wordList = new ArrayList<String>();

	@Override
	public void importFile(String filename) throws Exception {

		tree.importFile(filename);

		List<String> tempWords = readFileAsLines(filename);

		for (String w : tempWords)
			wordList.add(w.toLowerCase().replaceAll(" ", ""));
	}


	@Override
	public void load(String filename) throws Exception {

		tree.load(filename);

		List<String> tempWords = readFileAsLines(filename);

		for (String w : tempWords)
			wordList.add(w.toLowerCase());

		Collections.sort(wordList);
	}


	@Override
	public void save(String filename) throws Exception {

		tree.save(filename);
	}


	/**
	 * 
	 * @param word
	 * @return If the word is <b>found</b> this method returns <b>null</b>. Otherwise, it returns a String array
	 *             organized as follows:
	 * 
	 *             <pre>
	 *         [0] = Preceeding word in the dictionary 
	 *         [1] = Succeeding word in the dictionary 
	 *         
	 *              e.g. if the unknown word was "spelm", the result might be:
	 *              
	 *         [0] = "spells" 
	 *         [1] = "spelt"
	 *         
	 *         If there is no preceeding or succeeding word in the dictionary, set the element to "".
	 *             </pre>
	 */
	@Override
	public String[] find(String word) {

		word = word.toLowerCase();

		if (tree.contains(word))
			return null;

		int ind = 0;

		for (int wordNum = tree.count; ind < wordNum; ind++)
			if (word.compareTo(wordList.get(ind)) < 0)
				break;

		String[] error = { (ind > 0 ? wordList.get(ind - 1) : ""), (ind < wordList.size() ? wordList.get(ind) : "") };
		return error;

	}


	@Override
	public void add(String word) {

		if (!tree.contains(word)) {

			tree.add(word);

			wordList.add(word);
			Collections.sort(wordList);
		}
	}


	@Override
	public BinaryTreeNode getRoot() {

		return tree.root;
	}


	@Override
	public int getCount() {

		return tree.count;
	}
}
