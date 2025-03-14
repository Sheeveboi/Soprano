package net.altosheeve.soprano.client.Tuba;

import net.altosheeve.soprano.client.Tuba.Async.Request;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Movement extends BasicInstructions {


    public void _WALK_TO() {
        System.out.println("walking");
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        int blockX = ValueParsing._PARSE_INT(this);
        int blockY = ValueParsing._PARSE_INT(this);
        int blockZ = ValueParsing._PARSE_INT(this);

        double dx = player.getX() - blockX - .5;
        double dy = player.getY() - blockY;
        double dz = player.getZ() - blockZ - .5;

        double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);

        dx /= dist;
        dy /= dist;
        dz /= dist;

        int toleranceNumerator = ValueParsing._PARSE_INT(this);
        int toleranceDenominator = ValueParsing._PARSE_INT(this);

        System.out.println(dx);
        System.out.println(dy);
        System.out.println(dz);

        float pitch = (float) Math.asin(-dy);
        float yaw = (float) Math.atan2(dz, dx);

        pitch = (float) (pitch * 180.0 / Math.PI);
        yaw = (float) (yaw * 180.0 / Math.PI);

        yaw += 90;

        System.out.println(pitch);
        System.out.println(yaw);

        double tolerance = (double) toleranceNumerator / (double) toleranceDenominator;

        player.setPitch(pitch);
        player.setYaw(yaw);

        this.addRequest(new Request(() -> {
            System.out.println(player.getPos().distanceTo(new Vec3d(blockX + .5, blockY + .5, blockZ + .5)));
            boolean out = player.getPos().distanceTo(new Vec3d(blockX + .5, blockY + .5, blockZ + .5)) < tolerance;
            MinecraftClient.getInstance().options.forwardKey.setPressed(!out);

            return out;
        }));
    }

    public Movement(ArrayList<Byte> program) {
        super(program);
        this.registerInstruction((byte) 0x0, this::_WALK_TO);
    }
}
