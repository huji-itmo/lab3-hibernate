package commands;

import commands.exceptions.CommandDoesntExistsException;
import commands.exceptions.CommandException;
import commands.nativeCommands.*;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Class that handles an execution of a given command through HashMap, that on given string key, gives out a command instance.
 * Stores history of 11 last commands.
 */
@Getter
public class CommandProcessor extends AbstractCommandProcessor{

    public CommandProcessor() {
        commands.put("help", new HelpCommand(this));
        commands.put("history", new HistoryCommand(this));
        commands.put("execute_script", new ExecuteScriptCommand(this));
        commands.put("exit", new ExitCommand());

    }
    private final HashMap<String, Command> commands = new HashMap<>();

    public static final int HISTORY_LENGTH = 11;

    private final ArrayDeque<String> history = new ArrayDeque<>(HISTORY_LENGTH);

    /**
     * Tries to execute a command with given input.
     *
     * @param input command
     */

    public void executeCommand(String input) {

        String[] commandSplit = input.trim().split(" ", 2);

        String commandName = commandSplit[0];
        String commandArgs = "";

        if (commandSplit.length >= 2) {
            commandArgs = commandSplit[1];
        }

        executeCommandWithArgs(commandName, commandArgs);
    }

    private void executeCommandWithArgs(String commandName, String args) {
        try {
            if (!commands.containsKey(commandName)) {
                throw new CommandDoesntExistsException(commandName);
            }
            String output = commands.get(commandName).execute(args);
            if (!output.isBlank())
                System.out.println(output);
        } catch (CommandException e) {
            System.err.println(e.getMessage());
        }

        if (history.size() == HISTORY_LENGTH) {
            history.removeFirst();
        }

        history.add(commandName);
    }
}
