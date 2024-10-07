package io.iridium.qolhunters.features.backpackcycler;

import io.iridium.qolhunters.QOLHunters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.client.KeybindHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.network.BackpackOpenMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.SBPPacketHandler;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value= Dist.CLIENT)
public class BackpackCycler {



    private static int currentBackpackIndex = 0;

    @SubscribeEvent
    public static void keyInputScreen(InputEvent.KeyInputEvent event){

        if(event.getAction() != GLFW.GLFW_PRESS) return;

        if(event.getKey() == KeybindHandler.BACKPACK_OPEN_KEYBIND.getKey().getValue()){
            if(!KeyConflictContext.GUI.isActive()){
                CycleButton.wasLastBackpackOpenedDoneSoAutomaticallyUsingTheOneThatTriesToOpenFromTheCuriosSlot = true;
                return;
            }
            CycleButton.wasLastBackpackOpenedDoneSoAutomaticallyUsingTheOneThatTriesToOpenFromTheCuriosSlot = false;
            return;
        }
        Screen screen = Minecraft.getInstance().screen;
        if(!(screen instanceof BackpackScreen)) return;

        if (event.getKey() == GLFW.GLFW_KEY_LEFT) {
                cycleBackpack((AbstractContainerScreen<?>) screen, -1); // Cycle left
        } else if (event.getKey() == GLFW.GLFW_KEY_RIGHT) {
            cycleBackpack((AbstractContainerScreen<?>) screen, 1); // Cycle right
        }


    }


    @SubscribeEvent
    public static void addCustomButtonToInventoryS(ScreenEvent.InitScreenEvent.Post event) {
        if (event.getScreen() instanceof BackpackScreen screen) {
            event.addListener(new CycleButton(screen, 2, "←", -1));
            event.addListener(new CycleButton(screen, 21, "→", 1));
        }
    }

    static class CycleButton extends Button {
        private final int direction;


        private static boolean wasLastBackpackOpenedDoneSoAutomaticallyUsingTheOneThatTriesToOpenFromTheCuriosSlot = false;

        public CycleButton(AbstractContainerScreen<?> screen, int xOffset, String label, int direction) {
            super(screen.getGuiLeft() + xOffset, screen.getGuiTop() - 19, 20, 20, new TextComponent(label), (button) -> {
                cycleBackpack(screen, direction);
            }, (button, poseStack, mouseX, mouseY) -> {
                screen.renderTooltip(poseStack, Collections.singletonList(new TextComponent("Cycle Backpack")), Optional.empty(), mouseX, mouseY);
            });
            this.direction = direction;
        }


        private static List<ItemStack> findBackpacks(Player player) {
            List<ItemStack> backpacks = new ArrayList<>();
            for (ItemStack stack : player.getInventory().items) {
                if (stack.getItem() instanceof BackpackItem) {
                    backpacks.add(stack);
                }
            }
            QOLHunters.LOGGER.info("Backpacks: " + backpacks.size());
            return backpacks;
        }

    }

    private static void cycleBackpack(AbstractContainerScreen<?> screen, int direction) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            List<ItemStack> backpacks = CycleButton.findBackpacks(player);
            if (!backpacks.isEmpty()) {
                currentBackpackIndex = (currentBackpackIndex + direction + backpacks.size()) % backpacks.size();
                ItemStack backpack = backpacks.get(currentBackpackIndex);

                int backpackSlot = player.getInventory().findSlotMatchingItem(backpack);

                double MouseX = Minecraft.getInstance().mouseHandler.xpos();
                double MouseY = Minecraft.getInstance().mouseHandler.ypos();

                screen.onClose();
                Minecraft.getInstance().setScreen(new InventoryScreen(player));

                Minecraft.getInstance().execute(() -> {
                    GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), MouseX, MouseY);
                    if (currentBackpackIndex == 0 && !CycleButton.wasLastBackpackOpenedDoneSoAutomaticallyUsingTheOneThatTriesToOpenFromTheCuriosSlot) {
                        CycleButton.wasLastBackpackOpenedDoneSoAutomaticallyUsingTheOneThatTriesToOpenFromTheCuriosSlot = true;
                        SBPPacketHandler.INSTANCE.sendToServer(new BackpackOpenMessage());
                    } else {
                        CycleButton.wasLastBackpackOpenedDoneSoAutomaticallyUsingTheOneThatTriesToOpenFromTheCuriosSlot = false;
                        SBPPacketHandler.INSTANCE.sendToServer(new BackpackOpenMessage(backpackSlot));
                    }
                });
            }
        }
    }




    @SubscribeEvent
    public static void testRender(ScreenEvent.DrawScreenEvent.Post event) {
        if (event.getScreen() instanceof InventoryScreen || event.getScreen() instanceof BackpackScreen ) {
            Screen screen = event.getScreen();
            screen.renderables.forEach(widget -> {
                if (widget instanceof CycleButton) {
                    widget.render(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTicks());
                }
            });
        }
    }


}
