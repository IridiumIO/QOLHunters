//package io.iridium.qolhunters.mixin.elixirrenderer;
//
//import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
//import iskallia.vault.entity.entity.ElixirOrbEntity;
//import iskallia.vault.network.message.SummonElixirOrbMessage;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@Mixin(value = SummonElixirOrbMessage.class, remap = false)
//public class MixinSummonElixirOrbMessage {
//
//
//    @Unique private static int RENDERED_ORBS = 0;
//
//    @Unique private static final ScheduledExecutorService KILL_SCHEDULER = Executors.newScheduledThreadPool(1);
//
//    @Unique private static final Queue<ElixirOrbEntity> ORB_QUEUE = new LinkedList<>();
//
//
//    @OnlyIn(Dist.CLIENT)
//    @Inject(method="spawnElixirOrb", at=@At("HEAD"), cancellable = true)
//    private static void spawnElixirOrb(SummonElixirOrbMessage message, CallbackInfo ci) {
//
//            ClientLevel world = Minecraft.getInstance().level;
//
//            if (RENDERED_ORBS >= QOLHuntersClientConfigs.ELIXIR_ORB_CULLING.get().getCount()) {
//                // Kill the oldest orb
//                ElixirOrbEntity oldestOrb = ORB_QUEUE.poll();
//                if (oldestOrb != null) {
//                    oldestOrb.kill();
//                    RENDERED_ORBS--;
//                }
//            }
//
//
//            // Spawn the new orb
//            ElixirOrbEntity entity = new ElixirOrbEntity(world, message.getX(), message.getY(), message.getZ(), message.getSize(), message.getAge());
//
//            entity.setPacketCoordinates(message.getX(), message.getY(), message.getZ());
//            entity.setYRot(0.0F);
//            entity.setXRot(0.0F);
//            entity.setId(message.getId());
//            world.putNonPlayerEntity(message.getId(), entity);
//
//            // Add the new orb to the queue
//            ORB_QUEUE.add(entity);
//            RENDERED_ORBS++;
//
//            // Schedule the decrement after 4 seconds
//            KILL_SCHEDULER.schedule(() -> {
//                if (ORB_QUEUE.remove(entity)) {
//                    RENDERED_ORBS--;
//                }
//            }, 4, TimeUnit.SECONDS);
//
//            ci.cancel();
//    }
//
//}
