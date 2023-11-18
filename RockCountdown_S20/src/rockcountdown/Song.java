package rockcountdown;

import static sbcc.Core.*;

public class Song {

	int rank;
	String title = "";
	String artist = "";

	public Song(int rank, String title, String artist) {

		super();
		this.rank = rank;
		this.title = title;
		this.artist = artist;
	}


	public Song(String song) {

		super();

		var fields = song.split("\t");

		rank = parseInt(fields[0]);
		title = fields[1];
		artist = fields[2];
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getArtist() {
		return artist;
	}


	public void setArtist(String artist) {
		this.artist = artist;
	}

}
