package dev.gaston.captcha.config;

import org.bukkit.configuration.file.*;

import dev.gaston.captcha.*;

import org.bukkit.*;

public class ConfigFile
{
    public static int kick;
    public static boolean enablekick;
    public static String gui_name;
    public static String gui_correct;
    public static String gui_incorrect;
    public static String authenticated;
    public static String kick_msg;
    public static String reload;
    public static String no_perms;
    public static FileConfiguration config;
    
    static {
        ConfigFile.kick = Captcha.getInstance().getConfig().getInt("Kick.time");
        ConfigFile.enablekick = Captcha.getInstance().getConfig().getBoolean("Kick.kick");
        ConfigFile.gui_name = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Gui.name"));
        ConfigFile.gui_correct = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Gui.correct"));
        ConfigFile.gui_incorrect = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Gui.incorrect"));
        ConfigFile.authenticated = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Messages.authentication"));
        ConfigFile.kick_msg = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Kick.msg"));
        ConfigFile.reload = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Messages.reload"));
        ConfigFile.no_perms = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Messages.perms"));
        ConfigFile.config = Captcha.getInstance().getConfig();
    }
    
    public static void refreshConfig() {
        Captcha.getInstance().saveDefaultConfig();
        Captcha.getInstance().reloadConfig();
        ConfigFile.config = Captcha.getInstance().getConfig();
        ConfigFile.kick = Captcha.getInstance().getConfig().getInt("Kick.time");
        ConfigFile.enablekick = Captcha.getInstance().getConfig().getBoolean("Kick.kick");
        ConfigFile.gui_name = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Gui.name"));
        ConfigFile.gui_correct = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Gui.correct"));
        ConfigFile.gui_incorrect = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Gui.incorrect"));
        ConfigFile.authenticated = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Messages.authentication"));
        ConfigFile.kick_msg = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Kick.msg"));
        ConfigFile.reload = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Messages.reload"));
        ConfigFile.no_perms = ChatColor.translateAlternateColorCodes('&', Captcha.getInstance().getConfig().getString("Messages.perms"));
    }
}
