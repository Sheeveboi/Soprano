package net.altosheeve.soprano.client.RenderMethods;

import net.altosheeve.soprano.client.Core.Rendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Waypoint {

    public enum Type {
        GOOD_GUY,
        NORMAL,
        SHITTER,
        HITLER,
        SNITCH,
        SNITCH_ALERT,
        PING,
        ALERT,
        PERMANENT
    }

    public static ArrayList<Waypoint> waypoints = new ArrayList<>();

    public float x;
    public float y;
    public float z;

    public Type type;
    public float importance;
    public float decayRate;

    public Waypoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void draw(BufferBuilder buffer) {

        float originalScale = 10f;
        float scale = originalScale * (float) (0.005f * (Rendering.client.player.getPos().distanceTo(new Vec3d(this.x + .5, this.y + .5, this.z + .5)) / 2.4));
        scale = Math.clamp(scale, originalScale / 20, originalScale * 4);
        Matrix4f transform = new Matrix4f();

        transform.translationRotateScale(new Vector3f(this.x + .5f, this.y + .5f, this.z + .5f), Rendering.client.getEntityRenderDispatcher().getRotation(), scale);

        buffer.vertex(transform, -.5f, -.5f, 0).color(1f, 0f, 0f, 1f);
        buffer.vertex(transform,.5f, -.5f, 0).color(1f, 0f, 0f, 1f);
        buffer.vertex(transform,.5f, .5f, 0).color(1f, 0f, 0f, 1f);
        buffer.vertex(transform,-.5f, .5f, 0).color(1f, 0f, 0f, 1f);
    }
}
