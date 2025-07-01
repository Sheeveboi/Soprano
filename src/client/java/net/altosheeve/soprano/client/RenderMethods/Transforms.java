package net.altosheeve.soprano.client.RenderMethods;

import net.altosheeve.soprano.client.Core.Rendering;
import net.altosheeve.soprano.client.Core.Values;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transforms {

    public static Matrix4f getSpriteTransform(float x, float y, float z, float scale) {
        Vector3f directionalVector = new Vector3f(x + .5f, y - .5f, z + .5f);
        Vector3f playerPos = new Vector3f((float) Rendering.client.gameRenderer.getCamera().getPos().x, (float) Rendering.client.gameRenderer.getCamera().getPos().y, (float) Rendering.client.gameRenderer.getCamera().getPos().z);

        directionalVector.sub(playerPos);
        //get vector of player to waypoint

        float dist = directionalVector.distance(0, 0, 0);
        directionalVector.div(dist, dist, dist);
        //get unit vector of the directional vector

        directionalVector.mul(Values.scaleThreshold, Values.scaleThreshold, Values.scaleThreshold);
        //scale vector by scale factor, essentially moving the waypoint away from the player from a set distance

        directionalVector.add(playerPos);
        //offset vector back to player position

        Matrix4f spriteTransform = new Matrix4f();
        spriteTransform.translationRotateScale(directionalVector, Rendering.client.getEntityRenderDispatcher().getRotation(), Values.waypointScale);

        return spriteTransform;
    }

    public static Matrix4f getShaftTransform(float x, float y, float z, float scale, Waypoint.Type type) {

        float shaftScale = Rendering.scalingFunction(scale, type, x + .5f, y - .5f, z + .5f);

        Matrix4f shaftTransform = new Matrix4f();
        shaftTransform.translationRotateScale(new Vector3f(x + .5f - shaftScale / 2, y - .5f, z + .5f - shaftScale / 2), new Quaternionf(), new Vector3f(shaftScale, 1, shaftScale));

        return shaftTransform;

    }

}
