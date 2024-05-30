package commands.databaseCommands;

import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.OverloadedCommand;
import databaseClasses.Person;
import databaseClasses.Robot;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Scanner;

@AllArgsConstructor
public class AddRobotCommand implements OverloadedCommand {
    private final SessionFactory factory;
    @Override
    public String execute(String args) throws CommandException {
        Robot robot = Robot.createFromInput(new Scanner(System.in), factory);

        Session session =factory.openSession();
        session.beginTransaction();
        session.save(robot);

        session.getTransaction().commit();
        session.close();

        return "Success!";
    }

    @Override
    public String getDescription() {
        return "Adds new robot.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument("robot", "the robot that will be added.", false)
        };
    }
}
