package dev.gaston.captcha.command;

import org.bukkit.configuration.file.*;

import dev.gaston.captcha.*;
import dev.gaston.captcha.config.*;
import dev.gaston.captcha.utils.*;

import org.bukkit.command.*;

public class Commands implements CommandExecutor
{
    FileConfiguration config;
    
    public Commands() {
        this.config = Captcha.getInstance().getConfig();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage("§eCaptcha Plugin");
            sender.sendMessage("§eAuthor§7: §fGaston");
            sender.sendMessage("§eVersion§7: §f" + Captcha.getInstance().getDescription().getVersion());
            sender.sendMessage("§eDiscord§7: §fGaston#0001");
            sender.sendMessage(CC.CHAT_BAR);
            return true;
        }
        if (!args[0].equalsIgnoreCase("reload")) {
            return true;
        }
        if (sender.hasPermission("captcha.*") || sender.isOp()) {
            sender.sendMessage(ConfigFile.reload);
            ConfigFile.refreshConfig();
            return true;
        }
        sender.sendMessage(ConfigFile.no_perms);
        return true;
    }
}
