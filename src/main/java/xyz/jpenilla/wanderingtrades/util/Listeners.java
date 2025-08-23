package xyz.jpenilla.wanderingtrades.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.wanderingtrades.WanderingTrades;
import xyz.jpenilla.wanderingtrades.listener.AcquireTradeListener;
import xyz.jpenilla.wanderingtrades.listener.BrainModificationListener;
import xyz.jpenilla.wanderingtrades.listener.JoinQuitListener;
import xyz.jpenilla.wanderingtrades.listener.ProtectTradersListener;
import xyz.jpenilla.wanderingtrades.listener.RefreshTradesListener;
import xyz.jpenilla.wanderingtrades.listener.TraderPotionListener;
import xyz.jpenilla.wanderingtrades.listener.TraderSpawnListener;

@DefaultQualifier(NonNull.class)
public final class Listeners {
    private final WanderingTrades plugin;
    private final Map<Class<?>, Listener> listeners = new HashMap<>();

    private Listeners(final WanderingTrades plugin) {
        this.plugin = plugin;
    }

    private <L extends Listener> void registerListener(final Class<L> listenerClass, final L listener) {
        this.listeners.put(listenerClass, listener);
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    private void register() {
        this.registerListener(JoinQuitListener.class, new JoinQuitListener(this.plugin));

        if (this.plugin.config().enabled()) {
            this.registerListener(AcquireTradeListener.class, new AcquireTradeListener(this.plugin));
            this.registerListener(TraderSpawnListener.class, new TraderSpawnListener(this.plugin));
            this.registerListener(ProtectTradersListener.class, new ProtectTradersListener());
        }

        if (this.plugin.config().refreshCommandTraders()) {
            this.registerListener(RefreshTradesListener.class, new RefreshTradesListener(this.plugin));
        }

        this.registerListener(TraderPotionListener.class, new TraderPotionListener(this.plugin));
        this.registerListener(BrainModificationListener.class, new BrainModificationListener(this.plugin));
    }

    private void unregister() {
        this.listeners.forEach((clazz, listener) -> HandlerList.unregisterAll(listener));
        this.listeners.clear();
    }

    public void reload() {
        this.unregister();
        this.register();
    }

    public static Listeners setup(final WanderingTrades plugin) {
        final Listeners instance = new Listeners(plugin);
        instance.register();
        return instance;
    }
}
