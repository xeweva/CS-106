package rockcountdown;

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

		try {

			var filename = readLine();
			var filedata = readFile(filename);
			var songsStr = filedata.split("\r\n");

			ArrayList<Song> songs = new ArrayList<Song>();

			for (int i = 0; i < songsStr.length; i++)
				songs.add(new Song(songsStr[i]));

			StringBuilder cmpltStr = new StringBuilder();

			for (int i = songs.size() - 1; i >= 0; i--)
				cmpltStr.append(songs.get(i).getRank() + "\t" + songs.get(i).getTitle() + "\r\n");

			println(cmpltStr + "\r\n" + "Complete");

		} catch (Exception e) {

			println(e);
		}

	}

}
