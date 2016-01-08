package cs355.model.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Created by benja on 12/8/2015.
 */
public class FlexibleImage extends CS355Image{
    BufferedImage bufImg;

    @Override
    public BufferedImage getImage() {
        if(getWidth() == 0 || getHeight() == 0){
            return null;
        }
        return bufImg;
    }

    @Override
    public boolean open(File file){
        boolean success = super.open(file);
        recalcBufferedImage();
        return success;
    }

    @Override
    public void edgeDetection() {
        float[][][] pixelArray = getHSBPixelArray();
        float[][] xSobel = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        float[][] ySobel = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        int[] rgb = new int[3];

        float[][][] xPixelArray = convolve(pixelArray, xSobel);
        float[][][] yPixelArray = convolve(pixelArray, ySobel);

        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                double xSq = Math.pow(xPixelArray[i][j][2]/8, 2);
                double ySq = Math.pow(yPixelArray[i][j][2]/8, 2);
                double dist = Math.sqrt(xSq + ySq);

                // Convert HSB brightness to [0,255], then set as all three values of rgb array.
                int b = (int)((dist+1)*(255d/2));
                rgb[0] = b;
                rgb[1] = b;
                rgb[2] = b;
                setPixel(i, j, rgb);
            }
        }

        recalcBufferedImage();
    }

    @Override
    public void sharpen() {
        float[][][] pixelArray = getRGBPixelArray();
        float[][] mat = {{0,-1,0}, {-1,6,-1}, {0,-1,0}};
        int[] rgb = new int[3];
        pixelArray = convolve(pixelArray, mat);

        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                rgb[0] = (int)(pixelArray[i][j][0] / 2f);
                rgb[1] = (int)(pixelArray[i][j][1] / 2f);
                rgb[2] = (int)(pixelArray[i][j][2] / 2f);


                if(rgb[0] > 255){
                    System.out.println(rgb[0]);
                }

                rgb[0] = rgb[0] > 255 ? 255 : rgb[0];
                rgb[1] = rgb[1] > 255 ? 255 : rgb[1];
                rgb[2] = rgb[2] > 255 ? 255 : rgb[2];

                rgb[0] = rgb[0] < 0 ? 0 : rgb[0];
                rgb[1] = rgb[1] < 0 ? 0 : rgb[1];
                rgb[2] = rgb[2] < 0 ? 0 : rgb[2];

                setPixel(i, j, rgb);
            }
        }

        recalcBufferedImage();
    }

    @Override
    public void medianBlur() {
        float[][][] pixelArray = getRGBPixelArray();
        float[][][] paddedImg = pad(pixelArray);
        java.util.List<Float> rList = new ArrayList<>();
        java.util.List<Float> gList = new ArrayList<>();
        java.util.List<Float> bList = new ArrayList<>();

        for(int i = 1; i < paddedImg.length-1; i++) {
            for (int j = 1; j < paddedImg[0].length-1; j++) {
                // get median of each channel
                rList.clear();
                gList.clear();
                bList.clear();

                for(int k = i-1; k <= i+1; k++) {
                    for (int m = j - 1; m <= j + 1; m++) {
                        rList.add(paddedImg[k][m][0]);
                        gList.add(paddedImg[k][m][1]);
                        bList.add(paddedImg[k][m][2]);
                    }
                }
                Collections.sort(rList);
                Collections.sort(gList);
                Collections.sort(bList);

                float rMed = rList.get(4);
                float gMed = gList.get(4);
                float bMed = bList.get(4);

                // now find the closest color
                float[] closestColor = new float[3];
                double closestDistance = Float.MAX_VALUE;

                for(int k = i-1; k < i+1; k++) {
                    for (int m = j - 1; m < j + 1; m++) {
                        float[] pix = paddedImg[k][m];
                        double distance = Math.pow(pix[0] - rMed, 2) +
                                Math.pow(pix[1] - gMed, 2) +
                                Math.pow(pix[2] - bMed, 2);
                        if(distance < closestDistance){
                            closestColor = pix;
                            closestDistance = distance;
                        }
                    }
                }

                int[] newPix = {(int)closestColor[0], (int)closestColor[1], (int)closestColor[2]};
                setPixel(i-1, j-1, newPix);
            }
        }

        recalcBufferedImage();
    }

    @Override
    public void uniformBlur() {
        float[][][] pixelArray = getRGBPixelArray();
        float[][] mask = {{1.0f/9,1.0f/9,1.0f/9}, {1.0f/9,1.0f/9,1.0f/9}, {1.0f/9,1.0f/9,1.0f/9}};

        pixelArray = convolve(pixelArray, mask);

        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                int[] rgb = {(int)pixelArray[i][j][0],(int)pixelArray[i][j][1],(int)pixelArray[i][j][2]};
                setPixel(i, j, rgb);
            }
        }

        recalcBufferedImage();
    }

    @Override
    public void grayscale() {
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                float[] hsb = getHSBPixel(i, j);

                hsb[1] = 0;

                int[] rgb = hsbToRGB(hsb);
                setPixel(i, j, rgb);

            }
        }
        recalcBufferedImage();
    }

    @Override
    public void contrast(int amount) {
        int[] rgb = new int[3];
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                getPixel(i, j, rgb);
                double r = (double)rgb[0];
                double g = (double)rgb[1];
                double b = (double)rgb[2];
                rgb[0] = (int)(Math.pow(((amount+100d)/100d), 4) * (r-128d) + 128d);
                rgb[1] = (int)(Math.pow(((amount+100d)/100d), 4) * (g-128d) + 128d);
                rgb[2] = (int)(Math.pow(((amount+100d)/100d), 4) * (b-128d) + 128d);


                rgb[0] = rgb[0] > 255 ? 255 : rgb[0];
                rgb[1] = rgb[1] > 255 ? 255 : rgb[1];
                rgb[2] = rgb[2] > 255 ? 255 : rgb[2];

                rgb[0] = rgb[0] < 0 ? 0 : rgb[0];
                rgb[1] = rgb[1] < 0 ? 0 : rgb[1];
                rgb[2] = rgb[2] < 0 ? 0 : rgb[2];

                setPixel(i, j, rgb);
            }
        }

        recalcBufferedImage();

    }

    @Override
    public void brightness(int amount) {
        float adjustedAmount = (float)amount / 100f;
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                float[] hsb = getHSBPixel(i, j);
                hsb[2] += adjustedAmount;

                hsb[2] = hsb[2] > 1f ? 1f : hsb[2];
                hsb[2] = hsb[2] < 0f ? 0f : hsb[2];

                setPixel(i, j, hsbToRGB(hsb));
            }
        }

        recalcBufferedImage();
    }

    private float[][][] getHSBPixelArray(){
        float[][][] result = new float[getWidth()][getHeight()][3];
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                float[] pix = getHSBPixel(i, j);
                result[i][j][0] = pix[0];
                result[i][j][1] = pix[1];
                result[i][j][2] = pix[2];
            }
        }
        return result;
    }

    private float[][][] getRGBPixelArray(){
        float[][][] result = new float[getWidth()][getHeight()][3];
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                int[] pix = getPixel(i, j, null);
                result[i][j][0] = pix[0];
                result[i][j][1] = pix[1];
                result[i][j][2] = pix[2];
            }
        }
        return result;
    }

    private void recalcBufferedImage(){
        bufImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                int[] color = getPixel(i, j, null);
                int rgb = color[0];
                rgb = (rgb << 8) + color[1];
                rgb = (rgb << 8) + color[2];
                bufImg.setRGB(i, j, rgb);
            }
        }
    }

    private float[][][] convolve(float[][][] img, float[][] mask){
        float[][][] result = new float[img.length][img[0].length][3];
        float[][][] paddedImg = pad(img);

        for(int i = 1; i < paddedImg.length-1; i++){
            for(int j = 1; j < paddedImg[0].length-1; j++){
                float[] pix = new float[3];
                for(int k = 0; k < mask.length; k++){
                    for(int m = 0; m < mask[0].length; m++){
                        pix[0] += mask[k][m] * paddedImg[i+k-1][j+m-1][0];
                        pix[1] += mask[k][m] * paddedImg[i+k-1][j+m-1][1];
                        pix[2] += mask[k][m] * paddedImg[i+k-1][j+m-1][2];
                    }
                }
                result[i-1][j-1] = pix;
            }
        }

        return result;
    }

    private float[][][] pad(float[][][] img){
        float[][][] result = new float[img.length+2][img[0].length+2][3];
        // img into padded img
        for(int i = 0; i < img.length; i++){
            for(int j = 0; j < img[0].length; j++){
                result[i+1][j+1][0] = img[i][j][0];
                result[i+1][j+1][1] = img[i][j][1];
                result[i+1][j+1][2] = img[i][j][2];
            }
        }
        return result;
    }

    private float[] getHSBPixel(int x, int y){
        int[] rgb = new int[3];
        float[] hsb = new float[3];
        getPixel(x, y, rgb);
        Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
        return hsb;
    }

    private int[] hsbToRGB(float[] hsb){
        int[] rgb = new int[3];
        Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        rgb[0] = c.getRed();
        rgb[1] = c.getGreen();
        rgb[2] = c.getBlue();
        return rgb;
    }
}
