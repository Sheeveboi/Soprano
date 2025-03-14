package net.altosheeve.soprano.client.Nodes.Screens;

import net.altosheeve.soprano.client.BetterGUI.Interactable;
import net.altosheeve.soprano.client.Nodes.Navigation;
import net.altosheeve.soprano.client.Nodes.Node;
import net.altosheeve.soprano.client.Nodes.NodeCreation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import java.io.IOException;

public class EditNodeScreen extends Screen {

    public EditNodeScreen(Text title) {
        super(title);
    }

    @Override
    public void init() {
        TextFieldWidget nodeX = new TextFieldWidget(this.textRenderer, 10, 20, 100, 20, Text.of("Node X"));
        TextFieldWidget nodeY = new TextFieldWidget(this.textRenderer, 10, 40, 100, 20, Text.of("Node Y"));
        TextFieldWidget nodeZ = new TextFieldWidget(this.textRenderer, 10, 60, 100, 20, Text.of("Node Z"));

        TextFieldWidget type = new TextFieldWidget(this.textRenderer, 10, 80, 100, 20, Text.of("Node Type"));
        TextFieldWidget name = new TextFieldWidget(this.textRenderer, 10, 100, 100, 20, Text.of("Node Name"));

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        if (Navigation.currentNode != null) {
            nodeX.setText(String.valueOf(Navigation.currentNode.x));
            nodeY.setText(String.valueOf(Navigation.currentNode.y));
            nodeZ.setText(String.valueOf(Navigation.currentNode.z));

            name.setText(Navigation.currentNode.tag);
            type.setText(Navigation.currentNode.type.toString());
        } else {

            nodeX.setText(String.valueOf(player.getBlockX()));
            nodeY.setText(String.valueOf(player.getBlockY()));
            nodeZ.setText(String.valueOf(player.getBlockZ()));

            type.setText("NORMAL");
        }
        ButtonWidget update = ButtonWidget.builder(Text.of("Update"), (widget) -> {
            if (Navigation.currentNode == null) return;

            for (Node node : Navigation.nodes) {
                if (node == Navigation.currentNode) {
                    node.x = Integer.parseInt(nodeX.getText());
                    node.y = Integer.parseInt(nodeY.getText());
                    node.z = Integer.parseInt(nodeZ.getText());

                    node.tag = name.getText();

                    try {
                        node.type = Node.NodeType.valueOf(type.getText());
                    } catch (IllegalArgumentException ignored) {
                        System.out.println(ignored);
                    }

                    break;
                }
            }

            try { NodeCreation.dumpNodes(); }
            catch (IOException e) { throw new RuntimeException(e); }
        }).dimensions(60, 120, 50, 20).build();;



        ButtonWidget add = ButtonWidget.builder(Text.of("Add"), (widget) -> {
            try {

                Node newNode = new Node(
                        player.getBlockX(),
                        player.getBlockY(),
                        player.getBlockZ(),

                        Node.NodeType.valueOf(type.getText()),
                        name.getText()
                );

                Navigation.nodes.add(newNode);

                if (Navigation.currentNode != null) {
                    newNode.connections.add(Navigation.nodes.indexOf(Navigation.currentNode));
                    Navigation.currentNode.connections.add(Navigation.nodes.indexOf(newNode));
                }

                Navigation.currentNode = newNode;

            } catch (IllegalArgumentException ignored) { System.out.println(ignored); }

            try { NodeCreation.dumpNodes(); }
            catch (IOException e) { throw new RuntimeException(e); }
        }).dimensions(10, 120, 50, 20).build();

        ButtonWidget getPlayerCoords = ButtonWidget.builder(
                Text.of("Apply Player Coords"),
                (widget) -> {
                    nodeX.setText(String.valueOf(player.getBlockX()));
                    nodeY.setText(String.valueOf(player.getBlockY()));
                    nodeZ.setText(String.valueOf(player.getBlockZ()));
                }
        ).dimensions(10, 140, 100, 20).build();

        addDrawableChild(nodeX);
        addDrawableChild(nodeY);
        addDrawableChild(nodeZ);
        addDrawableChild(name);
        addDrawableChild(type);
        addDrawableChild(update);
        addDrawableChild(add);
        addDrawableChild(getPlayerCoords);
    }
}
