package net.altosheeve.soprano.client.Core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.altosheeve.soprano.client.Networking.TypeGenerators;
import net.altosheeve.soprano.client.Nodes.Navigation;
import net.altosheeve.soprano.client.Nodes.Node;
import net.altosheeve.soprano.client.RenderMethods.Transforms;
import net.altosheeve.soprano.client.RenderMethods.Waypoint;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Rendering {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static Matrix4fStack modelViewStack;
    public static int renderTick = 0;
    public static int maxRenderTick = 100000;

    public static float scalingFunction(float scale, Waypoint.Type type, float x, float y, float z) {
        float originalScale = scale * Values.scaleRegistry(type);
        return originalScale * (float) (0.005f * (Rendering.client.player.getEyePos().distanceTo(new Vec3d(x + .5, y - .5, z + .5)) / (Math.E)));
    }

    public static void render3d(WorldRenderContext context) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        if (client.world == null) return;

        renderContext = context;

        renderTick ++;
        renderTick %= maxRenderTick;

        //initialize rendering system
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        //create view matrix stack
        Vec3d camPos = client.gameRenderer.getCamera().getPos();
        modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate((float) -camPos.x, (float) -camPos.y, (float) -camPos.z);

        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        //Soprano macro nodes
        if (!Navigation.nodes.isEmpty()) {
            BufferBuilder boxBuffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            for (Node node : Navigation.nodes) node.draw(boxBuffer);
            BufferRenderer.drawWithGlobalProgram(boxBuffer.end());

            BufferBuilder lineBuffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);

            for (Node node : Navigation.nodes) {
                for (int i : node.connections) {
                    lineBuffer.vertex(node.x + .5f, node.y + .5f, node.z + .5f).color(1f, 0f, 0f, 1f);
                    lineBuffer.vertex(Navigation.nodes.get(i).x + .5f, Navigation.nodes.get(i).y + .5f, Navigation.nodes.get(i).z + .5f).color(1f, 0f, 0f, 1f);
                }
            }

            if (Navigation.currentNode != null) {
                lineBuffer.vertex((float) client.player.getX(), (float) client.player.getY(), (float) client.player.getZ()).color(1f, 0f, 0f, 1f);
                lineBuffer.vertex(Navigation.currentNode.x + .5f, Navigation.currentNode.y + .5f, Navigation.currentNode.z + .5f).color(1f, 0f, 0f, 1f);
            }

            BufferRenderer.drawWithGlobalProgram(lineBuffer.end());
        }

        if (!Waypoint.waypoints.isEmpty()) {

            RenderSystem.disableDepthTest();

            BufferBuilder waypointBuffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            VertexConsumerProvider.Immediate textBuffer = client.getBufferBuilders().getEntityVertexConsumers();

            for (Waypoint waypoint : new ArrayList<>(Waypoint.waypoints)) {
                waypoint.drawPoint(waypointBuffer);
                waypoint.drawText(textBuffer);
                if (waypoint.importance <= 0) Waypoint.waypoints.remove(waypoint);
            }

            BufferRenderer.drawWithGlobalProgram(waypointBuffer.end());
            textBuffer.draw();

        }
        //reset rendering system
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.lineWidth(1.0F);
        RenderSystem.clearColor(1, 1, 1, 1);

        //clear view matrix stack
        modelViewStack.popMatrix();
    }

}
