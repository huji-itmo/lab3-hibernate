package commands.databaseCommands;

import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.Command;
import commands.nativeCommands.OverloadedCommand;
import databaseClasses.Conversation;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ExecCommand implements OverloadedCommand {
    private final SessionFactory factory;
    @Override
    public String execute(String args) throws CommandException {
        Session session =  factory.openSession();

        try {
            session.beginTransaction();
            List<?> list = session.createNativeQuery(args).list();
            session.getTransaction().commit();
            session.close();

            return list.stream().map(Object::toString).map(str -> str + "\n").collect(Collectors.joining());
        } catch (PersistenceException e) {
            session.cancelQuery();
            session.close();
            throw new CommandException(e.getMessage()+ ": " + e.getCause().getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Executes HQL";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("HQL string", "", false)
        };
    }
}
