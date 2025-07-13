package image_processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {


	/**
	 * Converts the input RGB image to a single-channel gray scale array.
	 * 
	 * @param img The input RGB image
	 * @return A 2-D array with intensities
	 */
	public static int[][] convertToGrayScaleArray(BufferedImage img) {
		int[][] result = new int[img.getHeight()][img.getWidth()];

			for (int y = 0; y < img.getHeight(); y++) {
				for (int x = 0; x < img.getWidth(); x++) {
					result[y][x] = convertARGBtoGray(img.getRGB(x, y) & 0xFFFFFF);
//					result[y][x] = convertARGBtoGray(img.getRGB(x, y));

				}
			}

		return result;
	}

	public static int convertARGBtoGray(int argb) {
		int alpha = (argb >> 24) & 0xff;
		int red = (argb >> 16) & 0xff;
		int green = (argb >> 8) & 0xff;
		int blue = (argb) & 0xff;
		int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
		return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
	}

	/**
	 * Converts a single-channel (gray scale) array to an RGB image.
	 * 
	 * @param img The input image array
	 * @return BufferedImage
	 */
	public static BufferedImage convertToBufferedImage(int[][] img) {
		BufferedImage image = new BufferedImage(img[0].length, img.length, BufferedImage.TYPE_INT_RGB);
		for (int height = 0; height<img.length; height++){
			int[] currLine = img[height];
			for (int width = 0; width<currLine.length; width++){
				int rgb = currLine[width];
				image.setRGB(width, height, rgb);
			}
		}
		return image;
	}

	/**
	 * Saves an image to the given file path
	 *
	 * @param img The RGB image
	 * @param path The path to save the image to
	 */
	public static void saveImage(BufferedImage img, String path) throws IOException {
		try{
			File f = new File(path);
			ImageIO.write(img, "png", f);
		} catch(Exception e){
			System.out.println("IO error occured while saving image to "+path);
		}




	}

	public static int[][] changeContrast(int[][] img, double contrastFactor) {
		int[][] result = new int[img.length][img[0].length];

		for (int y = 0; y < img.length; y++) {
			for (int x = 0; x < img[0].length; x++) {
				// Apply the contrast formula
				int oldValue = img[y][x];
				int newValue = (int) ((oldValue - 128) * contrastFactor + 128);

				// Assign the adjusted value to the result
				result[y][x] = newValue;
			}
		}
		return result;
	}




	public static void main(String[] args) throws IOException {
		String path = ".\\src\\imageProcessing\\images\\";
		BufferedImage img  = ImageIO.read(new File(path +"example.jpg"));
		ImageManipulator converter = new ImageManipulator(img, img.getWidth()/8, ImageManipulator.ConvertingMethod.DOWNSAMPLE, 1);
		BufferedImage resizedImage = convertToBufferedImage(convertToGrayScaleArray(convertToBufferedImage(converter.getResizedImage())));


		saveImage(resizedImage, path + "\\resized.png");
		System.out.println(converter);



    }


}
