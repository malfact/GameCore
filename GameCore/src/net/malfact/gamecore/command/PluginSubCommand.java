package net.malfact.gamecore.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PluginSubCommand {
	private String name;
	private String usage;
	private String description;
	private String permission;
	private String permissionMessage;
	
	public PluginSubCommand(String name) {
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPermission(){
		return permission;
	}
	
	public String getPermissionMessage() {
		return permissionMessage;
	}
	
	public PluginSubCommand setPermission(String permission) {
		this.permission = permission;
		return this;
	}
	
	public PluginSubCommand setPermissionMessage(String permissionMessage) {
		this.permissionMessage = permissionMessage;
		return this;
	}
	
	public PluginSubCommand setUsage(String usage) {
		this.usage = usage;
		return this;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public boolean execute(CommandSender sender, Command cmd, String[] args){
		return false;
	}
	
	public PluginSubCommand setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean testPermission(CommandSender target){
		if (testPermissionSilent(target)) {
            return true;
        }

        if (permissionMessage == null) {
            target.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
        } else if (permissionMessage.length() != 0) {
            for (String line : permissionMessage.replace("<permission>", permission).split("\n")) {
                target.sendMessage(line);
            }
        }

        return false;
	}
	
	public boolean testPermissionSilent(CommandSender target) {
        if ((permission == null) || (permission.length() == 0)) {
            return true;
        }

        for (String p : permission.split(";")) {
            if (target.hasPermission(p)) {
                return true;
            }
        }

        return false;
    }
}
