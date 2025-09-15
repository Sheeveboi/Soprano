package net.altosheeve.soprano.client.RenderMethods;

import net.altosheeve.soprano.client.RenderMethods.Util.RenderCircle;
import net.altosheeve.soprano.client.RenderMethods.Util.Transforms;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static java.lang.Math.*;

public class CivMap extends Screen {

    public static boolean renderMap = true;
    public static boolean panning = false;

    public static int staticWidth = 0;
    public static int staticHeight = 0;

    public static int pastMouseX = 0;
    public static int pastMouseY = 0;
    public static float mouseDelta = 0;
    public static float mouseDeltaX = 0;
    public static float mouseDeltaY = 0;

    public static float panX = 0;
    public static float panY = 0;
    public static float zoom = 1f;
    public static float zoomPower = .1f;

    public CivMap(Text title) {
        super(title);
    }

    public static void draw(BufferBuilder buffer, Matrix4f hudTransform) {

        Matrix4f mapTransform = Transforms.getMapTransform(
                panX,
                panY,
                zoom
        );

        //buffer.vertex(mapTransform, -1, -1, 0).color(1, 0, 1, 1);
        //buffer.vertex(mapTransform, 1, -1, 0).color(1, 0, 1, 1);
        //buffer.vertex(mapTransform, 1, 1, 0).color(1, 0, 1, 1);
        //buffer.vertex(mapTransform, -1, 1, 0).color(1, 0, 1, 1);

        RenderCircle mapOutline = new RenderCircle(staticWidth / 2, staticHeight / 2, 0, .8f, .8f, .8f, .9f, 90f, 91f);
        mapOutline.draw(buffer, mapTransform);
    }

    @Override
    public void init() {
        ScreenMouseEvents.beforeMouseClick(this).register((screen, x, y, d) -> panning = true);
        ScreenMouseEvents.afterMouseRelease(this).register((screen, x, y, d) -> panning = false);
        ScreenMouseEvents.beforeMouseScroll(this).register((screen, x, y, horizontal, vertical) -> {

            zoom += (float) (vertical * zoomPower);

            System.out.println(vertical);

            double xModRatio = (float) this.width / this.height;
            double yModRatio = (float) this.height / this.width;

            //get delta of original vector and scaled vector
            double modX = ((x - panX) - ((x - panX) * (zoomPower + 1))) * vertical;
            double modY = ((y - panY) - ((y - panY) * (zoomPower + 1))) * vertical;

            System.out.println("Vector X: " + (x - panX));
            System.out.println("Vector Y: " + (y - panY));

            System.out.println(modX);
            System.out.println(modY);

            panX += (float) modX;
            panY += (float) modY;

        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        mouseDelta = (float) Math.sqrt(pow(mouseX - pastMouseX, 2) + pow(mouseY - pastMouseY, 2));
        mouseDeltaX = mouseX - pastMouseX;
        mouseDeltaY = mouseY - pastMouseY;

        if (panning) {
            panX += mouseDeltaX;
            panY += mouseDeltaY;
        }

        pastMouseX = mouseX;
        pastMouseY = mouseY;

        staticWidth = this.width;
        staticHeight = this.height;

        renderMap = true;
    }
}
