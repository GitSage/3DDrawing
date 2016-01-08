package cs355.view;

public class Vector4 {
    public double x, y, z, w;

    public Vector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4 apply(Matrix4 m) {
        x = (m.x0*x) + (m.x1*y) + (m.x2*z) + (m.x3*w);
        y = (m.y0*x) + (m.y1*y) + (m.y2*z) + (m.y3*w);
        z = (m.z0*x) + (m.z1*y) + (m.z2*z) + (m.z3*w);
        w = (m.w0*x) + (m.w1*y) + (m.w2*z) + (m.w3*w);

        return this;
    }

    public Vector4 divideW() {
        this.x = this.x/this.w;
        this.y = this.y/this.w;
        this.z = this.z/this.w;
        this.w = this.w/this.w;

        return this;
    }
}
