package spellchecker;

import static sbcc.Core.*;

import java.util.*;

public class BinarySearchTree {

	public BinaryTreeNode root = null;
	public int count = 0;

	public void importFile(String filename) throws Exception {

		List<String> words = readFileAsLines(filename);

		while (words.get(0).toLowerCase().charAt(0) != 'm')
			Collections.shuffle(words);

		count = words.size();

		for (int i = 0; i < count; i++)
			add(words.get(i).toLowerCase().replaceAll("\\s", ""));
	}


	public void load(String filename) throws Exception {

		List<String> words = readFileAsLines(filename);

		count = words.size();

		root = new BinaryTreeNode(words.get(0).toLowerCase());

		Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();

		stack.push(root);

		for (int i = 1; i < count; ++i) {

			BinaryTreeNode curNode = null;
			String curWord = words.get(i).toLowerCase();

			while (!stack.isEmpty() && curWord.compareTo(stack.peek().value) > 0)
				curNode = stack.pop();

			if (curNode != null) {

				curNode.right = new BinaryTreeNode(curWord);
				stack.push(curNode.right);
			} else {

				curNode = stack.peek();
				curNode.left = new BinaryTreeNode(curWord);
				stack.push(curNode.left);
			}
		}
	}


	public void add(String word) {

		if (root == null) {

			root = new BinaryTreeNode(word);
			return;
		}

		BinaryTreeNode curNode = root, prNode = root;

		while (curNode != null) {

			prNode = curNode;

			if (word.compareTo(curNode.value) < 0)
				curNode = curNode.left;
			else
				curNode = curNode.right;
		}

		BinaryTreeNode newNode = new BinaryTreeNode(word);

		if (word.compareTo(prNode.value) < 0)
			prNode.left = newNode;
		else
			prNode.right = newNode;
	}


	public boolean contains(String word) {

		BinaryTreeNode curNode = root;

		while (curNode != null && !word.equals(curNode.value))
			if (word.compareTo(curNode.value) < 0)
				curNode = curNode.left;
			else
				curNode = curNode.right;

		return !(curNode == null);
	}


	public void save(String filename) throws Exception {

		StringBuilder output = new StringBuilder();

		saveTreePreOrder(root, output);

		writeFile(filename, output.toString());
	}


	private void saveTreePreOrder(BinaryTreeNode cursor, StringBuilder output) {

		if (cursor == null)
			return;

		saveNode(cursor, output);
		saveTreePreOrder(cursor.left, output);
		saveTreePreOrder(cursor.right, output);
	}


	private void saveNode(BinaryTreeNode cursor, StringBuilder output) {

		output.append(cursor.value + System.lineSeparator());
	}
}