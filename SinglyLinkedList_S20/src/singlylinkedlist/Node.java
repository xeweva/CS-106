package singlylinkedlist;

public class Node {

	public String value;
	public Node next;


	public Node(String value) {
		super();
		this.value = value;
	}


	@Override
	public String toString() {
		return value;
	}

}
