package net.altosheeve.soprano.client.Tuba;

import net.altosheeve.soprano.client.Nodes.Navigation;
import net.altosheeve.soprano.client.Nodes.Node;
import net.altosheeve.soprano.client.Tuba.Async.Request;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Movement extends BasicInstructions {

    public void _CALIBRATE() {
        System.out.println("Calibrating");

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        String targetTag = Values._PARSE_STRING(this);
        int toleranceNumerator = Values._PARSE_INT(this);
        int toleranceDenominator = Values._PARSE_INT(this);

        double tolerance = (double) toleranceNumerator / toleranceDenominator;

        Optional<Node> testing = Navigation.nodes.stream().filter(node -> Objects.equals(targetTag, node.tag)).findFirst();
        if (testing.isEmpty()) return;

        Node targetNode = testing.get();

        boolean left = player.getX() < targetNode.x;
        boolean forward = player.getZ() < targetNode.z;

        player.setYaw(0);

        this.addRequest(new Request(() -> {
            boolean xFufilled = Math.abs(player.getX() - targetNode.x - .5) < tolerance;
            boolean zFufilled = Math.abs(player.getZ() - targetNode.z - .5) < tolerance;

            if (!zFufilled) {
                if (forward) client.options.forwardKey.setPressed(true);
                else client.options.backKey.setPressed(true);
            }

            if (zFufilled && !xFufilled) {
                client.options.forwardKey.setPressed(false);
                client.options.backKey.setPressed(false);

                if (left) client.options.leftKey.setPressed(true);
                else client.options.rightKey.setPressed(true);
            }

            client.options.sneakKey.setPressed(true);
            if (xFufilled && zFufilled) {
                client.options.sneakKey.setPressed(false);
                client.options.leftKey.setPressed(false);
                client.options.rightKey.setPressed(false);
                Navigation.currentNode = targetNode;
            }

            return xFufilled && zFufilled;
        }));
    }

    public void _WALK_TO() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        int blockX = Values._PARSE_INT(this);
        int blockY = Values._PARSE_INT(this);
        int blockZ = Values._PARSE_INT(this);

        double dx = player.getX() - blockX - .5;
        double dy = player.getY() - blockY;
        double dz = player.getZ() - blockZ - .5;

        double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);

        dx /= dist;
        dy /= dist;
        dz /= dist;

        int toleranceNumerator = Values._PARSE_INT(this);
        int toleranceDenominator = Values._PARSE_INT(this);

        float pitch = (float) Math.asin(-dy);
        float yaw = (float) Math.atan2(dz, dx);

        pitch = (float) (pitch * 180.0 / Math.PI);
        yaw = (float) (yaw * 180.0 / Math.PI);

        yaw += 90;

        double tolerance = (double) toleranceNumerator / (double) toleranceDenominator;

        player.setPitch(pitch);
        player.setYaw(yaw);

        final boolean[] firstTick = {true};
        this.addRequest(new Request(() -> {
            boolean out = player.getPos().distanceTo(new Vec3d(blockX + .5, blockY + .5, blockZ + .5)) < tolerance;
            MinecraftClient.getInstance().options.forwardKey.setPressed(!out);
            if (!firstTick[0]) Navigation.handler.cb();
            firstTick[0] = false;

            return out;
        }));
    }

    public void _PATH_TO() {
        String targetTag = Values._PARSE_STRING(this);
        int toleranceNumerator = Values._PARSE_INT(this);
        int toleranceDenominator = Values._PARSE_INT(this);

        int origin = this.programPointer + 1;
        for (int i : Objects.requireNonNull(Navigation.generatePathingIttinerary(targetTag))) {
            Node node = Navigation.nodes.get(i);

            this.addInstruction((byte) 0x3, origin); origin++; //set as target node
            this.addInstruction((byte) i, origin); origin++; //index of node

            switch (node.type) {
                case NORMAL:
                    this.addInstruction((byte) 0x5, origin); origin++; //set basic movement handler
                    this.addInstruction((byte) 8, origin); origin++; //apply velocity threshold
                    this.addInstruction((byte) 100, origin); origin++;
                    break;
                case DOOR:
                    this.addInstruction((byte) 0x6, origin); origin++; //set basic movement handler
                    this.addInstruction((byte) 3, origin); origin++; //apply door threshold
                    this.addInstruction((byte) 1, origin); origin++;
                    this.addInstruction((byte) 8, origin); origin++; //apply velocity threshold
                    this.addInstruction((byte) 100, origin); origin++;
                    break;
                case ICEROAD:
                    this.addInstruction((byte) 0x7, origin); origin++; //set basic movement handler
                    this.addInstruction((byte) 8, origin); origin++; //apply velocity threshold
                    this.addInstruction((byte) 100, origin); origin++;
                    break;
            }

            this.addInstruction((byte) 0x1, origin); origin++; //walk to
            this.addInstruction((byte) node.x, origin); origin++;
            this.addInstruction((byte) node.y, origin); origin++;
            this.addInstruction((byte) node.z, origin); origin++;
            this.addInstruction((byte) toleranceNumerator, origin); origin++;
            this.addInstruction((byte) toleranceDenominator, origin); origin++;
            this.addInstruction((byte) 0x4, origin); origin++; //set as current node
            this.addInstruction((byte) i, origin); origin++;
        }
    }

    public void _SET_CURRENT_NODE() {
        int index = Values._PARSE_INT(this);
        Navigation.currentNode = Navigation.nodes.get(index);
    }

    public void _SET_TARGET_NODE() {
        int index = Values._PARSE_INT(this);
        Navigation.targetNode = Navigation.nodes.get(index);
    }

    public void _SET_BASIC_MOVEMENT_HANDLER() {
        int numerator = Values._PARSE_INT(this);
        int denominator = Values._PARSE_INT(this);
        Navigation.velocityThreshold = (double) numerator / denominator;
        Navigation.handler = Navigation::basicWalkHandler;
    }

    public void _SET_DOOR_HANDLER() {
        int numerator = Values._PARSE_INT(this);
        int denominator = Values._PARSE_INT(this);
        Navigation.doorThreshold = (double) numerator / denominator;
        numerator = Values._PARSE_INT(this);
        denominator = Values._PARSE_INT(this);
        Navigation.velocityThreshold = (double) numerator / denominator;
        Navigation.handler = Navigation::doorHandler;
    }

    public void _SET_ICEROAD_HANDLER() {
        float numerator = Values._PARSE_INT(this);
        float denominator = Values._PARSE_INT(this);
        Navigation.velocityThreshold = (double) numerator / denominator;
        Navigation.handler = Navigation::iceroadHandler;
    }

    public Movement(ArrayList<Byte> program) {
        super(program);
        this.registerInstruction((byte) 0x0, this::_CALIBRATE);
        this.registerInstruction((byte) 0x1, this::_WALK_TO);
        this.registerInstruction((byte) 0x2, this::_PATH_TO);
        this.registerInstruction((byte) 0x3, this::_SET_TARGET_NODE);
        this.registerInstruction((byte) 0x4, this::_SET_CURRENT_NODE);
        this.registerInstruction((byte) 0x5, this::_SET_BASIC_MOVEMENT_HANDLER);
        this.registerInstruction((byte) 0x6, this::_SET_DOOR_HANDLER);
        this.registerInstruction((byte) 0x7, this::_SET_ICEROAD_HANDLER);
    }
}
