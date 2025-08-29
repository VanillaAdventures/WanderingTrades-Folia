package xyz.jpenilla.wanderingtrades.gui;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.wanderingtrades.WanderingTrades;
import xyz.jpenilla.wanderingtrades.config.Messages;
import xyz.jpenilla.wanderingtrades.config.TradeConfig;
import xyz.jpenilla.wanderingtrades.util.HeadBuilder;
import xyz.jpenilla.wanderingtrades.util.ItemBuilder;

@NullMarked
public final class ListTradesInterface extends BaseInterface {
    private final TradeConfig tradeConfig;

    public ListTradesInterface(
        final WanderingTrades plugin,
        final TradeConfig tradeConfig
    ) {
        super(plugin);
        this.tradeConfig = tradeConfig;
    }

    @Override
    protected ChestInterface buildInterface() {
        final int rows = 6;
        return ChestInterface.builder()
            .rows(rows)
            .title(Component.textOfChildren(Messages.GUI_TRADE_LIST_TITLE, Component.text(this.tradeConfig.configName())))
            .addTransform(this.parts.fillBottomRow())
            .addTransform((pane, view) -> pane.element(this.newTradeElement(), 4, pane.rows() - 1))
            .addTransform((pane, view) -> pane.element(this.settingsElement(), 7, pane.rows() - 1))
            .addTransform(this.parts.backButton(context -> new ListTradeConfigsInterface(this.plugin).replaceActiveScreen(context)))
            .addReactiveTransform(this.parts.paginationTransform(rows, this::listElements))
            .build();
    }

    private ItemStackElement<ChestPane> settingsElement() {
        final ItemStack stack = new HeadBuilder(HeadSkins.WRENCH_ON_IRON)
            .customName(Messages.GUI_TRADE_LIST_EDIT_CONFIG)
            .lore(Messages.GUI_TRADE_LIST_EDIT_CONFIG_LORE)
            .build();
        return ItemStackElement.of(stack, this::settingsClick);
    }

    private void settingsClick(final ClickContext<ChestPane, InventoryClickEvent, PlayerViewer> context) {
        new TradeConfigSettingsInterface(this.plugin, this.tradeConfig).replaceActiveScreen(context);
    }

    private ItemStackElement<ChestPane> newTradeElement() {
        final ItemStack stack = new HeadBuilder(HeadSkins.GREEN_PLUS)
            .customName(Messages.GUI_TRADE_LIST_NEW_TRADE)
            .lore(Messages.GUI_TRADE_LIST_NEW_TRADE_LORE)
            .build();
        return ItemStackElement.of(stack, this::newTradeClick);
    }

    private void newTradeClick(final ClickContext<ChestPane, InventoryClickEvent, PlayerViewer> context) {
        new TradeCreateInterface(this.plugin, this.tradeConfig).replaceActiveScreen(context);
    }

    private List<ItemStackElement<ChestPane>> listElements() {
        return this.tradeConfig.tradesByName().entrySet().stream()
            .map(entry -> this.tradeEntry(entry.getKey(), entry.getValue()))
            .toList();
    }

    private ItemStackElement<ChestPane> tradeEntry(final String tradeName, final MerchantRecipe recipe) {
        final ItemStack stack = ItemBuilder.create(recipe.getResult())
            .clearEnchants()
            .clearLore()
            .customName(Component.text(tradeName, NamedTextColor.AQUA))
            .build();
        return ItemStackElement.of(stack, context -> this.tradeEntryClick(tradeName, context));
    }

    private void tradeEntryClick(
        final String tradeName,
        final ClickContext<ChestPane, InventoryClickEvent, PlayerViewer> context
    ) {
        new TradeEditInterface(this.plugin, this.tradeConfig, tradeName).replaceActiveScreen(context);
    }
}
