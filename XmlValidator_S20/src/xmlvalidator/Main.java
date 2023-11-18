package xmlvalidator;

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

		BasicXmlValidator validator = new BasicXmlValidator();

		String xml = "";

		xml = "<note vasya=\"as\"\n   georg=12>" + "\n   <bold>" + "\n   </bold>" + "\n</note>";

		println(xml + "\n\n" + validator.validate(xml));
	}
}
