package xmlvalidator;

public class BasicXmlTagStack implements XmlTagStack {

	private XmlTag[] xmlStack = new XmlTag[256];
	private int objNum = 0;

	@Override
	public void push(XmlTag item) {

		if (objNum >= xmlStack.length)
			extendSpace();

		xmlStack[objNum++] = item;
	}


	@Override
	public XmlTag pop() {

		return objNum == 0 ? null : xmlStack[(objNum--) - 1];
	}


	@Override
	public XmlTag peek(int position) {

		return position < objNum ? xmlStack[objNum - position - 1] : null;
	}


	@Override
	public int getCount() {

		return objNum;
	}


	/**
	 * extends stack space
	 */
	private void extendSpace() {

		XmlTag[] temp = new XmlTag[(int) (xmlStack.length * 1.25)];

		for (int i = objNum - 1; i >= 0; i--)
			temp[i] = xmlStack[i];

		xmlStack = temp;
	}


	/**
	 * 
	 * @return true if stack is empty
	 */
	public boolean isEmpty() {

		return objNum == 0 ? true : false;
	}
}
