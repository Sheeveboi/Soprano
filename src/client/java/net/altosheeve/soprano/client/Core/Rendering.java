package net.altosheeve.soprano.client.Core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.altosheeve.soprano.client.Nodes.Navigation;
import net.altosheeve.soprano.client.Nodes.Node;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;

public class Rendering {
    private static MinecraftClient client = MinecraftClient.getInstance();
    public static void renderBox(RenderBox box, BufferBuilder buffer) {

        //left face
        buffer.vertex(box.x, box.y, box.z).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y, box.z).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y + box.h, box.z).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x, box.y + box.w, box.z).color(box.r, box.g, box.b, box.a);

        //right face
        buffer.vertex(box.x, box.y, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y + box.h, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x, box.y + box.w, box.z + box.d).color(box.r, box.g, box.b, box.a);

        //bottom face
        buffer.vertex(box.x, box.y, box.z).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x, box.y, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y, box.z).color(box.r, box.g, box.b, box.a);

        //top face
        buffer.vertex(box.x, box.y + box.h, box.z).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x, box.y + box.h, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y + box.h, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y + box.h, box.z).color(box.r, box.g, box.b, box.a);

        //front face
        buffer.vertex(box.x, box.y, box.z).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x, box.y, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x, box.y + box.h, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x, box.y + box.h, box.z).color(box.r, box.g, box.b, box.a);

        //back face
        buffer.vertex(box.x + box.w, box.y, box.z).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y + box.h, box.z + box.d).color(box.r, box.g, box.b, box.a);
        buffer.vertex(box.x + box.w, box.y + box.h, box.z).color(box.r, box.g, box.b, box.a);
    }
    public static void render(WorldRenderContext context) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        if (client.world == null) return;
        if (Navigation.nodes.isEmpty()) return;

        //initialize rendering system
        RenderSystem.enableDepthTest();
        //RenderSystem.depthFunc(515);
        RenderSystem.enableBlend();
        //RenderSystem.defaultBlendFunc();
        //RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        //create view matrix stack
        Vec3d camPos = client.gameRenderer.getCamera().getPos();
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate((float) -camPos.x, (float) -camPos.y, (float) -camPos.z);

        BufferBuilder boxBuffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        for (Node node : Navigation.nodes) { renderBox(node, boxBuffer); }
        BufferRenderer.drawWithGlobalProgram(boxBuffer.end());

        BufferBuilder lineBuffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        for (Node node : Navigation.nodes) {
            for (int i : node.connections) {
                lineBuffer.vertex(node.x + .5f, node.y + .5f, node.z + .5f).color(1f,0f,0f,1f);
                lineBuffer.vertex(Navigation.nodes.get(i).x + .5f, Navigation.nodes.get(i).y + .5f, Navigation.nodes.get(i).z + .5f).color(1f,0f,0f,1f);
            }
        }

        if (Navigation.currentNode != null) {
            lineBuffer.vertex((float) client.player.getX(), (float) client.player.getY(), (float) client.player.getZ()).color(1f, 0f, 0f, 1f);
            lineBuffer.vertex(Navigation.currentNode.x + .5f, Navigation.currentNode.y + .5f, Navigation.currentNode.z + .5f).color(1f, 0f, 0f, 1f);
        }

        lineBuffer.vertex(0f, 0f, 0f).color(1f,0f,0f, 1f);
        lineBuffer.vertex(0f, -60f, 0f).color(1f,0f,0f,1f);

        BufferRenderer.drawWithGlobalProgram(lineBuffer.end());

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
