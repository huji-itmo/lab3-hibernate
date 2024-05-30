package commands.databaseCommands;

import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.Command;
import commands.nativeCommands.OverloadedCommand;
import databaseClasses.Conversation;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Scanner;

@AllArgsConstructor
public class AddConversationCommand implements OverloadedCommand {
    private final SessionFactory factory;
    @Override
    public String execute(String args) throws CommandException {
        Conversation conversation = Conversation.createFromInput(new Scanner(System.in));

        Session session =factory.openSession();
        session.beginTransaction();
        session.save(conversation);

        session.getTransaction().commit();
        session.close();

        return "Success!";
    }

    @Override
    public String getDescription() {
        return "Adds new conversation.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("conversation", "the conversation that will be added.", false)
        };
    }
}
