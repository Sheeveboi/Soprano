package net.altosheeve.soprano.client;

public class RenderBox {
    public int x;
    public int y;
    public int z;

    public int w;
    public int h;
    public int d;

    public float r;
    public float g;
    public float b;
    public float a;

    public RenderBox(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.w = 1;
        this.h = 1;
        this.d = 1;

        this.r = 1f;
        this.g = 0f;
        this.b = 0f;
        this.a = 1f;
    }
}
