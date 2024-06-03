package commands.databaseCommands;

import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.OverloadedCommand;
import databaseClasses.Emotion;
import databaseClasses.Person;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import java.util.Scanner;

@AllArgsConstructor
public class AddEmotionCommand implements OverloadedCommand {
    private final SessionFactory factory;
    @Override
    public String execute(String args) throws CommandException {
        Emotion emotion = Emotion.createFromInput(new Scanner(System.in), factory);

        Session session =factory.openSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        session.beginTransaction();
        em.merge(emotion);

        session.getTransaction().commit();
        session.close();

        return "Success!";
    }

    @Override
    public String getDescription() {
        return "Adds new emotion.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("emotion", "the emotion that will be added.", false)
        };
    }
}
