package me.sonny.soundtest;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;

import com.comphenix.protocol.wrappers.MinecraftKey;

public class SoundTest extends JavaPlugin {
	
	@Override
	public void onEnable() {
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.CUSTOM_SOUND_EFFECT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if (event.getPacketType() == PacketType.Play.Server.CUSTOM_SOUND_EFFECT) {
							MinecraftKey key = event.getPacket().getMinecraftKeys().read(0);
							
							getServer().broadcastMessage(key.getFullKey());
						}
					}
				});
	}
	
	@Override
	public void onDisable() {
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("soundtest")) {	
			if (sender instanceof Player) {
		        Player player = (Player) sender;
		        Location loc = player.getLocation();
		        
		        player.sendMessage("SoundTest");
		        
		        MinecraftKey key = new MinecraftKey("ambient.cave");
		        
	            ProtocolManager pm = ProtocolLibrary.getProtocolManager();
	            PacketContainer packet = pm.createPacket(PacketType.Play.Server.CUSTOM_SOUND_EFFECT);
	            packet.getModifier().writeDefaults();
	            
	            packet.getMinecraftKeys().write(0, key);
	            packet.getSoundCategories().write(0, SoundCategory.AMBIENT);
	            packet.getIntegers().write(0, loc.getBlockX()).write(1, loc.getBlockY()).write(2, loc.getBlockZ());
	            packet.getFloat().write(0, 10.0f).write(1, 1.0f);
	            
	            try {
					pm.sendServerPacket(player, packet);
				} catch (InvocationTargetException e) {
					player.sendMessage(e.getMessage());
				}
					
				return true;
			}
		}
		
		return false;
	}
}
