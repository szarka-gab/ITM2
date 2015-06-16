package itm.video;

/*******************************************************************************
 This file is part of the ITM course 2015
 (c) University of Vienna 2009-2015
 *******************************************************************************/

import itm.util.ImageCompare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

/**
 * This class reads video files, extracts metadata for both the audio and the
 * video track, and writes these metadata to a file.
 * 
 * It can be called with 3 parameters, an input filename/directory, an output
 * directory and an "overwrite" flag. It will read the input video file(s),
 * retrieve the metadata and write it to a text file in the output directory.
 * The overwrite flag indicates whether the resulting output file should be
 * overwritten or not.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class VideoThumbnailGenerator {
	
	ArrayList<IVideoPicture> allPictures = new ArrayList<IVideoPicture>();
	ArrayList<BufferedImage> allPictures_Buff = new ArrayList<BufferedImage>();
	
	int framesTotal;
	IContainer outContainer;
	IStream outStream;
	IStreamCoder outStreamCoder;
	IStreamCoder videoCoder = null;
	IRational frameRate;
	  
	int vWidth;
	int vHeight;
	
	long firstTimeStamp=-1;
  
	/**
	 * Constructor.
	 */
	public VideoThumbnailGenerator() {
	}

	/**
	 * Processes a video file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the video file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing output files should be overwritten
	 *            or not
	 * @return a list of the created media objects (videos)
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output, boolean overwrite, int timespan) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<File> ret = new ArrayList<File>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4"))
					try {
						File result = processVideo(f, output, overwrite, timespan);
						System.out.println("processed file " + f + " to " + output);
						ret.add(result);
					} catch (Exception e0) {
						System.err.println("Error processing file " + input + " : " + e0.toString());
					}
			}
		} else {

			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4"))
				try {
					File result = processVideo(input, output, overwrite, timespan);
					System.out.println("processed " + input + " to " + result);
					ret.add(result);
				} catch (Exception e0) {
					System.err.println("Error when creating processing file " + input + " : " + e0.toString());
				}

		}
		return ret;
	}
	

	@SuppressWarnings("deprecation")
	protected void getImagesFromVideo(File input, int timeSpan){
		
		IContainer container = IContainer.make();

		if (container.open(input.getAbsolutePath(), IContainer.Type.READ, null) < 0)
		      throw new IllegalArgumentException("Cant open data: " + input.getAbsolutePath());
		
		int numStreams = container.getNumStreams();
		
		int videoStreamId = -1;

	    for(int i = 0; i < numStreams; i++){
	    	
		      IStream stream = container.getStream(i);
	
		      IStreamCoder coder = stream.getStreamCoder();
	
		      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO){
		        videoStreamId = i;
		        videoCoder = coder;
		        break;
		      }
	    }

	    if (videoStreamId == -1)
	      throw new RuntimeException("VideoStream not found"+ input.getAbsolutePath());

	    if (videoCoder.open() < 0)
	      throw new RuntimeException("error opening videodecoder" + input.getAbsolutePath());

	    IVideoResampler resampler = null;
	    if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24){

	      resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24,videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
	      
	      if (resampler == null)
	        throw new RuntimeException("Error creating color space" + input.getAbsolutePath());
	    }

	    IPacket packet = IPacket.make();
	    
	    while(container.readNextPacket(packet) >= 0){
	      
	      if (packet.getStreamIndex() == videoStreamId){
	        IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),videoCoder.getWidth(), videoCoder.getHeight());

	        int offset = 0;
	        
		        while(offset < packet.getSize()){
		        	
			          int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
			          
			          if (bytesDecoded < 0)
			            throw new RuntimeException("error decoding video: " + input.getAbsolutePath());
			          
			          offset += bytesDecoded;
			          
			          if (picture.isComplete()){
			            IVideoPicture newPic = picture;
		
			            if (resampler != null){
			            	
			              newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(), picture.getHeight());
			              
			              if (resampler.resample(newPic, picture) < 0)
			                throw new RuntimeException("error resampling video: " + input.getAbsolutePath());
			            }
		
			            if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
			              throw new RuntimeException("error decoding video as BGR24 " + input.getAbsolutePath());
		
			            	allPictures.add(newPic);
		
			          }
		        }
	      }
	      
	      else{
	        do{} 
	        
	        while(false);
	      }
	    }

	    if (videoCoder != null){
	    	
		      videoCoder.close();
		      videoCoder = null;
	    }
	    
	    if (container !=null){
	    	
	      container.close();
	      container = null;
	      
	    }
	    
	    
	    //Timespan//
    	
    	if(timeSpan > 0){
    		System.out.println("timespan =" + timeSpan);
    		
	    	 int numPictures = allPictures.size();
	    	 int startWert = 0;
	    	 
	    	 for(int i = 0; i < (numPictures-1); i++){
	    		 if (startWert == allPictures.get(i).getPts()/1000000){
	
	    			 BufferedImage pictureToAdd = Utils.videoPictureToImage(allPictures.get(i));
	    			 allPictures_Buff.add(pictureToAdd);
	    			
	    			 startWert = startWert + timeSpan;
	    		 }
	    	 }
    		
    	}
    	
    	if(timeSpan == 0){
    		
	    		ArrayList<BufferedImage> tempList = new ArrayList<BufferedImage>();
	    		
		    		for(int i = 0; i < allPictures.size(); i++){
		    			tempList.add(Utils.videoPictureToImage(allPictures.get(i)));
		    		}
		    		
	    		for(int i = 0; i < allPictures.size(); i = i + 20){
	
	    		BufferedImage toCompare = Utils.videoPictureToImage(allPictures.get(i));
	    		allPictures_Buff.add(toCompare);
	    		
		    		for(int j = i; j < allPictures.size(); j= j + 3){
		    			
		    			BufferedImage compPic = tempList.get(j);
		    			
		    			ImageCompare tool = new ImageCompare(toCompare,compPic);
		    			tool.compare();
		    			
		    			if(tool.match() == false ){
		    				System.out.println("treffer");
		    				
		    				allPictures_Buff.add(compPic);
		    				i = j;
		    				break;
		    			}
	    			
		    		}
	    		}
	    	}
		    
    	framesTotal = allPictures_Buff.size();
    	
		}
	
	/**
	 * Processes the passed input video file and stores a thumbnail of it to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input video file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing files should be overwritten or not
	 * @return the created video media object
	 */
	protected File processVideo(File input, File output, boolean overwrite, int timespan) throws Exception {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		File outputFile = new File(output, input.getName() + "_thumb.swf");

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		
		getImagesFromVideo(input,timespan);

		String outFile = outputFile.getAbsolutePath();

		vHeight = allPictures_Buff.get(0).getHeight();
		vWidth = allPictures_Buff.get(0).getWidth();
		        		
		IMediaWriter outWriter = ToolFactory.makeWriter(outFile);
		outWriter.addVideoStream(0,0, ICodec.ID.CODEC_ID_FLV1,vWidth,vHeight);
		
		long startTime = System.nanoTime();
		int lange = framesTotal;
		int j = 0;
		for (int i = 0; i < 64*lange ; i++){
			
			outWriter.encodeVideo(0, allPictures_Buff.get(j), System.nanoTime()-startTime, TimeUnit.NANOSECONDS);

				if(i%64 == 0){
					j++;
					if(j >= framesTotal){
						break;
					}
				}
			
		}
		        
		outWriter.flush();
		outWriter.close();
		
		return outputFile;
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 3) {
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-video> <output-directory> <timespan>");
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-directory> <output-directory> <timespan>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        int timespan = 5;
        if(args.length == 3)
            timespan = Integer.parseInt(args[2]);
        
        VideoThumbnailGenerator videoMd = new VideoThumbnailGenerator();
        videoMd.batchProcessVideoFiles(fi, fo, true, timespan);
	}
}