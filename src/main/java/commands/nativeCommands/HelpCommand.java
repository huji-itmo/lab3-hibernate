package commands.nativeCommands;

import commands.AbstractCommandProcessor;
import commands.CommandArgument;
import commands.exceptions.CommandDoesntExistsException;
import commands.exceptions.CommandException;
import commands.exceptions.IllegalCommandSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HelpCommand implements OverloadedCommand {
    private final AbstractCommandProcessor commandProcessor;

    public HelpCommand(AbstractCommandProcessor processor) {
        commandProcessor = processor;
    }
    @Override
    public String execute(String args) throws CommandException {
        if (args.isBlank()) {
            return showAllCommands();
        }
        else if (!args.contains(" ")) {
            if (!commandProcessor.getCommands().containsKey(args)) {
                throw new CommandDoesntExistsException(args);
            }

            return showInfoAboutCommand(commandProcessor.getCommands().get(args));
        }
        else {
            throw new IllegalCommandSyntaxException("Help command expects only one or none arguments.", this);
        }
    }

    public String showInfoAboutCommand(Command command) {
        StringBuilder builder = new StringBuilder(command.getDescription());

        if (!(command instanceof OverloadedCommand)) {
            return builder.toString();
        }

        builder.append("\n\n    Syntax: ").append(command.getArgumentsSyntax()).append("\n");

        CommandArgument[] arguments = ((OverloadedCommand) command).getArguments();

        for (CommandArgument argument : arguments) {
            builder.append("\n")
                    .append(" ".repeat(4))
                    .append(argument.getName())
                    .append(argument.isOptional()? " (optional)" : "")
                    .append(" - ")
                    .append(argument.getDescription());
        }

        return builder.toString();
    }

    public String showAllCommands() {
        List<String> list = new ArrayList<>();

        for(Map.Entry<String, Command> entry: commandProcessor.getCommands().entrySet()) {
            String line =entry.getKey();

            if (entry.getValue() instanceof OverloadedCommand) {
                CommandArgument[] arguments = ((OverloadedCommand) entry.getValue()).getArguments();

                for (CommandArgument commandArgument : arguments) {
                    line += " " + commandArgument;
                }
            }

            line += "\n";
            list.add(line);
        }

        return list.stream().sorted().collect(Collectors.joining());
    }

    @Override
    public String getDescription() {
        return "Shows all commands if no arguments provided. Displays info about command you provided.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("command_name", "Name of the command you want to know more about.", true)
        };
    }
}
