package commands.databaseCommands;

import commands.CommandArgument;
import commands.exceptions.CommandException;
import commands.nativeCommands.OverloadedCommand;
import databaseClasses.Conversation;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.ref.PhantomReference;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowClassCommand<T> implements OverloadedCommand {
    private final SessionFactory factory;
    private final Class<T> clazz;

    @Override
    public String execute(String args) throws CommandException {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            EntityManager em = session.getEntityManagerFactory().createEntityManager();

            CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(clazz);

            query.select(query.from(clazz));

            List<T> list = session.createQuery(query).getResultList();
            session.getTransaction().commit();

            if (list.isEmpty()) {
                return clazz.getSimpleName() + " table is currently empty.";
            }

            return list.stream().map(Objects::toString).map(str -> str + "\n").collect(Collectors.joining());
        }
    }

    @Override
    public String getDescription() {
        return "Prints out " + clazz.getSimpleName() + " table.";
    }

    @Override
    public CommandArgument[] getArguments() {
        return new CommandArgument[] {
                new CommandArgument(clazz.getSimpleName(), "", false)
        };
    }
}
