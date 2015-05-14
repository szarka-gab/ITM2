package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioStream;

/**
 * Plays an audio file using the system's default sound output device
 * 
 */
public class AudioPlayer {

	/**
	 * Constructor
	 */
	public AudioPlayer() {

	}

	/**
	 * Plays audio data from a given input file to the system's default sound
	 * output device
	 * 
	 * @param input
	 *            the audio file
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	protected void playAudio(File input) throws IOException {

		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");

		AudioInputStream audio = null;
		try {
			audio = openAudioInputStream(input);
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("could not open audio file " + input
					+ ". Encoding / file format not supported");
		}

		try {
			rawplay(audio);
		} catch (LineUnavailableException e) {
			throw new IOException("Error when playing sound from file "
					+ input.getName() + ". Sound output device unavailable");
		}

		audio.close();

	}

	/**
	 * Decodes an encoded audio file and returns a PCM input stream
	 * 
	 * Supported encodings: MP3, OGG (requires SPIs to be in the classpath)
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @return a PCM AudioInputStream
	 * @throws UnsupportedAudioFileException
	 *             an audio file's encoding is not supported
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	private AudioInputStream openAudioInputStream(File input)
			throws UnsupportedAudioFileException, IOException {

		AudioInputStream din = null;
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// open audio stream
		AudioInputStream in = AudioSystem.getAudioInputStream(input);
		
		// get format
		AudioFormat baseFormat = in.getFormat();
		
		// get decoded format
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false);
		// get decoded audio input stream
		din = AudioSystem.getAudioInputStream(decodedFormat, in);
		
		return din;
	}

	/**
	 * Writes audio data from an AudioInputStream to a SourceDataline
	 * 
	 * @param audio
	 *            the audio data
	 * @throws IOException
	 *             error when writing audio data to source data line
	 * @throws LineUnavailableException
	 *             system's default source data line is not available
	 */
	private void rawplay(AudioInputStream audio) throws IOException,
			LineUnavailableException {

		
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// get audio format
		AudioFormat Format =audio.getFormat();
		
		// get a source data line
		
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,Format);
		SourceDataLine l = (SourceDataLine) AudioSystem.getLine(info);
		
		l.open(Format);
        l.start();
		
		
		// read samples from audio and write them to the data line 
        
        int numBytes = 1024 * 64; 
        byte[] audioBytes = new byte[numBytes];
        int bRead=0;
        
        if (bRead!=-1){
        for(;;){
        	bRead=audio.read(audioBytes,0,audioBytes.length);
        	if (bRead>-1){
        		l.write(audioBytes, 0, bRead);
        		}
        	
        	}
        }


		// properly close the line!
        
        l.drain();
        l.stop();
        l.close();
	}
		
		

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.out
					.println("usage: java itm.audio.AudioPlayer <input-audioFile>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		AudioPlayer player = new AudioPlayer();
		player.playAudio(fi);
		System.exit(0);

	}

}
