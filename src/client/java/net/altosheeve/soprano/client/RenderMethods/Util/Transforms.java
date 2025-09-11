package net.altosheeve.soprano.client.RenderMethods.Util;

import net.altosheeve.soprano.client.Core.Rendering;
import net.altosheeve.soprano.client.Core.Values;
import net.altosheeve.soprano.client.RenderMethods.Waypoint;
import net.minecraft.client.render.Camera;
import org.joml.*;

import java.lang.Math;

public class Transforms {

    public static float distanceValue(float x, float y, float z) {
        Camera camera = Rendering.client.gameRenderer.getCamera();

        Vector3f directionalVector = new Vector3f(x + .5f, y - .5f, z + .5f);
        Vector3f playerPos         = new Vector3f((float) camera.getPos().x, (float) camera.getPos().y, (float) camera.getPos().z);

        return directionalVector.distance(playerPos);
    }

    public static float facingValue(float x, float y, float z) {
        Camera camera = Rendering.client.gameRenderer.getCamera();

        float pitchRad = (float) (((Rendering.client.player.getPitch() + 90) * Math.PI) / 180);
        float yawRad = (float) (((Rendering.client.player.getYaw() + 90) * Math.PI) / 180);

        Vector3f directionalVector = new Vector3f(x + .5f, y - .5f, z + .5f);
        Vector3f playerPos         = new Vector3f((float) camera.getPos().x, (float) camera.getPos().y, (float) camera.getPos().z);
        Vector3f cameraVector      = new Vector3f((float) (Math.sin(pitchRad) * Math.cos(yawRad)), (float) Math.cos(pitchRad),  (float) (Math.sin(pitchRad) * Math.sin(yawRad)));

        directionalVector.sub(playerPos).normalize();

        return directionalVector.dot(cameraVector);
    }

    public static Matrix4f getSpriteTransform(float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
        Camera camera = Rendering.client.gameRenderer.getCamera();

        Vector3f directionalVector = new Vector3f(x + .5f, y - .5f, z + .5f);
        Vector3f playerPos         = new Vector3f((float) camera.getPos().x, (float) camera.getPos().y, (float) camera.getPos().z);

        //get vector of player to waypoint
        directionalVector.sub(playerPos);

        //get unit vector of the directional vector
        directionalVector.normalize();

        //scale vector by scale factor, essentially moving the waypoint away from the player from a set distance
        directionalVector.mul(Values.globalSpriteDistance, Values.globalSpriteDistance, Values.globalSpriteDistance);

        //offset vector back to player position
        directionalVector.add(playerPos);

        Matrix4f spriteTransform = new Matrix4f();

        //execute main sprite transforms
        spriteTransform.translationRotateScale(directionalVector, camera.getRotation(), new Vector3f(scaleX, scaleY, scaleZ));

        return spriteTransform;
    }

    public static Matrix4f getShaftTransform(float x, float y, float z, float scale, Waypoint.Type type) {

        float shaftScale = Rendering.scalingFunction(scale, type, x + .5f, y - .5f, z + .5f);

        Matrix4f shaftTransform = new Matrix4f();
        shaftTransform.translationRotateScale(new Vector3f(x + .5f - shaftScale / 2, y - .5f, z + .5f - shaftScale / 2), new Quaternionf(), new Vector3f(shaftScale, 1, shaftScale));

        return shaftTransform;

    }

}
