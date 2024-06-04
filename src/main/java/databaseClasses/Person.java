package databaseClasses;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "people")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "people_id_seq")
    @SequenceGenerator(name="people_id_seq", sequenceName="people_id_seq",allocationSize=1)
    private long id;

    private String name;

    private boolean gender;

    private double strength;

    private double verbosity;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Conversation.class)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @OneToMany(mappedBy = "id", targetEntity= Emotion.class)
    @ToString.Exclude
    private List<Emotion> emotions;

    public static boolean existsAny(SessionFactory factory) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Person> query = builder.createQuery(Person.class);
            query.select(query.from(Person.class));
            return !(session.createQuery(query).getResultList().isEmpty());
        }
    }

    public static Person createFromInput(Scanner scanner, SessionFactory factory) {
        if (!Conversation.existAny(factory)) {
            System.out.println("Can't create new person because conversations table is empty!");
            return null;
        }

        return Person.builder()
                .name(enterName(scanner))
                .gender(enterGender(scanner))
                .strength(enterStrength(scanner))
                .verbosity(enterVerbosity(scanner))
                .conversation(chooseConversation(scanner, factory))
                .build();
    }

    private static String enterName(Scanner scanner) {
        System.out.println("Enter name");
        try {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                System.err.println("Name can't be empty!");
                return enterName(scanner);
            }

            return line;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        return enterName(scanner);
    }

    public static Conversation chooseConversation(Scanner scanner, SessionFactory factory) {
        System.out.println("Choose and enter id from following conversations");

        Session session = factory.openSession();

        session.beginTransaction();
        List<Conversation> list = session.createNativeQuery("select * from conversations;", Conversation.class).list();
        list.forEach(System.out::println);

        session.getTransaction().commit();
        session.close();

        try {
            long id = Long.parseLong(scanner.nextLine());

            return list.stream().filter((conversation) -> conversation.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Choose number that listed!"));

        } catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
            return chooseConversation(scanner, factory);
        }
    }

    private static double enterVerbosity(Scanner scanner) {
        System.out.println("Enter verbosity");
        try {
            double in = Double.parseDouble(scanner.nextLine());
            if (in < 0 || in > 10) {
                System.err.println("Verbosity can't be negative or greater that zero!");
                return enterVerbosity(scanner);
            }

            return in;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        return enterVerbosity(scanner);
    }

    private static double enterStrength(Scanner scanner) {
        System.out.println("Enter strength");
        try {
            double in = Double.parseDouble(scanner.nextLine());
            if (in < 0 || in > 10) {
                System.err.println("Strength can't be negative or greater that zero!");
                return enterStrength(scanner);
            }

            return in;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        return enterStrength(scanner);
    }

    private static boolean enterGender(Scanner scanner) {
        System.out.println("Enter gender. True for male, false for women");

        try {
            return Boolean.parseBoolean(scanner.nextLine());
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        return enterGender(scanner);
    }


}
