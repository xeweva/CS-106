package flights;

import static sbcc.Core.*;

import java.util.*;

import org.apache.commons.lang3.*;

import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * 
 * @author Max Dokukin
 *
 */
public class Main {

	public static void main(String[] args) {

		ArrayList<Flight> flights = loadFlights("flights.txt");

		printf("%-16s%-8s%-8s%-5s\n%-16s%-8s%-8s%-5s\n", "AIRLINE", "FROM", "TO", "PRICE", "-------", "----", "--",
				"-----");

		for (Flight f : flights) {

			printf("%-16s%-8s%-8s%-5s\n", f.getAirline(), f.getFrom(), f.getTo(), f.getPrice());
		}
	}


	public static ArrayList<Flight> loadFlights(String fileName) {

		ArrayList<Flight> flights = new ArrayList<Flight>();

		try {

			List<String> lines = readFileAsLines(fileName);

			for (String line : lines) {

				String[] flightInfo = line.split(", ");
				String[] airports = StringUtils.splitByWholeSeparator(flightInfo[0], " => ");

				flights.add(new Flight(airports[0], airports[1], flightInfo[1], flightInfo[2]));
			}

		} catch (Exception e) {

			println(e);
		}

		return flights;
	}

}
