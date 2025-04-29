package io.iridium.qolhunters.features.virtualdehammerizer;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.DehammerizerConfig;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.tool.ToolItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

import static io.iridium.qolhunters.util.SharedFunctions.displayMessageOnScreen;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class VirtualDehammerizer {


    private static int currentDehammerizerIndex = 1;


    private static boolean sanityCheck(Player player, Item checkItem){

        if(player == null) return false;
        if(player.level.dimension() != Level.OVERWORLD) return false;

        if(!QOLHuntersClientConfigs.ENABLE_VIRTUAL_DEHAMMERIZER.get()) return false;

        ItemStack heldItem = player.getMainHandItem();
        Item item = heldItem.getItem();
        if (item != checkItem) return false;

        return true;

    }


    @SubscribeEvent
    public static synchronized void onKeyInput(InputEvent.KeyInputEvent event) {

        if(!sanityCheck(Minecraft.getInstance().player, Items.WARPED_FUNGUS_ON_A_STICK)) return;

        if (event.getKey() == GLFW.GLFW_KEY_UP && (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_REPEAT) && currentDehammerizerIndex < 25) {
            currentDehammerizerIndex++;
        }
        if (event.getKey() == GLFW.GLFW_KEY_DOWN && (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_REPEAT) && currentDehammerizerIndex > 1) {
            currentDehammerizerIndex--;
        }


        if (event.getKey() == GLFW.GLFW_KEY_DELETE && event.getAction() == GLFW.GLFW_PRESS && event.getModifiers() == GLFW.GLFW_MOD_CONTROL) {
            String saveName = DehammerizerConfig.getSaveName();
            if(QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.containsKey(saveName)){
                QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.get(saveName).remove(currentDehammerizerIndex);
                QOLHunters.DEHAMMERIZER_CONFIG.save();
            }
        }

    }



    @SubscribeEvent
    public static synchronized void onItemUse(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getPlayer();
        if(!sanityCheck(player, Items.WARPED_FUNGUS_ON_A_STICK)) return;

        if(!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)) return;

        DehammerizerConfig.Coordinates coordinates = new DehammerizerConfig.Coordinates(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());

        saveDehammerizer(currentDehammerizerIndex, coordinates);


    }


    @SubscribeEvent
    public static synchronized void whileHoldingDehammerizer(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if(!sanityCheck(player, Items.WARPED_FUNGUS_ON_A_STICK)) return;

        DehammerizerConfig.Coordinates coordinates = getDehammerizer(currentDehammerizerIndex);

        String coordinatesString = coordinates == null ? "[Use Ctrl + Right Click to set]" : "[" + coordinates.x + ", " + coordinates.y + ", " + coordinates.z + "]";
        MutableComponent displayComponent = new TextComponent("Dehammerizer ").withStyle(ChatFormatting.WHITE).append(new TextComponent(String.valueOf(currentDehammerizerIndex)).withStyle(ChatFormatting.GREEN)).append(new TextComponent(": " + coordinatesString).withStyle(ChatFormatting.GRAY));

        if(currentDehammerizerIndex == 1){
            displayComponent = displayComponent.append(new TextComponent(" ▲  ").withStyle(ChatFormatting.WHITE));
        } else if (currentDehammerizerIndex == 5) {
            displayComponent = displayComponent.append(new TextComponent(" ▼  ").withStyle(ChatFormatting.WHITE));
        }else {
            displayComponent = displayComponent.append(new TextComponent(" ▲ ▼").withStyle(ChatFormatting.WHITE));
        }

        displayMessageOnScreen(displayComponent);
        if(coordinates == null) return;

        renderParticleRing(player, coordinates, QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_RANGE.get(), 30* QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_RANGE.get());


    }



    @SubscribeEvent
    public static void onBlockStartBreak(InputEvent.ClickInputEvent event) {

        if (!event.isAttack()) return;

        Player player = Minecraft.getInstance().player;
        if(player.level.dimension() != Level.OVERWORLD) return;

        if(!QOLHuntersClientConfigs.ENABLE_VIRTUAL_DEHAMMERIZER.get()) return;

        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof ToolItem)) return;


        VaultGearData data = VaultGearData.read(heldItem);
        if(!data.has(ModGearAttributes.HAMMERING, VaultGearData.Type.ALL_MODIFIERS)) return;
        Integer radius = QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_RANGE.get();


        int playerX = player.blockPosition().getX();
        int playerY = player.blockPosition().getY();
        int playerZ = player.blockPosition().getZ();
        boolean isCylinderMode = QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_MODE.get() == QOLHuntersClientConfigs.VirtualDehammerizerMode.CYLINDER;
        double radiusSquared = radius * radius;


        for(Map<Integer, DehammerizerConfig.Coordinates> dehammerizer : QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.values()){
            for(DehammerizerConfig.Coordinates coordinates : dehammerizer.values()){

                int y = isCylinderMode ? playerY : coordinates.y;

                double dx = playerX - coordinates.x;
                double dy = playerY - y;
                double dz = playerZ - coordinates.z;
                double distSquared = dx * dx + dy * dy + dz * dz;

                if (distSquared < radiusSquared) {
                    event.setCanceled(true);
                    displayMessageOnScreen(new TextComponent("This region is protected by the Dehammerizer!").withStyle(ChatFormatting.RED));
                    return;
                }
            }
        }

    }



    private static synchronized void saveDehammerizer(int currentDehammerizerIndex, DehammerizerConfig.Coordinates coordinates){

        String saveName = DehammerizerConfig.getSaveName();

        if (QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.containsKey(saveName)) {
            QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.get(saveName).put(currentDehammerizerIndex, coordinates);
        } else {
            Map<Integer, DehammerizerConfig.Coordinates> coordinatesMap = new HashMap<>();
            coordinatesMap.put(currentDehammerizerIndex, coordinates);
            QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.put(saveName, coordinatesMap);
        }

        QOLHunters.DEHAMMERIZER_CONFIG.save();

    }


    private static synchronized DehammerizerConfig.Coordinates getDehammerizer(int currentDehammerizerIndex){
        String saveName = DehammerizerConfig.getSaveName();
        if(QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.containsKey(saveName)){
            if(QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.get(saveName).containsKey(currentDehammerizerIndex)){
                return QOLHunters.DEHAMMERIZER_CONFIG.COORDINATES.get(saveName).get(currentDehammerizerIndex);
            }
        }
        return null;
    }


    static Integer playerStartHeight;
    static double ringHeight = 0.0D;
    public static void renderParticleRing(Player player, DehammerizerConfig.Coordinates centerPoint, double radius, int particleCount) {
        Level level = player.level;
        double centerX = centerPoint.x;
        double centerY = centerPoint.y + 1; // Adjust height as needed
        double centerZ = centerPoint.z;

        if(QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_MODE.get() == QOLHuntersClientConfigs.VirtualDehammerizerMode.CYLINDER){
            playerStartHeight = (playerStartHeight == null || ringHeight <=1.0D) ? player.blockPosition().getY() : playerStartHeight;
            centerY = ringHeight - 8.0 + playerStartHeight;
            for (int i = 0; i < particleCount; i++) {
                double angle = 6.283 * i / particleCount;
                double x = centerX + radius * Math.cos(angle);
                double z = centerZ + radius * Math.sin(angle);
                level.addAlwaysVisibleParticle(ParticleTypes.HAPPY_VILLAGER, true, x, centerY, z, 0, 0, 0);
            }

            ringHeight = (ringHeight + 0.1) % 16;

            return;
        }

        for (int i = 0; i < particleCount; i++) {
            double phi = Math.acos(2 * Math.random() - 1); // Random angle for latitude
            double theta = 6.283 * Math.random(); // Random angle for longitude

            double x = centerX + radius * Math.sin(phi) * Math.cos(theta);
            double y = centerY + radius * Math.sin(phi) * Math.sin(theta);
            double z = centerZ + radius * Math.cos(phi);
            level.addAlwaysVisibleParticle(ParticleTypes.HAPPY_VILLAGER, true, x, y, z, 0, 0, 0);
        }
    }

}
