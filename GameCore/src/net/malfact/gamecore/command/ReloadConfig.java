package net.malfact.gamecore.command;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.lib.Options;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadConfig extends PluginSubCommand{

	public ReloadConfig(String name) {
		super(name);
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		GameCore.getInstance().reloadConfig();
		Options.loadOptions(GameCore.getInstance());
		sender.sendMessage("§aGameCore Configuration File Reloaded!");
		return true;
	}
}
