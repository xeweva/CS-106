package xmlvalidator;

import java.util.*;
import java.util.regex.*;

import org.apache.commons.lang3.*;

import static sbcc.Core.*;

public class BasicXmlValidator implements XmlValidator {

	private BasicXmlTagStack stack = new BasicXmlTagStack();

	@Override
	public List<String> validate(String xmlDocument) {

		List<String> errorMessage = null;
		var p = Pattern.compile("<[^>]+>");
		Matcher tags = p.matcher(xmlDocument);

		int curTagPos = 0;
		String curTagName = "";

		while (tags.find() && errorMessage == null) {

			curTagName = tags.group(); // get current tag
			curTagPos = tags.start(); // get current tag position

			if (!ignoreTag(curTagName) && hasAttributes(curTagName)) { // if there are attributes

				int[] lineBias = new int[1]; // int to store unquoted attribute line bias relative to tag line
				String unquotedAtb = getUnquotedAttribute(curTagName, lineBias);

				if (unquotedAtb != null) { // if there is unquoted attribute

					int line = getTagLine(xmlDocument.substring(0, curTagPos)); // get tag line
					curTagName = tagFilter(tags.group()); // get rid of parentheses and attributes

					errorMessage = getError("Attribute not quoted", getTagText(curTagName), Integer.toString(line),
							unquotedAtb, Integer.toString(line + lineBias[0]));
					break;
				}
			}

			curTagName = tagFilter(tags.group()); // get rid of parentheses and attributes

			if (curTagName.isEmpty()) // if there is no tag after filter
				continue; // skip tag

			if (curTagName.charAt(0) == '/') { // found closing tag

				if (stack.isEmpty()) // orphan closing tag
					errorMessage = getError("Orphan closing tag", getTagText(curTagName),
							getTagLineStr(xmlDocument.substring(0, curTagPos)), null, null);

				else {

					var lastTag = stack.pop(); // get last tag from stack

					if (!curTagName.contains(lastTag.name)) // tag mismatch
						errorMessage = getError("Tag mismatch", getTagText(lastTag.name),
								getTagLineStr(xmlDocument.substring(0, lastTag.index)), getTagText(curTagName),
								getTagLineStr(xmlDocument.substring(0, curTagPos)));
				}

			} else { // found open tag

				stack.push(new XmlTag(curTagName, curTagPos)); // add new tag to stack
			}
		}

		if (!stack.isEmpty() && errorMessage == null) // unclosed tag at the end
			errorMessage = getError("Unclosed tag at end", getTagText(stack.pop().name),
					getTagLineStr(xmlDocument.substring(0, curTagPos)), null, null);

		return errorMessage;
	}


	/**
	 * tag line in int format
	 * 
	 * @param all the text from the beginning of the file to the tag
	 * @return tag line
	 */
	private int getTagLine(String text) {

		int count = 1;

		for (int i = text.length() - 1; i >= 0; i--)
			if (text.charAt(i) == '\n')
				count++;

		return count;
	}


	/**
	 * tag line in string format
	 * 
	 * @param all the text from the beginning of the file to the tag
	 * @return tag line
	 */
	private String getTagLineStr(String text) {

		return Integer.toString(getTagLine(text));
	}


	/**
	 * 
	 * @param tag
	 * @return tag without parentheses and parameters
	 */
	private String tagFilter(String tag) {

		if (ignoreTag(tag))
			return "";

		if (hasAttributes(tag)) // if tag has parameters
			tag = tag.split(" ")[0] + ">";

		return tag.substring(1, tag.length() - 1); // get rid of <>
	}


	/**
	 * filters tags
	 * 
	 * @param tag
	 * @return if tag should be ignored
	 */
	private boolean ignoreTag(String tag) {

		char c = tag.charAt(1);

		return !((c == '/' || (c >= 65 && c <= 90) || (c >= 97 && c <= 121)) && tag.charAt(tag.length() - 2) != '/');
	}


	/**
	 * prepares tag for error message
	 * 
	 * @param tag
	 * @return tag text without anything else
	 */
	private String getTagText(String tag) {

		if (tag.charAt(0) == '/')
			tag = tag.substring(1, tag.length()); // deleting /

		return tag;
	}


	/**
	 * @param tag
	 * @return if tag has any attributes
	 */
	private boolean hasAttributes(String tag) {

		return tag.replaceAll("\n", " ").contains(" ");
	}


	/**
	 * finds unquoted attribute and sets it's line bias relative to the tag's line
	 * 
	 * @param tag
	 * @param lineBias place to store unquoted attribute line bias
	 * @return unquoted attribute
	 */
	private String getUnquotedAttribute(String tag, int[] lineBias) {

		String[] attributes = tag.replaceAll("\n", " ").split(" ");

		String unquotedAtb = null;

		for (int i = 1; i < attributes.length; i++)
			if (!attributes[i].isEmpty() && StringUtils.substringBetween(attributes[i], "=\"", "\"") == null) {

				unquotedAtb = StringUtils.substringBefore(attributes[i], "=");
				break;
			}

		if (unquotedAtb != null)
			lineBias[0] = getTagLine(StringUtils.substringBetween(tag, "<", unquotedAtb)) - 1; // unquoted line bias
																								// relatively to tag
																								// line
		return unquotedAtb;
	}


	/**
	 * generates error message
	 * 
	 * @param error    - error name
	 * @param tag1name
	 * @param tag1line
	 * @param tag2name
	 * @param tag2line
	 * @return error array
	 */
	private List<String> getError(String error, String tag1name, String tag1line, String tag2name, String tag2line) {

		List<String> errorMessage = new ArrayList<String>();

		errorMessage.add(error);
		errorMessage.add(tag1name);
		errorMessage.add(tag1line);

		if (tag2name != null) {

			errorMessage.add(tag2name);
			errorMessage.add(tag2line);
		}

		return errorMessage;
	}
}