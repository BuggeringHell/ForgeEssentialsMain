package com.forgeessentials.economy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.forgeessentials.api.IEconManager;
import com.forgeessentials.data.v2.DataManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

/**
 * Call these methods to modify a target's Wallet.
 */
public class WalletHandler implements IEconManager {

    private static HashMap<UUID, Wallet> wallets = new HashMap<UUID, Wallet>();

    @Override
    public void addToWallet(int amountToAdd, UUID player)
    {
        wallets.get(player).amount = wallets.get(player).amount + amountToAdd;
    }

    @Override
    public int getWallet(UUID player)
    {
        return wallets.get(player).amount;
    }

    @Override
    public boolean removeFromWallet(int amountToSubtract, UUID player)
    {
        if (wallets.get(player).amount - amountToSubtract >= 0)
        {
            wallets.get(player).amount = wallets.get(player).amount - amountToSubtract;
            return true;
        }
        return false;
    }

    @Override
    public void setWallet(int setAmount, EntityPlayer player)
    {
        wallets.get(player.getUniqueID()).amount = setAmount;
    }

    @Override
    public String currency(int amount)
    {
        if (amount == 1)
        {
            return ModuleEconomy.currencySingular;
        }
        else
        {
            return ModuleEconomy.currencyPlural;
        }
    }

    @Override
    public String getMoneyString(UUID username)
    {
        int am = getWallet(username);
        return ModuleEconomy.formatCurrency(am);
    }

    private void saveWallet(Wallet wallet)
    {
        DataManager.getInstance().save(wallet, wallet.getUsername());
    }

    @Override
    public void save()
    {
        for (Wallet wallet : wallets.values()) {
            saveWallet(wallet);
        }
    }

    @Override
    public Map<String, Integer> getItemTables()
    {
        return ModuleEconomy.tables.valueMap;
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Wallet wallet = DataManager.getInstance().load(Wallet.class, event.player.getUniqueID().toString());
        if (wallet == null)
        {
            wallet = new Wallet(event.player, ModuleEconomy.startbudget);
        }
        wallets.put(event.player.getUniqueID(), wallet);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (wallets.containsKey(event.player.getUniqueID()))
        {
            Wallet wallet = wallets.remove(event.player.getUniqueID());
            saveWallet(wallet);
        }
    }
}
