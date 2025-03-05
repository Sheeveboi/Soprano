package net.altosheeve.soprano.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
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
        if (client.player == null) return;
        if (client.world == null) return;

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        BufferBuilder buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        Vec3d camPos = client.gameRenderer.getCamera().getPos();
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate((float) -camPos.x, (float) -camPos.y, (float) -camPos.z);

        renderBox(new RenderBox(0,-60,0), buffer);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.lineWidth(1.0F);
        RenderSystem.clearColor(1, 1, 1, 1);

        modelViewStack.popMatrix();
    }
}
