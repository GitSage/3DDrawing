package cs355.model.image;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by benja on 12/3/2015.
 */
public class ImageFilter {
    public BufferedImage grayscale(BufferedImage buff){
        return null;
    }

    public double[][] meanFilter(double[][] img){
        double[][] result = new double[img.length][img[0].length];
        double[][] paddedImg = pad(img);

        for(int i = 1; i < paddedImg.length-1; i++){
            for(int j = 1; j < paddedImg[0].length-1; j++){
                // add up pixel plus surrounding 8 pixels
                double pix = paddedImg[i-1][j-1] + paddedImg[i-1][j  ] + paddedImg[i-1][j+1] +
                             paddedImg[i  ][j-1] + paddedImg[i  ][j  ] + paddedImg[i  ][j+1] +
                             paddedImg[i+1][j-1] + paddedImg[i+1][j  ] + paddedImg[i+1][j+1];
                result[i-1][j-1] = pix/9;
            }
        }

        return result;
    }

    public double[][] medianFilter(double[][] img){
        double[][] result = new double[img.length][img[0].length];
        double[][] paddedImg = pad(img);

        for(int i = 1; i < paddedImg.length-1; i++){
            for(int j = 1; j < paddedImg[0].length-1; j++){
                // add up pixel plus surrounding 8 pixels
                List<Double> sort = new ArrayList<>();
                sort.add(paddedImg[i-1][j-1]);
                sort.add(paddedImg[i-1][j]);
                sort.add(paddedImg[i-1][j+1]);
                sort.add(paddedImg[i][j-1]);
                sort.add(paddedImg[i][j]);
                sort.add(paddedImg[i][j+1]);
                sort.add(paddedImg[i+1][j-1]);
                sort.add(paddedImg[i+1][j]);
                sort.add(paddedImg[i+1][j+1]);
                Collections.sort(sort);
                result[i-1][j-1] = sort.get(4);
            }
        }
        return result;
    }

    public double[][] unsharp(double[][] img){
        double[][] mat = {{0,-1,0},
                {-1,5,-1},
                {0,-1,0}};
        double[][] convolved = convolve(img, mat);
        return convolved;
    }

    private double[][] edgeDetect(double[][] img){
        double[][] xSobel = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        double[][] ySobel = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        double[][] xImg = convolve(img, xSobel);
        printImg(xImg);
        double[][] yImg = convolve(img, ySobel);
        printImg(yImg);

        double[][] result = new double[img.length][img[0].length];
        for(int i = 0; i < result.length; i++){
            for(int j = 0; j < result[0].length; j++){
                double xSq = Math.pow(xImg[i][j], 2);
                double ySq = Math.pow(yImg[i][j], 2);
                result[i][j] = Math.sqrt(xSq + ySq);
            }
        }
        return result;
    }

    private double[][] pad(double[][] img){
        double[][] result = new double[img.length+2][img[0].length+2];
        // img into padded img
        for(int i = 0; i < img.length; i++){
            for(int j = 0; j < img[0].length; j++){
                result[i+1][j+1] = img[i][j];
            }
        }
        return result;
    }

    private double[][] convolve(double[][] img, double[][] mat){
        double[][] result = new double[img.length][img[0].length];
        double[][] paddedImg = pad(img);

        for(int i = 1; i < paddedImg.length-1; i++){
            for(int j = 1; j < paddedImg[0].length-1; j++){
                double pix = 0;
                for(int k = 0; k < mat.length; k++){
                    for(int m = 0; m < mat[0].length; m++){
                        pix += mat[k][m] * paddedImg[i-1+k][j-1+m];
                    }
                }
                result[i-1][j-1] = pix;
            }
        }

        return result;
    }

    public static void printImg(double[][] img){
        for(int i = 0; i < img.length; i++){
            System.out.println(Arrays.toString(img[i]));
        }
        System.out.println();
    }

    public static void main(String[] args){
        ImageFilter imgFilter = new ImageFilter();
        double[][] img =
                {{10,11,9 ,25,22},
                 {8 ,10,9 ,26,28},
                 {9 ,99,9 ,24,25},
                 {11,11,12,23,22},
                 {10,11,9 ,22,25}};

        double[][] result = imgFilter.edgeDetect(img);
        ImageFilter.printImg(result);
    }
}
