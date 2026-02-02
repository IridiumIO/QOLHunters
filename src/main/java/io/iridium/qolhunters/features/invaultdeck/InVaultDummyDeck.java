package io.iridium.qolhunters.features.invaultdeck;

import iskallia.vault.client.gui.screen.CardDeckScreen;
import iskallia.vault.container.inventory.CardDeckContainer;
import iskallia.vault.container.inventory.CardDeckContainerMenu;
import iskallia.vault.init.ModItems;
import iskallia.vault.network.message.OpenCardDeckMessage;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(Dist.CLIENT)
public class InVaultDummyDeck {
    /** {@link iskallia.vault.network.message.OpenCardDeckMessage#handle(OpenCardDeckMessage, Supplier)} */
    @SubscribeEvent
    public static void onClientChatReceived(ClientChatReceivedEvent event) {
        var msg = event.getMessage();
        if (msg instanceof TextComponent tc && "You can't open the card deck in a Vault!"/*or can you??*/.equals(tc.getText())) {
            var player = Minecraft.getInstance().player;
            if (player == null) return;
            var unsafeStack = CuriosApi
                .getCuriosHelper()
                .findFirstCurio(player, ModItems.CARD_DECK).map(SlotResult::stack).orElse(ItemStack.EMPTY);
            if (unsafeStack.isEmpty()) return;
            var curioCopy = unsafeStack.copy();

            // disable as many things as possible
            Inventory fakeInv = new Inventory(player) {
                @Override public ItemStack getItem(int index) {return ItemStack.EMPTY;}
                @Override public void setItem(int index, ItemStack stack) {}
                @Override public boolean add(ItemStack stack) {return false;}
            };

            CardDeckContainerMenu menu =
                new CardDeckContainerMenu(0, fakeInv, 0, true, new CardDeckContainer(curioCopy)) {
                    @Override public boolean stillValid(Player p) { return true; }
                    @Override public ItemStack quickMoveStack(Player p, int i) { return ItemStack.EMPTY; }
                    @Override public void clicked(int s, int b, ClickType t, Player p) {}
                };

            var screen = new CardDeckScreen(menu, fakeInv, new TextComponent("Fake Card Deck View")) {
                @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {return (keyCode != GLFW.GLFW_KEY_ESCAPE) || super.keyPressed(keyCode, scanCode, modifiers);}
                @Override public boolean mouseClicked(double x, double y, int button) {return true;}
                @Override public boolean mouseReleased(double x, double y, int button) {return true;}
                @Override public boolean mouseDragged(double x, double y, int button, double dx, double dy) {return true;}
            };
            Minecraft.getInstance().setScreen(screen);
        }
    }

}
