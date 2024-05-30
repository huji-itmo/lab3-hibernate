package commands;

import commands.nativeCommands.Command;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Class that handles an execution of a given command through HashMap, that on given string key, gives out a command instance.
 */
@Getter

public abstract class AbstractCommandProcessor {

    private final HashMap<String, Command> commands = new HashMap<>();
    /**
     * Tries to execute a command with given input.
     *
     * @param input command
     */

    public abstract void executeCommand(String input);
    public abstract ArrayDeque<String> getHistory();
}
