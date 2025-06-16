package net.altosheeve.soprano.client.RenderMethods;

import net.minecraft.client.render.BufferBuilder;

public class RenderBox{
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
        this.b = 1f;
        this.a = 1f;
    }
    
    public void draw(BufferBuilder buffer) {
            //left face
        buffer.vertex(this.x, this.y, this.z).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y, this.z).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y + this.h, this.z).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x, this.y + this.w, this.z).color(this.r, this.g, this.b, this.a);

        //right face
        buffer.vertex(this.x, this.y, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y + this.h, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x, this.y + this.w, this.z + this.d).color(this.r, this.g, this.b, this.a);

        //bottom face
        buffer.vertex(this.x, this.y, this.z).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x, this.y, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y, this.z).color(this.r, this.g, this.b, this.a);

        //top face
        buffer.vertex(this.x, this.y + this.h, this.z).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x, this.y + this.h, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y + this.h, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y + this.h, this.z).color(this.r, this.g, this.b, this.a);

        //front face
        buffer.vertex(this.x, this.y, this.z).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x, this.y, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x, this.y + this.h, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x, this.y + this.h, this.z).color(this.r, this.g, this.b, this.a);

        //back face
        buffer.vertex(this.x + this.w, this.y, this.z).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y + this.h, this.z + this.d).color(this.r, this.g, this.b, this.a);
        buffer.vertex(this.x + this.w, this.y + this.h, this.z).color(this.r, this.g, this.b, this.a);
    }
}
