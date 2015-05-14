package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2014
 (c) University of Vienna 2009-2014
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	protected String encoding = "n.a";
	protected long duration;
	protected String author = "n.a";
	protected String title = "n.a";
	protected Date date;
	protected String comment = "n.a";
	protected String album = "n.a";
	protected String track = "n.a";
	protected String composer = "n.a";
	protected String genre = "n.a";
	protected float frequency;
	protected int bitrate;
	protected int channels;

	/**
	 * Constructor.
	 */
	public AudioMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public AudioMedia(File instance) {
		super(instance);
	}

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* GET / SET methods */
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(String date) {
		try {
			this.date = new SimpleDateFormat("yyyy").parse(date);
		} catch (ParseException e) {
			// do nothing
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	/* (de-)serialization */

	/**
	 * Serializes this object to the passed file.
	 * 
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: audio");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		out.println("encoding: " + this.encoding);
		out.println("author: " + this.author);
		out.println("title: " + this.title);
		out.println("comment: " + this.comment);
		out.println("album: " + this.album);
		out.println("track: " + this.track);
		out.println("composer: " + this.composer);
		out.println("genre: " + this.genre);
		out.println("frequency: " + this.frequency);
		out.println("bitrate: " + this.bitrate);
		out.println("channels: " + this.channels);
		
		try {
			out.println("date: " + new SimpleDateFormat("yyyy").format(this.date));
		} catch (Exception e) {
			// do nothing
		}
		
		return data.getBuffer();
	}

	/**
	 * Deserializes this object from the passed string buffer.
	 * 
	 * @throws ParseException
	 */
	@Override
	public void deserializeObject(String data) throws IOException {
		super.deserializeObject(data);

		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;

		while ((line = br.readLine()) != null) {

			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************

			if (line.startsWith("encoding: ")) {
				this.setEncoding(line.substring("encoding: ".length()));

			} else if (line.startsWith("author: ")) {
				this.setAuthor(line.substring("author: ".length()));

			} else if (line.startsWith("title: ")) {
				this.setTitle(line.substring("title: ".length()));

			} else if (line.startsWith("date: ")) {
				this.setDate(line.substring("date: ".length()));

			} else if (line.startsWith("comment: ")) {
				this.setComment(line.substring("comment: ".length()));

			} else if (line.startsWith("album: ")) {
				this.setAlbum(line.substring("album: ".length()));

			} else if (line.startsWith("track: ")) {
				this.setTrack(line.substring("track: ".length()));

			} else if (line.startsWith("composer: ")) {
				this.setComposer(line.substring("composer: ".length()));

			} else if (line.startsWith("genre: ")) {
				this.setGenre(line.substring("genre: ".length()));

			} else if (line.startsWith("frequency: ")) {
				this.setFrequency(Integer.parseInt(line.substring("frequency: ".length())));

			} else if (line.startsWith("bitrate: ")) {
				this.setBitrate(Integer.parseInt(line.substring("bitrate: ".length())));

			} else if (line.startsWith("channels: ")) {
				this.setChannels(Integer.parseInt(line.substring("channels: ".length())));
			}
		}
	}

}