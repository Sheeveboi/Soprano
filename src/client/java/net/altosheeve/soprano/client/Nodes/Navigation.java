package net.altosheeve.soprano.client.Nodes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.util.*;

public class Navigation {
    public static ArrayList<Node> nodes = new ArrayList<>();
    public static Node currentNode;
    public static double velocityThreshold;

    public static Handler handler;

    public interface Handler {
        void cb();
    }

    public static void basicWalkHandler() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        double velocity = player.getVelocity().length();

        System.out.println(velocity);

        client.options.useKey.setPressed(false);
        client.options.jumpKey.setPressed(false);

        if (velocity > velocityThreshold) return;

        System.out.println("handling");

        client.options.useKey.setPressed(true);
        client.options.jumpKey.setPressed(true);
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
                if (!searched.contains(i)) {
                    searched.add(i);
                    out.add(i);
                    testNode = nodes.get(i);
                    deadEnd = false;
                }
            }

            //if all the nodes in the testNode's connections have been tested already, jump back one node
            if (deadEnd) {
                out.removeLast();
                testNode = nodes.get(out.getLast());
            }
        }

        return out;
    }
}
