package xyz.jpenilla.wanderingtrades.util;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import org.jspecify.annotations.NullMarked;
import xyz.jpenilla.wanderingtrades.WanderingTrades;

@NullMarked
public interface PlayerHeads {
    List<MerchantRecipe> randomlySelectPlayerHeads();

    void handleLogin(final Player player);

    void handleLogout(final Player player);

    void configChanged();

    static PlayerHeads create(final WanderingTrades plugin) {
        return new PlayerHeadsImpl(plugin);
    }
}
