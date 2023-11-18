package singlylinkedlist;

import java.io.*;
import java.util.*;

/**
 * Defines the interface for a singly-linked list.
 * 
 */
public interface SinglyLinkedList {

	/**
	 * @return Reference to the first node. If the list is empty, this method returns null.
	 */
	public Node getFirst();


	/**
	 * @return Reference to the last node . If the list is empty, this method returns null.
	 */
	public Node getLast();


	/**
	 * @return Number of nodes in the list
	 */
	public int size();


	/**
	 * @return true if the list has no nodes; false otherwise
	 */
	public boolean isEmpty();


	/**
	 * Removes all nodes in the list.
	 */
	public void clear();


	/**
	 * Inserts a new node with the given value after n.
	 * 
	 * @param cursor The node to insert after. Set this to null to insert value as the new first Node.
	 * @param value  The value to insert
	 * @return a reference to the newly inserted Node
	 */
	public Node insert(Node cursor, String value);


	/**
	 * Inserts a new node with the given value at the "end" of the list.
	 * 
	 * @param value
	 * @return a reference to the newly inserted Node
	 */
	public Node append(String value);


	/**
	 * Removes the specified Node from the list.
	 * 
	 * @param cursor A reference to the Node to remove.
	 */
	public void remove(Node cursor);


	/**
	 * Returns a reference to the first Node containing the key, starting from the given Node.
	 * 
	 * @param start
	 * @param key
	 * @return a reference to the first Node containing the key
	 */
	public Node find(Node start, String key);


	/**
	 * @return a comma-separated String of the values in the list. Don't add any spaces between the values.
	 */
	public String toCsvString();


	/**
	 * @return an ArrayList<String> of the values in the list.
	 */
	public ArrayList<String> toList();


	/**
	 * Loads text from file into new nodes in the list; one line from the file becomes one node in the list. For
	 * example, if the file looks like this:
	 * 
	 * <pre>
	 * These are
	 * the days
	 * you'll remember
	 * </pre>
	 * 
	 * then the resulting list would be:
	 * 
	 * These are -> the days -> you'll remember -> null
	 * 
	 * @param filename Path to the file
	 * @throws IOException
	 */
	public void loadFile(String filename) throws IOException;


	/**
	 * Saves values from the nodes in the list to the specified file; one line in the file for each node in the list.
	 * 
	 * @param filename Path to the file
	 * @throws IOException
	 */
	public void saveFile(String filename) throws IOException;

}
