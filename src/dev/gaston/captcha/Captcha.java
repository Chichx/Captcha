package dev.gaston.captcha;

import org.bukkit.plugin.java.*;

import dev.gaston.captcha.command.*;
import dev.gaston.captcha.handler.*;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import fr.xephi.authme.api.v3.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.event.*;

public class Captcha extends JavaPlugin
{
    public static Captcha instance;
    public static PluginManager plugin;
    public static Set<Player> authenticated;
    public static Set<Player> join;
    public static HashMap<String, ItemStack> saved_captcha;
    public static HashMap<String, Integer> saved_captcha_slot;
    public static HashMap<String, String> saved_captcha_info;
    public static AuthMeApi AuthmeApi;
    
    static {
        Captcha.plugin = Bukkit.getServer().getPluginManager();
        Captcha.authenticated = new HashSet<Player>();
        Captcha.join = new HashSet<Player>();
        Captcha.saved_captcha = new HashMap<String, ItemStack>();
        Captcha.saved_captcha_slot = new HashMap<String, Integer>();
        Captcha.saved_captcha_info = new HashMap<String, String>();
    }
    
    public void onEnable() {
        Captcha.instance = this;
        this.getLogger().info("Registering configuration...");
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eCaptcha &aEnabled"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eAuthor: &fGaston"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eVersion: " + ChatColor.WHITE + getInstance().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
        this.getCommand("captcha").setExecutor((CommandExecutor)new Commands());
        Captcha.plugin.registerEvents((Listener)new CaptchaHandler(), (Plugin)this);
    }
    
    public void onDisable() {
        Captcha.instance = this;
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eCaptcha &cDisabled"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eAuthor: &fGaston"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eVersion: " + ChatColor.WHITE + getInstance().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
    }
    
    public static Captcha getInstance() {
        return Captcha.instance;
    }
    
    public static boolean authme() {
        if (Bukkit.getPluginManager().getPlugin("AuthMe") == null) {
            Captcha.AuthmeApi = null;
            return false;
        }
        Captcha.AuthmeApi = AuthMeApi.getInstance();
        return true;
    }
}
