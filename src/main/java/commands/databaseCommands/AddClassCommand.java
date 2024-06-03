package commands.databaseCommands;

import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.OverloadedCommand;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.function.Supplier;

@AllArgsConstructor
public class AddClassCommand implements OverloadedCommand {
    private final SessionFactory factory;
    private final Supplier<Object> objectSupplier;

    @Override
    public String execute(String args) throws CommandException {

        Object obj = objectSupplier.get();

        if (obj == null) {
            throw new CommandException("Failed to create new element.");
        }

        try (Session session =factory.openSession()) {

//            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            session.beginTransaction();
            session.saveOrUpdate(obj);
            session.getTransaction().commit();
            return "Success!";
        } catch (IllegalArgumentException e) {
            throw new CommandException("Wrong class!");
        } catch (javax.persistence.PersistenceException e) {
            throw new CommandException(e.getMessage());

        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[0];
    }
}
