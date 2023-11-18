package singlylinkedlist;

import static sbcc.Core.*;

import java.util.*;

import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * 
 * @author Max Dokukin
 *
 */
public class Main {

	public static void main(String[] args) {

		BasicSinglyLinkedList list = new BasicSinglyLinkedList();

		// list.insert(null, "first node");
		// list.insert(list.insert(list.insert(null, "first node"), "second node"), "third node");

		// list.append("fourth node");

		// list.remove(list.get(3));

		// list.clear();

		try {

			list.loadFile("nodesData.txt");

			println(list.size());

			// list.insert(null, "very first node");

			// list.remove(list.get(0));

			list.saveFile("nodesData.txt");

			println(list.toList());

		} catch (Exception e) {

			println(e);
		}

	}

}
