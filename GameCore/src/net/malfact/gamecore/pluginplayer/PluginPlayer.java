package net.malfact.gamecore.pluginplayer;

import java.util.ArrayList;

import net.malfact.gamecore.profession.Profession;

import org.bukkit.entity.Player;

public class PluginPlayer {

		private static ArrayList<PluginPlayer> players = new ArrayList<PluginPlayer>();
		
		private Player player;
		private Profession[] professions;
		
		PluginPlayer(Player player){
			this.player = player;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		public static ArrayList<PluginPlayer> getPlayers() {
			return players;
		}
		
		public PluginPlayer setProfessions(Profession[] professions) {
			this.professions = professions;
			return this;
		}
		
		public Profession[] getProfessions() {
			return professions;
		}
		
		public static Player getPlayer(String name){
			for (PluginPlayer player : players){
				if (player.getPlayer().getName().equalsIgnoreCase(name)){
					return player.getPlayer();
				}
			}
			return null;
		}
		
		public static void addPlayer(PluginPlayer player){
			for (PluginPlayer p : players){
				if (p.getPlayer().getName().equalsIgnoreCase(player.getPlayer().getName())){
					return;
				}
			}
			players.add(player);
		}
		
		public static void removePlayer(String name){
			PluginPlayer[] playerArray = players.toArray(new PluginPlayer[players.size()]);
			ArrayList<PluginPlayer> playerArrayList = new ArrayList<PluginPlayer>();
			for (PluginPlayer player : playerArray){
				if (!player.getPlayer().getName().equalsIgnoreCase(name)){
					playerArrayList.add(player);
				}
			}
			players = playerArrayList;
		}
		
}
