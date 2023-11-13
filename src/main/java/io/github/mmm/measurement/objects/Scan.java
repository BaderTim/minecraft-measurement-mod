package io.github.mmm.measurement.objects;

public class Scan {

    private Scan2D scan2D;
    private Scan3D scan3D;
    private boolean is2D;

    public Scan(Scan2D scan2D) {
        this.scan2D = scan2D;
        this.is2D = true;
    }

    public Scan(Scan3D scan3D) {
        this.scan3D = scan3D;
        this.is2D = false;
    }

    @Override
    public String toString() {
        if (is2D) {
            return scan2D.toString();
        } else {
            return scan3D.toString();
        }
    }

    public Scan2D getScan2D() {
        return scan2D;
    }

    public Scan3D getScan3D() {
        return scan3D;
    }

    public boolean is2D() {
        return is2D;
    }

}
