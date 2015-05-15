package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class VideoMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* video format metadata */

	protected String videoCodec;
	protected String videoCodecID;
	protected String videoFrameRate;
	protected long videoLength;
	protected int videoHeight;
	protected int videoWidth;
	
	/* audio format metadata */
	protected String audioCodec;
	protected String audioCodecID;
	protected int audioChannels;
	protected int audioSampleRate;
	protected int audioBitRate;
	
	/**
	 * Constructor.
	 */
	public VideoMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public VideoMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************
	public String getVideoCodec() {
		return videoCodec;
	}

	public void setVideoCodec(String videoCodec) {
		if (videoCodec.startsWith("com.xuggle")) {
			this.videoCodec = videoCodec.split("name=")[1].split(";")[0];
		} else {
			this.videoCodec = videoCodec;
		}
	}

	public String getVideoCodecID() {
		return videoCodecID;
	}

	public void setVideoCodecID(String videoCodecID) {
		this.videoCodecID = videoCodecID;
	}

	public String getVideoFrameRate() {
		return videoFrameRate;
	}

	public void setVideoFrameRate(String videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	public long getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(long videoLength) {
		this.videoLength = videoLength / 1000000;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public String getAudioCodec() {
		return audioCodec;
	}

	public void setAudioCodec(String audioCodec) {
		if (audioCodec.startsWith("com.xuggle")) {
			this.audioCodec = audioCodec.split("name=")[1].split(";")[0];
		} else {
			this.audioCodec = audioCodec;
		}
	}

	public String getAudioCodecID() {
		return audioCodecID;
	}

	public void setAudioCodecID(String audioCodecID) {
		this.audioCodecID = audioCodecID;
	}

	public int getAudioChannels() {
		return audioChannels;
	}

	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}

	public int getAudioSampleRate() {
		return audioSampleRate;
	}

	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate * 1000;
	}

	public int getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(int audioBitRate) {
		this.audioBitRate = audioBitRate / 1000;
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
		out.println("type: video");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		/* video fields */
		out.println("videoCodec: " + this.videoCodec);
		out.println("videoCodecID: " + this.videoCodecID);
		out.println("videoFrameRate: " + this.videoFrameRate);
		out.println("videoLength: " + this.videoLength);
		out.println("videoHeight: " + this.videoHeight);
		out.println("videoWidth: " + this.videoWidth);
		
		/* audio fields */
		out.println("audioCodec: " + this.audioCodec);
		out.println("audioCodecID: " + this.audioCodecID);
		out.println("audioChannels: " + this.audioChannels);
		out.println("audioSampleRate: " + this.audioSampleRate);
		out.println("audioBitRate: " + this.audioBitRate);
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************
		return data.getBuffer();
	}

	/**
	 * Deserializes this object from the passed string buffer.
	 */
	@Override
	public void deserializeObject(String data) throws IOException {
		super.deserializeObject(data);

		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {

			/* video fields */
			
			if (line.startsWith("videoCodec: ")) {
				this.setVideoCodec(line.substring("videoCodec: ".length()));

			} else if (line.startsWith("videoCodecID: ")) {
				this.setVideoCodecID(line.substring("videoCodecID: ".length()));

			} else if (line.startsWith("videoFrameRate: ")) {
				this.setVideoFrameRate(line.substring("videoFrameRate: ".length()));

			} else if (line.startsWith("videoLength: ")) {
				this.setVideoLength(Integer.parseInt(line.substring("videoLength: ".length())) * 1000000);

			} else if (line.startsWith("videoHeight: ")) {
				this.setVideoHeight(Integer.parseInt(line.substring("videoHeight: ".length())));

			} else if (line.startsWith("videoWidth: ")) {
				this.setVideoWidth(Integer.parseInt(line.substring("videoWidth: ".length())));

			} else if (line.startsWith("audioCodec: ")) {
				this.setAudioCodec(line.substring("audioCodec: ".length()));

			} else if (line.startsWith("audioCodecID: ")) {
				this.setAudioCodecID(line.substring("audioCodecID: ".length()));

			} else if (line.startsWith("audioChannels: ")) {
				this.setAudioChannels(Integer.parseInt(line.substring("audioChannels: ".length())));

			} else if (line.startsWith("audioSampleRate: ")) {
				this.setAudioSampleRate(Integer.parseInt(line.substring("audioSampleRate: ".length())) / 1000);

			} else if (line.startsWith("audioBitRate: ")) {
				this.setAudioBitRate(Integer.parseInt(line.substring("audioBitRate: ".length())) * 1000);
			}
			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************
		}
	}

}
