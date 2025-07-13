package image_processing;

import java.awt.image.BufferedImage;

public class ImageToASCIIConverter {
    private char[] palette;
    public static final int black = -16777216;
    public static final int white = -1;
    private int[] breakPoints;
    private int widthFactor;

    public static char[][] palettes = new char[][]{
            {' ', '.'},
            {' ', '.', 'W'},
            { ' ', '.', '_', ':', '-', '=','+' ,'*','w','8','#','O','Q','&','@' },//12 elements
            { ' ', '.', '_', ':', '-', '=','+' ,'*','#','&','@' },// looks nice
            { ' ', '.', ',', '-', '~', ':', ';', '=', '+', '*', '!','(', '|','[','{', '#', '%', '$', '@', '&', '8', 'B', 'M', 'W' }, // the best one
            { ' ', '\'', '.', '"', '-', '^', '+', 'i', 'l', 'o', 'x', '%', '&', '8', 'O', '@', 'M', 'W', '#' }, // still good
            { ' ', '.', '`', '\'', ':', ';', '"', '*', '|', '/', '\\', '^', '>', '<', '!', '~', '=', '#', '$', '@' },
            { ' ', '.', ',', '-', '=', '+', '*', 'x', '/', '|', '1', '2', '3', '4', '%', '#', '$', '@' }
    };



    public ImageToASCIIConverter(char[] palette, int widthFactor) {
        this.palette = palette;
        int len = palette.length;
        breakPoints = new int[len + 1];
        // Set the black and white breakpoints explicitly
        breakPoints[0] = black;
        breakPoints[len] = white;
        createBreakPoints(0, len);
        this.widthFactor = widthFactor;


    }

    private void createBreakPoints(int low, int high) {
        if (low + 1 >= high) return;  // Base condition: stop if the range is too small

        int mid = (low + high) / 2;
        breakPoints[mid] = (breakPoints[low] + breakPoints[high]) / 2;  // Set the midpoint value

        // Recurse into the lower and upper halves
        createBreakPoints(low, mid);  // Corrected: include mid-point for next recursion
        createBreakPoints(mid, high); // Corrected: include mid-point for next recursion
    }


//    public char[][] convertToASCII(BufferedImage img) {
//        ImageManipulator converter = new ImageManipulator(img, img.getWidth()/4, ImageManipulator.ConvertingMethod.DOWNSAMPLE, 1);
//        BufferedImage resizedImage = ImageProcessing.convertToBufferedImage(
//                ImageProcessing.convertToGrayScaleArray(
//                        ImageProcessing.convertToBufferedImage(converter.getResizedImage())
//                )
//        );
//
//        char[][] result = new char[resizedImage.getHeight()][resizedImage.getWidth() * widthFactor];
//
//        for (int y = 0; y < resizedImage.getHeight(); y++) {
//            for (int x = 0; x < resizedImage.getWidth(); x++) {
//                char c = palette[convertPixel(resizedImage.getRGB(x, y))];
//                for (int i = 0; i < widthFactor; i++) {
//                    result[y][x++] = c;
//                }
//            }
//        }
//
//        return result;
//    }

    public char[][] convertToASCII(BufferedImage img, int reductionFactor, double contrastFactor) {
        ImageManipulator converter = new ImageManipulator(img, img.getWidth() / reductionFactor, ImageManipulator.ConvertingMethod.DOWNSAMPLE, contrastFactor);
        BufferedImage resizedImage = ImageProcessing.convertToBufferedImage(
                ImageProcessing.convertToGrayScaleArray(
                        ImageProcessing.convertToBufferedImage(converter.getResizedImage())
                )
        );

        char[][] result = new char[resizedImage.getHeight()][resizedImage.getWidth() * widthFactor];

        for (int y = 0; y < resizedImage.getHeight(); y++) {
            for (int x = 0; x < resizedImage.getWidth(); x++) {
                char c = palette[convertPixel(resizedImage.getRGB(x, y))];
                // Use a separate index for the width factor expansion
                for (int i = 0; i < widthFactor; i++) {
                    result[y][x * widthFactor + i] = c;
                }
            }
        }

        return result;
    }



    private int convertPixel(int pixel) {
        for (int i = 0; i < breakPoints.length - 1; i++) {
            if (pixel >= breakPoints[i] && pixel <= breakPoints[i + 1]) {
                return i;
            }
        }
        return palette.length - 1;  // Default to the last palette character if no match
    }



}
