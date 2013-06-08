package net.malfact.gamecore.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public class PluginCommand extends Command implements PluginIdentifiableCommand{

	protected ArrayList<PluginSubCommand> subCommands = new ArrayList<PluginSubCommand>();
	
	protected final Plugin owningPlugin;
	protected CommandExecutor executor;
	protected TabCompleter completer;
	
	public PluginCommand(String name, Plugin owner) {
		super(name);
		this.executor = owner;
        this.owningPlugin = owner;
        this.usageMessage = "";
	}
	
	public PluginCommand(String name, Plugin owner, List<String> aliases) {
		super(name);
		this.setAliases(aliases);
		this.executor = owner;
        this.owningPlugin = owner;
        this.usageMessage = "";
	}
	
	public PluginCommand(String name, Plugin owner, String... aliases) {
		super(name);
		ArrayList<String> aliasList = new ArrayList<String>();
		{
			for (String s : aliases){
				aliasList.add(s);
			}
		}
		this.setAliases(aliasList);
		this.executor = owner;
        this.owningPlugin = owner;
        this.usageMessage = "";
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		boolean success = false;

        if (!owningPlugin.isEnabled())
            return false;

        if (args.length == 0)
        	args = new String[]{""};
        
        PluginSubCommand cmd = getSubCommand(args[0]);
        if (cmd != null){
	        if (!cmd.testPermission(sender) || !testPermission(sender))
	            return true;
	        
	        success = cmd.execute(sender, this, args);
	        
	        if (!success){
	        	sender.sendMessage("§6"+cmd.getUsage());
	        }
	        
	        success = true;
        } else {
        	sender.sendMessage("§e---- §fHelp: "+this.getName()+" §e----");
        	sender.sendMessage("§7"+this.getUsage()+" : "+this.getDescription());
        	for (PluginSubCommand a : subCommands){
        		sender.sendMessage("§6"+a.getUsage()+": §f"+a.getDescription());
        	}
        	
        	success = true;
        }
        
        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }
        
        return success;
	}

	@Override
	public Plugin getPlugin() {
		return owningPlugin;
	}
	
	public PluginCommand setSubCommands(ArrayList<PluginSubCommand> subCommands) {
		this.subCommands = subCommands;
		return this;
	}
	
	public PluginCommand setSubCommands(PluginSubCommand... subCommands) {
		this.subCommands = new ArrayList<PluginSubCommand>();
		for (PluginSubCommand cmd : subCommands){
			this.subCommands.add(cmd);
		}
		return this;
	}
	
	public ArrayList<PluginSubCommand> getSubCommands() {
		return subCommands;
	}
	
	public PluginSubCommand getSubCommand(String name){
		for (PluginSubCommand cmd : subCommands){
			if (cmd.getName().equalsIgnoreCase(name)){
				return cmd;
			}
		}
		return null;
	}
}
