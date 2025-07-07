package net.altosheeve.soprano.client.RenderMethods;

import net.altosheeve.soprano.client.Core.Rendering;
import net.altosheeve.soprano.client.Core.Values;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Objects;

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
    public boolean overworld;

    public String uuid;
    public String username;

    public static void updateWaypoint(float x, float y, float z, Type type, String name, boolean overworld) {
        for (Waypoint waypoint : waypoints) {
            if (Objects.equals(waypoint.uuid, name)) {
                waypoint.type = type;

                waypoint.x = x;
                waypoint.y = y;
                waypoint.z = z;

                waypoint.importance = Values.importanceRegistry(type);
                waypoint.decayRate = Values.decayRateRegistry(type);

                waypoint.overworld = overworld;

                return;
            }
        }

        waypoints.add(new Waypoint(x, y, z,type, name));

    }

    public Waypoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.type = Type.GOOD_GUY;
        this.importance = 1;
        this.decayRate = 0;
    }

    public Waypoint(float x, float y, float z, Type type) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.type = type;
        this.importance = 1;
        this.decayRate = 0;
    }

    public Waypoint(float x, float y, float z, Type type, String name) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.type = type;
        this.importance = Values.importanceRegistry(type);
        this.decayRate = Values.decayRateRegistry(type);;
        this.uuid = name;
    }

    public void drawGoodGuy(BufferBuilder buffer, Matrix4f spriteTransform) {
        RenderCircle outerOutline = new RenderCircle(0,0,0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance, .8f, 1);
        RenderCircle innerCircle  = new RenderCircle(0,0,0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance, 0, .6f);

        outerOutline.draw(buffer, spriteTransform);
        innerCircle.draw(buffer, spriteTransform);
    }

    public void drawNormal(BufferBuilder buffer, Matrix4f spriteTransform) {
        RenderCircle outerOutline = new RenderCircle(0,0,0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance, .9f, 1);
        RenderCircle innerCircle  = new RenderCircle(0,0,0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance, 0, .4f);

        outerOutline.draw(buffer, spriteTransform);
        innerCircle.draw(buffer, spriteTransform);
    }

    public void drawShitter(BufferBuilder buffer, Matrix4f spriteTransform) {
        RenderCircle outerOutline = new RenderCircle(0,0,0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance, .5f, 1);

        outerOutline.draw(buffer, spriteTransform);
    }

    public void drawHitler(BufferBuilder buffer, Matrix4f spriteTransform) {
        RenderCircle outerOutline = new RenderCircle(0,0,0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance, 0, 1);

        outerOutline.draw(buffer, spriteTransform);
    }

    public void drawSnitch(BufferBuilder buffer, Matrix4f spriteTransform) {
        buffer.vertex(spriteTransform, 0, .999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, 0, .999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, .999f, -.999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -.999f, -.999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
    }

    public void drawSnitchAlert(BufferBuilder buffer, Matrix4f spriteTransform) {
        buffer.vertex(spriteTransform, 0, .999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, 0, .999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, .999f, -.999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -.999f, -.999f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
    }

    public void drawPing(BufferBuilder buffer, Matrix4f spriteTransform) {
        RenderCircle firstRing  = new RenderCircle(0, 0, 0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], 1, 1, .80f);
        RenderCircle secondRing = new RenderCircle(0, 0, 0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], 1, .60f, .40f);

        firstRing.draw(buffer, spriteTransform);
        secondRing.draw(buffer, spriteTransform);
    }

    public void drawAlert(BufferBuilder buffer, Matrix4f spriteTransform) {
        RenderCircle firstRing  = new RenderCircle(0, 0, 0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], 1, 1, .80f);
        RenderCircle secondRing = new RenderCircle(0, 0, 0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], 1, .60f, .40f);
        RenderCircle thirdRing  = new RenderCircle(0, 0, 0, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], 1, .20f, 0);

        firstRing.draw(buffer, spriteTransform);
        secondRing.draw(buffer, spriteTransform);
        thirdRing.draw(buffer, spriteTransform);
    }

    public void drawPermanent(BufferBuilder buffer, Matrix4f spriteTransform) {
        buffer.vertex(spriteTransform, 1, 1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, .8f, .8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -.8f, .8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -1, 1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);

        buffer.vertex(spriteTransform, 1, 1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, .8f, .8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, .8f, -.8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, 1, -1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);

        buffer.vertex(spriteTransform, -1, 1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -.8f, .8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -.8f, -.8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -1, -1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);

        buffer.vertex(spriteTransform, 1, -1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, .8f, -.8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -.8f, -.8f, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
        buffer.vertex(spriteTransform, -1, -1, 0).color(Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], importance);
    }

    public void drawShaft(BufferBuilder buffer, Matrix4f shaftTransform) {
        RenderBox shaft = new RenderBox(0, -500, 0, 1, 9000, 1, Values.waypointRegistry(this.type)[0], Values.waypointRegistry(this.type)[1], Values.waypointRegistry(this.type)[2], this.importance / 2);
        shaft.draw(buffer, shaftTransform);
    }

    public void drawPoint(BufferBuilder buffer) {

        this.importance -= decayRate;

        Matrix4f spriteTransform = Transforms.getSpriteTransform(this.x, this.y, this.z, Values.waypointScale, Values.waypointScale, Values.waypointScale);
        Matrix4f shaftTransform = Transforms.getShaftTransform(this.x, this.y, this.z, Values.shaftScale, this.type);

        drawShaft(buffer, shaftTransform);

        switch (this.type) {
            case GOOD_GUY -> drawGoodGuy(buffer, spriteTransform);
            case NORMAL   -> drawNormal(buffer, spriteTransform);
            case SHITTER  -> drawShitter(buffer, spriteTransform);
            case HITLER   -> drawHitler(buffer, spriteTransform);

            case SNITCH         -> drawSnitch(buffer, spriteTransform);
            case SNITCH_ALERT   -> drawSnitchAlert(buffer, spriteTransform);

            case PING  -> drawPing(buffer, spriteTransform);
            case ALERT -> drawAlert(buffer, spriteTransform);

            case PERMANENT -> drawPermanent(buffer, spriteTransform);
        }


    }
}
