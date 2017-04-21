package me.mrCookieSlime.QuestWorld.quests;

import me.mrCookieSlime.CSCoreLibPlugin.general.String.StringUtils;
import me.mrCookieSlime.QuestWorld.QuestWorld;
import me.mrCookieSlime.QuestWorld.utils.Text;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class MissionType {
	
	public enum SubmissionType {
		
		ITEM,
		ENTITY, 
		UNKNOWN, 
		CITIZENS_ITEM,
		LOCATION,
		INTEGER,
		TIME,
		CITIZENS_INTERACT,
		CITIZENS_KILL, 
		BLOCK;
		
	}
	
	String format, id;
	MaterialData item;
	SubmissionType type;
	boolean supportsTimeframes, supportsDeathReset, ticking;
	
	public MissionType(String name, boolean supportsTimeframes, boolean supportsDeathReset, boolean ticking, SubmissionType type, String format, MaterialData item) {
		this.id = name;
		this.format = Text.colorize(format);
		this.item = item;
		this.type = type;
		this.supportsTimeframes = supportsTimeframes;
		this.supportsDeathReset = supportsDeathReset;
		this.ticking = ticking;
	}
	
	public final String formatQuestDisplay(QuestMission instance) {
		return formatMissionDisplay(instance) + formatTimeframe(instance) + formatDeathReset(instance);
	}
	
	protected String formatMissionDisplay(QuestMission instance) {
		return instance.getName();
	}
	
	public String getFormat(EntityType entity, ItemStack item, Location location, int amount, String name, int citizenID, boolean spawners) {
		name = Text.colorize(name);
		switch (type) {
		case ENTITY: {
			return String.format(format, amount + "x " + (spawners ? "naturally spawned " : "") + StringUtils.format(entity.toString()) + ((!name.equals("") ? (Text.colorize(" named &r") + name): "") + ChatColor.GRAY));
		}
		case ITEM: {
			return String.format(format, amount + "x " + StringUtils.formatItemName(item, false) + ChatColor.GRAY);
		}
		case BLOCK: {
			return String.format(format, amount + "x " + StringUtils.formatItemName(item, false) + ChatColor.GRAY);
		}
		case CITIZENS_ITEM: {
			NPC npc = CitizensAPI.getNPCRegistry().getById(citizenID);
			return String.format(format, amount + "x " + StringUtils.formatItemName(item, false), ChatColor.GRAY + (npc == null ? "N/A": npc.getName()) + ChatColor.GRAY);
		}
		case CITIZENS_INTERACT: {
			NPC npc = CitizensAPI.getNPCRegistry().getById(citizenID);
			return String.format(format, Text.colorize("&7" + (npc == null ? "N/A": npc.getName()) + "&7"));
		}
		case CITIZENS_KILL: {
			NPC npc = CitizensAPI.getNPCRegistry().getById(citizenID);
			return String.format(format, Text.colorize("&7" + (npc == null ? "N/A": npc.getName()) + "&7 " + amount + " times"));
		}
		case INTEGER: {
			return String.format(format, amount);
		}
		case TIME: {
			return String.format(format, (amount / 60) + "h " + (amount % 60) + "m");
		}
		case LOCATION: {
			return String.format(format, name.equals("") ? ("X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ() + ChatColor.GRAY): (ChatColor.RESET + name));
		}
		default:
			return String.format(format, name);
		}
	}
	
	private String formatTimeframe(QuestMission quest) {
		if(!quest.hasTimeframe() || !supportsTimeframes)
			return "";
		long duration = quest.getTimeframe();
		
		return " &7within " + (duration / 60) + "h " + (duration % 60) + "m";
	}
	
	private String formatDeathReset(QuestMission quest) {
		if(!quest.resetsonDeath() || !supportsDeathReset)
			return "";
		
		return " &7without dying";
	}

	public MaterialData getItem() {
		return item;
	}
	
	public ItemStack getDisplayItem(QuestMission qm) {
		return new ItemStack(Material.COMMAND);
	}

	public static MissionType valueOf(String id) {
		return QuestWorld.getInstance().getMissionTypes().get(id);
	}

	public String getID() {
		return id;
	}
	
	public SubmissionType getSubmissionType() {
		return type;
	}
	
	public boolean supportsTimeframes() {
		return this.supportsTimeframes;
	}
	
	@Override
	public String toString() {
		return id;
	}

	public boolean supportsDeathReset() {
		return this.supportsDeathReset;
	}

	public boolean isTicker() {
		return this.ticking;
	}
}
