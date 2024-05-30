package commands.databaseCommands;

import commands.exceptions.CommandException;
import commands.nativeCommands.Command;
import databaseClasses.Emotion;
import databaseClasses.Robot;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowRobotsCommand implements Command {
    private final SessionFactory factory;
    @Override
    public String execute(String args) throws CommandException {
        Session session = factory.openSession();
        session.beginTransaction();

        List<Robot> list = session.createNativeQuery("select * from robots",
                Robot.class).list();
        String res = list.stream().map(Objects::toString).map(str -> str + "\n").collect(Collectors.joining());

        session.getTransaction().commit();
        session.close();

        return res;
    }

    @Override
    public String getDescription() {
        return "Shows robots";
    }
}
