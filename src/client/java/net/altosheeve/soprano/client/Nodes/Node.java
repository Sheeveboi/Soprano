package net.altosheeve.soprano.client.Nodes;

import net.altosheeve.soprano.client.Core.RenderBox;

import java.util.ArrayList;

public class Node extends RenderBox {

    public enum NodeType {
        NORMAL,
        ICEROAD,
        DOOR,
        INTERACTABLE
    }

    public String tag;
    public ArrayList<Integer> connections;
    public NodeType type;

    public void setColor() {
        this.a = .3f;
        switch (this.type) {
            case NORMAL -> {
                this.r = 0;
                this.g = 0;
                this.b = 1;
            }

            case ICEROAD -> {
                this.r = .5f;
                this.g = .5f;
                this.b = 1f;
            }

            case DOOR -> {
                this.r = 0f;
                this.g = 1f;
                this.b = 0f;
            }

            case INTERACTABLE -> {
                this.r = 1f;
                this.g = 0f;
                this.b = 0f;
            }
        }
    }

    public Node(int x, int y, int z) {
        super(x, y, z);
        this.connections = new ArrayList<>();
    }

    public Node(int x, int y, int z, NodeType type) {
        super(x, y, z);
        this.type = type;
        this.setColor();
        this.connections = new ArrayList<>();
    }

    public Node(int x, int y, int z, NodeType type, String tag) {
        super(x, y, z);
        this.type = type;
        this.setColor();
        this.tag = tag;
        this.connections = new ArrayList<>();
    }

    public Node(int x, int y, int z, NodeType type, String tag, ArrayList<Integer> connections) {
        super(x, y, z);
        this.type = type;
        this.setColor();
        this.tag = tag;
        this.connections = connections;
    }
}
