package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2015
    (c) University of Vienna 2009-2015
*******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
    This class converts images into various image formats (BMP, PNG, ...).
    It can be called with 3 parameters, an input filename/directory, an output directory and a target format.
    It will read the input image(s) and convert it/them to the target format and write them to the output directory.
    
    If the input file/dir or the output directory do not exist, an exception is thrown.
*/
public class ImageConverter {

    public final static String BMP = "bmp";
    public final static String PNG = "png";
    public final static String JPEG = "jpeg";
   
    /**
        Constructor.
    */
    public ImageConverter()
    {
    }

    /**
        Processes the passed input image / image directory and stores the processed files to the output directory.
        @param input a reference to the input image / input directory
        @param output a reference to the output directory
      	@param targetFormat bmp, png or jpeg
      	@param quality a number between 0 (minimum quality) and 1 (max quality)  
    */
    public ArrayList<File> batchProcessImages( File input, File output, String targetFormat, float quality ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );
        if ( ( ! targetFormat.equalsIgnoreCase( BMP ) ) &&
             ( ! targetFormat.equalsIgnoreCase( PNG ) ) &&
             ( ! targetFormat.equalsIgnoreCase( JPEG ) ) )
            throw new IllegalArgumentException( "Unknown target format: " + targetFormat );
             

        ArrayList<File> ret = new ArrayList<File>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, targetFormat, quality  );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + f + " : " + e0.toString() );
                    }
                 }
            } else {
            try {
                File result = processImage( input, output, targetFormat, quality );
                System.out.println( "converted " + input + " to " + result );
                ret.add( result );
            } catch ( Exception e0 ) {
                System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        return ret;
    }    
    
    /**
        Processes the passed input image and stores the processed file to the output directory.
        @param input a reference to the input image
        @param output a reference to the output directory
      	@param targetFormat bmp, png or jpeg
      	@param quality a number between 0 (minimum quality) and 1 (max quality)  
    */
	protected File processImage( File input, File output, String targetFormat, float quality ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( input.isDirectory() ) 
            throw new IOException( "Input file " + input + " is a directory!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );
        if ( ( ! targetFormat.equalsIgnoreCase( BMP ) ) &&
                ( ! targetFormat.equalsIgnoreCase( PNG ) ) &&
                ( ! targetFormat.equalsIgnoreCase( JPEG ) ) )
               throw new IllegalArgumentException( "Unknown target format: " + targetFormat );

        File outputFile = null;
        
        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // load the input image
       
        // encode and save the image 

        return outputFile;
    }
    
        
    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
    	if ( args.length < 3 ) {
            System.out.println( "usage: java itm.image.ImageConverter <input-image> <output-directory> <format> [<quality>]" );
            System.out.println( "usage: java itm.image.ImageConverter <input-directory> <output-directory> <format> [<quality>]" );
            System.out.println( "");
            System.out.println( "formats: bmp, png, jpeg" );
            System.out.println( "quality: only for jpeg, 0: low quality, 1: highest quality" );
            System.exit( 1 );
            }
        // read params
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        String targetFormat = args[2];
        float quality = 1.0f;
        if ( args.length > 3 )
        	quality = Float.parseFloat( args[3] );

        System.out.println( "converting " + fi.getAbsolutePath() + " to " + fo.getAbsolutePath() );
        ImageConverter converter = new ImageConverter();
        converter.batchProcessImages( fi, fo, targetFormat, quality );        
    }    
}
