package flights;

import org.apache.commons.lang3.*;

class Flight {

	private String price, airline, from, to;

	public Flight(String from, String to, String airline, String price) {

		this.from = from;
		this.to = to;
		this.airline = airline;
		this.price = price;
	}


	public String getFrom() {
		return from;
	}


	public String getTo() {
		return to;
	}


	public String getAirline() {
		return airline;
	}


	public String getPrice() {
		return price;
	}
}