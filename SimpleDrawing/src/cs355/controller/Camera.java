package cs355.controller;

import cs355.view.Matrix4;

public class Camera {
    public double x, y, z;
    public double angle = 0;

    double fov = 60, near = 1, far = 50;
    double zoom = 1/Math.tan(Math.toRadians(fov/2));

    public Camera(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Matrix4 getPerspectiveMatrix() {
        double c = Math.cos(Math.toRadians(-angle)), s = Math.sin(Math.toRadians(-angle));

        Matrix4 p = new Matrix4(
                1, 0, 0, -x,
                0, 1, 0, -y,
                0, 0, 1, -z,
                0, 0, 0, 1
        ).multiply(
                c, 0, s, 0,
                0, 1, 0, 0,
                -s, 0, c, 0,
                0, 0, 0, 1
        ).multiply(
                zoom, 0, 0, 0,
                0, zoom, 0, 0,
                0, 0, (far+near)/(far-near), (-2*far*near)/(far-near),
                0, 0, 1,  0
        );

        return p;
    }
}
