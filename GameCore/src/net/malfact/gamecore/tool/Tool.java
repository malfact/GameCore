package net.malfact.gamecore.tool;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Tool {

	public static final ArrayList<Tool> toolList = new ArrayList<Tool>();
	
	public final int toolId;
	
	public static final Tool netherStar = new NetherStarTool(Material.NETHER_STAR.getId());
	
	public Tool(int id) {
		this.toolId = id;
		
		toolList.add(this);
	}
	
	public boolean rightClickAir(Player player){
		return false;
	}
	
	public boolean leftClickAir(Player player){
		return false;
	}
	
	public boolean rightClickBlock(Player player){
		return false;
	}
	
	public boolean leftClickBlock(Player player){
		return false;
	}
	
	public static Tool getToolFromId(int id){
		Tool[] tools = toolList.toArray(new Tool[toolList.size()]);
		
		for (Tool tool : tools){
			if (tool.toolId == id)
				return tool;
		}
		
		return null;
	}
}
