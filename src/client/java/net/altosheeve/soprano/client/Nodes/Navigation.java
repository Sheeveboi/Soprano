package net.altosheeve.soprano.client.Nodes;

import net.altosheeve.soprano.client.Core.Rendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.*;

import static java.lang.Math.abs;

public class Navigation {
    public static ArrayList<Node> nodes = new ArrayList<>();
    public static Node currentNode;
    public static Node targetNode;
    public static double velocityThreshold;
    public static double interactionThreshold;
    public static int tick;

    public static Handler handler;

    public interface Handler {
        void cb();
    }

    public static void resetControls() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        client.options.useKey.setPressed(false);
        client.options.jumpKey.setPressed(false);
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);
        client.options.forwardKey.setPressed(false);
        client.options.backKey.setPressed(false);
        client.options.attackKey.setPressed(false);
        client.options.sprintKey.setPressed(false);
    }

    public static void basicWalkHandler() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        double velocity = player.getVelocity().length();

        client.options.useKey.setPressed(false);
        client.options.jumpKey.setPressed(false);

        if (velocity > velocityThreshold) return;

        client.options.useKey.setPressed(true);
        client.options.jumpKey.setPressed(true);
    }

    public static void doorHandler() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        double velocity = player.getVelocity().length();

        if (player.getPos().distanceTo(new Vec3d(targetNode.x + .5, targetNode.y + .5, targetNode.z + .5)) < interactionThreshold) {
            client.options.useKey.setPressed(true);
        } else {
            client.options.useKey.setPressed(false);
        }

        if (velocity > velocityThreshold) return;

        double dx = player.getX() - targetNode.x - .5;
        double dz = player.getZ() - targetNode.z - .5;

        double dist = Math.sqrt(dx*dx + dz*dz);

        dx /= dist;
        dz /= dist;

        float yaw = (float) Math.atan2(dz, dx);

        boolean direction = player.getYaw() - yaw < 0;

        client.options.rightKey.setPressed(false);
        client.options.leftKey.setPressed(false);

        if (direction) client.options.rightKey.setPressed(true);
        else client.options.leftKey.setPressed(true);
    }

    public static void iceroadHandler() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        if (tick % 2 == 0) client.options.jumpKey.setPressed(true);
        else client.options.jumpKey.setPressed(false);
        client.options.sprintKey.setPressed(true);
        client.options.rightKey.setPressed(false);
        client.options.leftKey.setPressed(false);

        Vector3f idealVector = new Vector3f(targetNode.x, targetNode.y, targetNode.z).sub(currentNode.x, currentNode.y, currentNode.z);
        Vector3f idealNormal = new Vector3f(idealVector.z, idealVector.y, -idealVector.x);

        float innacuracy = idealVector.dot(player.getVelocity().toVector3f());

        if (abs(innacuracy) < .9) {

            Vector3f currentVector = new Vector3f((float) player.getX(), (float) player.getY(), (float) player.getZ()).sub(currentNode.x, currentNode.y, currentNode.z);
            float deviation = idealNormal.dot(currentVector);

            if (deviation > 0) client.options.rightKey.setPressed(true);
            else client.options.leftKey.setPressed(true);
        }

        if (player.getVelocity().length() < velocityThreshold) {
            double dx = player.getX() - targetNode.x - .5;
            double dz = player.getZ() - targetNode.z - .5;

            double dist = Math.sqrt(dx*dx + dz*dz);

            dx /= dist;
            dz /= dist;

            float yaw = (float) Math.atan2(dz, dx);

            boolean direction = player.getYaw() - yaw < 0;

            if (direction) client.options.rightKey.setPressed(true);
            else client.options.leftKey.setPressed(true);
        }

        if (player.getHungerManager().getFoodLevel() < 17) {
            if (!Objects.equals(player.getInventory().getStack(0).getItemName().getString(), "Baked Potato")) {
                for (int slot = 0; slot < 36; slot++) {
                    if (Objects.equals(player.getInventory().getStack(slot).getItemName().getString(), "Baked Potato")) {
                        player.getInventory().setSelectedSlot(0);
                        player.getInventory().swapSlotWithHotbar(slot);
                        break;
                    }
                }
            }
            client.options.useKey.setPressed(true);
        }
    }

    public static void interactionHandler() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        double velocity = player.getVelocity().length();

        if (player.getPos().distanceTo(new Vec3d(targetNode.x + .5, targetNode.y + .5, targetNode.z + .5)) < interactionThreshold) {

            System.out.println("entering interactable");

            Camera camera = Rendering.client.gameRenderer.getCamera();

            double dx = camera.getPos().x - targetNode.x - .5;
            double dy = camera.getPos().y - targetNode.y - 2.5;
            double dz = camera.getPos().z - targetNode.z - .5;

            double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);

            dx /= dist;
            dy /= dist;
            dz /= dist;

            float pitch = (float) Math.asin(-dy);
            float yaw = (float) Math.atan2(dz, dx);

            pitch = (float) (pitch * 180.0 / Math.PI);
            yaw = (float) (yaw * 180.0 / Math.PI) + 90;

            player.setPitch(pitch);
            player.setYaw(yaw);

            client.options.useKey.setPressed(true);


        } else {
            client.options.useKey.setPressed(false);
        }

        if (velocity > velocityThreshold) return;

        double dx = player.getX() - targetNode.x - .5;
        double dz = player.getZ() - targetNode.z - .5;

        double dist = Math.sqrt(dx*dx + dz*dz);

        dx /= dist;
        dz /= dist;

        float yaw = (float) Math.atan2(dz, dx);

        boolean direction = player.getYaw() - yaw < 0;

        client.options.rightKey.setPressed(false);
        client.options.leftKey.setPressed(false);

        if (direction) client.options.rightKey.setPressed(true);
        else client.options.leftKey.setPressed(true);
    }

    public static ArrayList<Integer> generatePathingIttinerary(String nodeTag) {
        ArrayList<Integer> out = new ArrayList<>();

        Optional<Node> testing = nodes.stream().filter(node -> Objects.equals(nodeTag, node.tag)).findFirst();
        if (testing.isEmpty()) return null;

        Node targetNode = testing.get();
        Node testNode = currentNode;

        ArrayList<Integer> searched = new ArrayList<>();

        class key implements Comparator {
            @Override
            public int compare(Object o1, Object o2) {
                Node node1 = nodes.get((Integer) o1);
                Node node2 = nodes.get((Integer) o2);

                double node1Dist = Math.sqrt(
                        Math.pow(targetNode.x - node1.x , 2) +
                        Math.pow(targetNode.y - node1.y , 2) +
                        Math.pow(targetNode.z - node1.z , 2)
                );

                double node2Dist = Math.sqrt(
                        Math.pow(targetNode.x - node2.x , 2) +
                        Math.pow(targetNode.y - node2.y , 2) +
                        Math.pow(targetNode.z - node2.z , 2)
                );

                if (node1Dist > node2Dist) return 1;
                return -1;
            }
        }

        while (testNode != targetNode) {

            //get next closest node from the current testNode and set that node as the textNode
            ArrayList<Integer> sorted = testNode.connections;
            sorted.sort(new key());

            boolean deadEnd = true;

            for (int i : sorted) {
                Node instance = nodes.get(i);
                if (!searched.contains(i)) {
                    if (instance.type != Node.NodeType.INTERACTABLE || instance == targetNode) {
                        searched.add(i);
                        out.add(i);
                        testNode = instance;
                        deadEnd = false;
                        break;
                    }
                }
            }

            //if all the nodes in the testNode's connections have been tested already, jump back one node
            if (deadEnd) {
                out.removeLast();
                testNode = nodes.get(out.getLast());
            }
        }

        System.out.println(out);

        return out;
    }
}
