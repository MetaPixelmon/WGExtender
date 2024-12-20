/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package wgextender.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommandUtils {
	public static CommandMap getCommands() {
		try {
			Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			return (CommandMap) commandMapField.get(Bukkit.getServer());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static List<String> getCommandAliases(String commandName) {
		CommandMap commandMap = getCommands();
		if (commandMap == null) {
			return Collections.singletonList(commandName);
		}

		Command command = commandMap.getCommand(commandName);
		if (command == null) {
			return Collections.singletonList(commandName);
		}

		List<String> aliases = new ArrayList<>();
		aliases.add(commandName);

		try {
			Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);

			Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
			for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
				if (entry.getValue().equals(command)) {
					aliases.add(entry.getKey());
				}
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		return aliases;
	}

	public static void replaceCommand(Command oldCommand, Command newCommand) {
		String cmdName = oldCommand.getName();
		var commandMap = getCommands();
		if (commandMap.getCommand(cmdName).equals(oldCommand)) {
			commandMap.register(cmdName, newCommand);
		}
		for (String alias : oldCommand.getAliases()) {
			if (commandMap.getCommand(alias).equals(oldCommand)) {
				commandMap.register(alias, newCommand);
			}
		}
	}
}
