package me.missionary.cuckhub.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * Command Framework - BukkitCompleter <br>
 * An implementation of the TabCompleter class allowing for multiple tab
 * completers per command
 * 
 * @author minnymin3
 * 
 */
public class BukkitCompleter implements TabCompleter {

	private Map<String, Entry<Method, Object>> completers = new HashMap<>();

	public void addCompleter(String label, Method m, Object obj) {
		completers.put(label, new AbstractMap.SimpleEntry<>(m, obj));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		for (int i = args.length; i >= 0; i--) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(label.toLowerCase());
			for (int x = 0; x < i; x++) {
				if (!args[x].equals("") && !args[x].equals(" ")) {
					buffer.append(".").append(args[x].toLowerCase());
				}
			}
			String cmdLabel = buffer.toString();
			if (completers.containsKey(cmdLabel)) {
				Entry<Method, Object> entry = completers.get(cmdLabel);
				try {
					return (List<String>) entry.getKey().invoke(entry.getValue(),
							new CommandArgs(sender, command, label, args, cmdLabel.split("\\.").length - 1));
				} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
					e.printStackTrace();
				}
            }
		}
		return null;
	}

}