package cs355.view;

import java.awt.geom.AffineTransform;

public class ManualAffineTransform extends AffineTransform {
    @Override
    public void rotate(double theta) {
        double[] matrix = new double[6];
        this.getMatrix(matrix);

        double c = Math.cos(-theta);
        double s = Math.sin(-theta);

        this.setTransform(
            matrix[0] * c + matrix[2] * -s,
            matrix[1] * c + matrix[3] * -s,
            matrix[0] * s + matrix[2] * c,
            matrix[1] * s + matrix[3] * c,
            matrix[4],
            matrix[5]
        );
    }

    @Override
    public void translate(double tx, double ty) {
        double[] matrix = new double[6];
        this.getMatrix(matrix);

        this.setTransform(
            matrix[0],
            matrix[1],
            matrix[2],
            matrix[3],
            matrix[0] * tx + matrix[2] * ty + matrix[4],
            matrix[1] * tx + matrix[3] * ty + matrix[5]
        );
    }

    @Override
    public void scale(double sx, double sy) {
        double[] matrix = new double[6];
        this.getMatrix(matrix);

        this.setTransform(
            matrix[0] * sx,
            matrix[1] * sx,
            matrix[2] * sy,
            matrix[3] * sy,
            matrix[4],
            matrix[5]
        );
    }
}
