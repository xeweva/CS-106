package singlylinkedlist;

import java.io.*;
import java.util.*;

import static sbcc.Core.*;

public class BasicSinglyLinkedList implements SinglyLinkedList {

	Node firstNode = null;

	@Override
	public Node getFirst() {

		return firstNode;
	}


	@Override
	public Node getLast() {

		Node curNode = firstNode;

		if (curNode != null)
			for (; curNode.next != null; curNode = curNode.next)
				;

		return curNode;
	}


	/**
	 * get node by index
	 * 
	 * @param index
	 * @return
	 */
	public Node get(int ind) {

		if (ind < size() && ind >= 0) {

			Node curNode = firstNode;

			for (int i = 0; i != ind; i++)
				curNode = curNode.next;

			return curNode;
		} else
			return null;
	}


	@Override
	public int size() {

		int count = 1;

		if (firstNode != null)
			for (Node curNode = firstNode; curNode.next != null; curNode = curNode.next)
				count++;
		else
			return 0;

		return count;
	}


	@Override
	public boolean isEmpty() {

		return firstNode == null ? true : false;
	}


	@Override
	public void clear() {

		firstNode = null;
	}


	@Override
	public Node insert(Node cursor, String value) {

		Node newNode = new Node(value);

		if (cursor == null) {

			if (!isEmpty())
				newNode.next = firstNode;

			firstNode = newNode;
		} else {

			newNode.next = cursor.next;
			cursor.next = newNode;
		}

		return newNode;
	}


	@Override
	public Node append(String value) {

		return insert(getLast(), value);
	}


	@Override
	public void remove(Node cursor) {

		if (cursor != null) {

			if (cursor != firstNode) {

				Node prevNode = firstNode;

				for (; prevNode.next != cursor; prevNode = prevNode.next)
					;

				prevNode.next = cursor.next;
			} else
				firstNode = firstNode.next;

		}
	}


	@Override
	public Node find(Node start, String key) {

		if (start != null) {

			Node curNode = start;

			for (; !key.equals(curNode.value) && curNode != null; curNode = curNode.next)
				;

			return curNode;
		} else
			return null;
	}


	@Override
	public String toCsvString() {

		String output = "";

		for (Node curNode = firstNode; curNode != null; curNode = curNode.next)
			output += curNode.value + (curNode.next == null ? "" : ",");

		return output;
	}


	@Override
	public ArrayList<String> toList() {

		ArrayList<String> output = new ArrayList<String>();

		for (Node curNode = firstNode; curNode != null; curNode = curNode.next)
			output.add(curNode.value);

		return output;
	}


	@Override
	public void loadFile(String filename) throws IOException {

		try {
			List<String> data = readFileAsLines(filename);

			Node lastNode = null;

			for (String curStr : data)
				lastNode = insert(lastNode, curStr);

		} catch (Exception e) {

			throw e;
		}

	}


	@Override
	public void saveFile(String filename) throws IOException {

		try {

			writeFileAsLines(filename, toList());

		} catch (Exception e) {

			throw e;
		}
	}
}
