package net.altosheeve.soprano.client.Nodes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NodeCreation {
    private static final File nodeFile = new File("nodeFile.NODE");

    public static void selectNode() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        for (Node node : Navigation.nodes) {
            if (node.x == player.getBlockX() && node.y == player.getBlockY() && node.z == player.getBlockZ()) {
                Navigation.currentNode = node;
                return;
            }
        }
        Navigation.currentNode = null; //unselect
    }

    public static void connectNode() throws IOException {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        assert player != null;
        if (Navigation.currentNode == null) return;

        for (Node node : Navigation.nodes) {
            if (node.x == player.getBlockX() && node.y == player.getBlockY() && node.z == player.getBlockZ()) {

                if (Navigation.currentNode.connections.contains(Navigation.nodes.indexOf(node)) || node.connections.contains(Navigation.nodes.indexOf(Navigation.currentNode))) {
                    node.connections.remove((Object) Navigation.nodes.indexOf(Navigation.currentNode));
                    Navigation.currentNode.connections.remove((Object) Navigation.nodes.indexOf(node));
                    //these need to be cast to Object, because the remove function will treat these like actual indexes instead

                    break;
                }

                node.connections.add(Navigation.nodes.indexOf(Navigation.currentNode));
                Navigation.currentNode.connections.add(Navigation.nodes.indexOf(node));

                break;
            }
        }

        dumpNodes();
    }

    public static void dumpNodes() throws IOException {
        if (!nodeFile.exists()) {
            nodeFile.createNewFile();
            return;
        }

        JSONArray nodesArray = new JSONArray();

        for (Node node : Navigation.nodes) {
            JSONObject nodeJson = new JSONObject();

            nodeJson.put("x", node.x);
            nodeJson.put("y", node.y);
            nodeJson.put("z", node.z);

            nodeJson.put("tag", node.tag);
            nodeJson.put("type", node.type);
            nodeJson.put("connections", node.connections);

            nodesArray.put(nodeJson);
        }

        JSONObject nodesJson = new JSONObject();
        nodesJson.put("nodes", nodesArray);

        FileWriter writer = new FileWriter("nodeFile.NODE");
        writer.write(nodesJson.toString());
        writer.close();
    }

    public static void loadNodes() throws IOException {
        if (!nodeFile.exists()) {
            nodeFile.createNewFile();
            return;
        }

        Scanner scanner = new Scanner(nodeFile);
        StringBuilder jsonString = new StringBuilder();
        while (scanner.hasNextLine()) jsonString.append(scanner.nextLine());

        if (jsonString.isEmpty()) return;

        JSONObject nodesJson = new JSONObject(jsonString.toString());
        JSONArray nodes = nodesJson.getJSONArray("nodes");

        for (int i = 0; i < nodes.length(); i++) {
            Node newNode = new Node(0, 0, 0);
            JSONObject nodeJson = nodes.getJSONObject(i);

            newNode.x = nodeJson.getInt("x");
            newNode.y = nodeJson.getInt("y");
            newNode.z = nodeJson.getInt("z");

            newNode.type = Node.NodeType.valueOf(nodeJson.getString("type"));
            newNode.tag = nodeJson.getString("tag");

            for (Object connection : nodeJson.getJSONArray("connections").toList()) newNode.connections.add((int) connection);

            newNode.setColor();

            Navigation.nodes.add(newNode);
        }
    }


}
