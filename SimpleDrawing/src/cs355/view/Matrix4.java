package cs355.view;

public class Matrix4 {
    double x0 = 1, x1, x2, x3,
           y0, y1 = 1, y2, y3,
           z0, z1, z2 = 1, z3,
           w0, w1, w2, w3 = 1;

    public Matrix4() {}

    public Matrix4(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3, double z0, double z1, double z2, double z3, double w0, double w1, double w2, double w3) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.y0 = y0;
        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        this.z0 = z0;
        this.z1 = z1;
        this.z2 = z2;
        this.z3 = z3;
        this.w0 = w0;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }

    public Matrix4 multiply(double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3, double z0, double z1, double z2, double z3, double w0, double w1, double w2, double w3) {
        this.x0 = (x0*this.x0) + (x1*this.y0) + (x2*this.z0) + (x3*this.w0);
        this.x1 = (x0*this.x1) + (x1*this.y1) + (x2*this.z1) + (x3*this.w1);
        this.x2 = (x0*this.x2) + (x1*this.y2) + (x2*this.z2) + (x3*this.w2);
        this.x3 = (x0*this.x3) + (x1*this.y3) + (x2*this.z3) + (x3*this.w3);

        this.y0 = (y0*this.x0) + (y1*this.y0) + (y2*this.z0) + (y3*this.w0);
        this.y1 = (y0*this.x1) + (y1*this.y1) + (y2*this.z1) + (y3*this.w1);
        this.y2 = (y0*this.x2) + (y1*this.y2) + (y2*this.z2) + (y3*this.w2);
        this.y3 = (y0*this.x3) + (y1*this.y3) + (y2*this.z3) + (y3*this.w3);

        this.z0 = (z0*this.x0) + (z1*this.y0) + (z2*this.z0) + (z3*this.w0);
        this.z1 = (z0*this.x1) + (z1*this.y1) + (z2*this.z1) + (z3*this.w1);
        this.z2 = (z0*this.x2) + (z1*this.y2) + (z2*this.z2) + (z3*this.w2);
        this.z3 = (z0*this.x3) + (z1*this.y3) + (z2*this.z3) + (z3*this.w3);

        this.w0 = (w0*this.x0) + (w1*this.y0) + (w2*this.z0) + (w3*this.w0);
        this.w1 = (w0*this.x1) + (w1*this.y1) + (w2*this.z1) + (w3*this.w1);
        this.w2 = (w0*this.x2) + (w1*this.y2) + (w2*this.z2) + (w3*this.w2);
        this.w3 = (w0*this.x3) + (w1*this.y3) + (w2*this.z3) + (w3*this.w3);

        return this;
    }

    public Matrix4 multiply(Matrix4 m) {
        return multiply(m.x0, m.x1, m.x2, m.x3, m.y0, m.y1, m.y2, m.y3, m.z0, m.z1, m.z2, m.z3, m.w0, m.w1, m.w2, m.w3);
    }
}
