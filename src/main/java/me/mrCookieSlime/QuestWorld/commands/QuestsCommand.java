package me.mrCookieSlime.QuestWorld.commands;

import java.util.UUID;

import me.mrCookieSlime.QuestWorld.QuestWorld;
import me.mrCookieSlime.QuestWorld.api.Translation;
import me.mrCookieSlime.QuestWorld.quests.Category;
import me.mrCookieSlime.QuestWorld.quests.Party;
import me.mrCookieSlime.QuestWorld.quests.QuestBook;
import me.mrCookieSlime.QuestWorld.utils.PlayerTools;
import me.mrCookieSlime.QuestWorld.utils.Text;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 0) QuestBook.openLastMenu((Player) sender);
			else {
				if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
					Party party = QuestWorld.getInstance().getManager(Bukkit.getOfflinePlayer(UUID.fromString(args[1]))).getParty();
					if (party != null && party.hasInvited((Player) sender)) {
						if (party.getPlayers().size() >= QuestWorld.getInstance().getCfg().getInt("party.max-members")) {
							PlayerTools.sendTranslation(sender, true, Translation.party_errorfull);
						}
						else party.addPlayer((Player) sender);
					}
				}
				else {
					try {
						Category category = QuestWorld.getInstance().getCategory(Integer.parseInt(args[0]));
						if (category != null)  {
							if (args.length == 2) QuestBook.openQuest((Player) sender, category.getQuest(Integer.parseInt(args[1])), false, false);
							else QuestBook.openCategory((Player) sender, category, false);
						}
						else sender.sendMessage(Text.colorize("&4Unknown Category: &c", args[0]));
					} catch(Exception x) {
						 sender.sendMessage(Text.colorize("&4Unknown Category: &c", args[0]));
					}
				}
			}
		}
		else sender.sendMessage(Text.colorize("&4You are not a Player"));
		return true;
	}

}
