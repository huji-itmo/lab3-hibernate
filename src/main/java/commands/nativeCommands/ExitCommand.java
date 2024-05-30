package commands.nativeCommands;

import commands.exceptions.CommandException;

public class ExitCommand implements commands.nativeCommands.Command {
    @Override
    public String execute(String args) throws CommandException {
        System.out.println("Bye bye!");
        System.exit(0);
        return null;
    }

    @Override
    public String getDescription() {
        return "Exits the application";
    }
}
