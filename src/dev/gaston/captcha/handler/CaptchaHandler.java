package dev.gaston.captcha.handler;

import org.bukkit.scheduler.*;

import dev.gaston.captcha.*;
import dev.gaston.captcha.config.*;

import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import java.security.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

public class CaptchaHandler implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if (Captcha.authme()) {
            new BukkitRunnable() {
                public void run() {
                    if (Captcha.AuthmeApi.isAuthenticated(p)) {
                        Captcha.join.add(p);
                        CaptchaHandler.this.auth(p);
                        this.cancel();
                    }
                }
            }.runTaskTimer(Captcha.getInstance(), 0L, 0L);
        }
        else {
            Captcha.join.add(p);
            this.auth(p);
        }
    }
    
    public void auth(final Player p) {
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            this.captcha(p);
        }
        new BukkitRunnable() {
            public void run() {
                if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p) && ConfigFile.enablekick) {
                    p.kickPlayer(ConfigFile.kick_msg);
                }
            }
        }.runTaskLater(Captcha.getInstance(), 20L * ConfigFile.kick);
    }
    
    @EventHandler
    public void onCloseCaptchaInventory(final InventoryCloseEvent e) {
        final Player p = (Player)e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            this.captcha(p);
        }
    }
    
    public void captcha(final Player p) {
        final int randomslot = randomSlot(0, 8);
        if (Captcha.saved_captcha.get(String.valueOf(String.valueOf(p.getName())) + "1") != null) {
            final String str = Captcha.saved_captcha_info.get(String.valueOf(String.valueOf(p.getName())) + "4");
            final ItemStack itemStack1 = Captcha.saved_captcha.get(String.valueOf(String.valueOf(p.getName())) + "1");
            final ItemStack itemStack2 = Captcha.saved_captcha.get(String.valueOf(String.valueOf(p.getName())) + "2");
            final int slot = Captcha.saved_captcha_slot.get(String.valueOf(String.valueOf(p.getName())) + "3");
            final Inventory savedcaptcha = Bukkit.createInventory((InventoryHolder)null, InventoryType.DISPENSER, str);
            for (int j = 0; j < 9; ++j) {
                savedcaptcha.setItem(j, new ItemStack(itemStack2));
            }
            savedcaptcha.setItem(slot, new ItemStack(itemStack1));
            new BukkitRunnable() {
                public void run() {
                    p.openInventory(savedcaptcha);
                }
            }.runTaskLater(Captcha.getInstance(), 1L);
            return;
        }
        Material item1_raw = Material.getMaterial(this.randomBlock());
        Material item2_raw = Material.getMaterial(this.randomBlock());
        if (item1_raw.equals((Object)item2_raw)) {
            item1_raw = Material.getMaterial(this.randomBlock());
            item2_raw = Material.getMaterial(this.randomBlock());
        }
        final ItemStack item1 = setMeta(new ItemStack(item1_raw), ConfigFile.gui_correct);
        final ItemStack item2 = setMeta(new ItemStack(item2_raw), ConfigFile.gui_incorrect);
        final String title = ConfigFile.gui_name.replaceAll("%block%", item1.getType().toString());
        final Inventory captcha = Bukkit.createInventory(null, InventoryType.DISPENSER, title);
        for (int i = 0; i < 9; ++i) {
            captcha.setItem(i, new ItemStack(item2));
        }
        captcha.setItem(randomslot, new ItemStack(item1));
        new BukkitRunnable() {
            public void run() {
                p.openInventory(captcha);
                Captcha.saved_captcha.put(String.valueOf(String.valueOf(p.getName())) + "1", item1);
                Captcha.saved_captcha.put(String.valueOf(String.valueOf(p.getName())) + "2", item2);
                Captcha.saved_captcha_slot.put(String.valueOf(String.valueOf(p.getName())) + "3", randomslot);
                Captcha.saved_captcha_info.put(String.valueOf(String.valueOf(p.getName())) + "4", title);
            }
        }.runTaskLater(Captcha.getInstance(), 1L);
    }
    
    @EventHandler
    public void onRightClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        final ItemStack o = e.getCurrentItem();
        final InventoryView i = e.getView();
        final boolean captcha = e.getView().getTitle().equalsIgnoreCase(Captcha.saved_captcha_info.get(String.valueOf(String.valueOf(p.getName())) + "4"));
        try {
            if (captcha) {
                e.setCancelled(true);
                if (o.equals((Object)i.getItem((int)Captcha.saved_captcha_slot.get(String.valueOf(String.valueOf(p.getName())) + "3")))) {
                    Captcha.authenticated.add(p);
                    Captcha.join.remove(p);
                    p.sendMessage(ConfigFile.authenticated);
                    p.closeInventory();
                }
                else if (ConfigFile.enablekick && e.getClickedInventory().getType() != InventoryType.PLAYER) {
                    p.kickPlayer(ConfigFile.kick_msg);
                }
            }
        }
        catch (NullPointerException ex) {}
    }
    
    public static ItemStack setMeta(final ItemStack m, final String name) {
        if (m.getType() != Material.AIR && m.getType() != null) {
            final ItemMeta meta = m.getItemMeta();
            meta.setDisplayName(name);
            m.setItemMeta(meta);
        }
        return m;
    }
    
    public String randomBlock() {
        final String[] arr = { "EMERALD_BLOCK", "EMERALD", "DIAMOND_BLOCK", "DIAMOND", "GOLD_BLOCK", "GOLD_INGOT", "IRON_BLOCK", "IRON_INGOT", "REDSTONE_BLOCK", "REDSTONE", "CHEST", "HOPPER", "BEACON", "PAPER", "BOOK", "ICE", "BOOK_AND_QUILL", "ARROW", "SUGAR", "STONE", "COBBLESTONE", "GRASS", "NETHERRACK", "BOOKSHELF" };
        final Random r = new Random();
        final int randomNumber = r.nextInt(arr.length);
        return arr[randomNumber];
    }
    
    public static int randomSlot(final int min, final int max) {
        final SecureRandom rand = new SecureRandom();
        final int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        Captcha.join.remove(p);
        Captcha.saved_captcha.remove(String.valueOf(String.valueOf(p.getName())) + "1");
        Captcha.saved_captcha.remove(String.valueOf(String.valueOf(p.getName())) + "2");
        Captcha.saved_captcha_slot.remove(String.valueOf(String.valueOf(p.getName())) + "3");
        Captcha.authenticated.remove(p);
    }
    
    @EventHandler
    public void onCmd(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            if (!p.isOnGround()) {
                p.setAllowFlight(false);
            }
            else {
                final Location from = e.getFrom();
                from.setYaw(e.getTo().getYaw());
                from.setPitch(e.getTo().getPitch());
                e.setTo(from);
            }
        }
    }
    
    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        if (!Captcha.authenticated.contains(p) && Captcha.join.contains(p)) {
            e.setCancelled(true);
        }
    }
}
