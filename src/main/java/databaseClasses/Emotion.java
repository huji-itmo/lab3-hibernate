package databaseClasses;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Scanner;

@Data
@Entity
@Table(name = "Emotions")
@NoArgsConstructor
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emotions_id_seq")
    @SequenceGenerator(name="emotions_id_seq", sequenceName="emotions_id_seq",allocationSize=1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Person.class)
    @JoinColumn(name = "person_id")
    private Person person;

    private double joy;
    private double fear;
    private double despair;

    public Emotion(Person person, double joy, double fear, double despair) {
        this.person = person;
        this.joy = joy;
        this.fear = fear;
        this.despair = despair;
    }

    public static Emotion createFromInput(Scanner scanner, SessionFactory factory) {
        if (!Person.existsAny(factory)) {
            System.out.println("Can't create new emotion because people table is empty!");

            return null;
        }

        return new Emotion(choosePerson(scanner, factory), enterJoy(scanner), enterFear(scanner), enterDespair(scanner));
    }

    private static double enterJoy(Scanner scanner) {
        System.out.println("Enter joy");
        try {
            double in = Double.parseDouble(scanner.nextLine());
            if (in < 0 || in > 10) {
                System.err.println("Joy can't be negative or greater that zero!");
                return enterJoy(scanner);
            }

            return in;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        return enterJoy(scanner);
    }

    private static double enterFear(Scanner scanner) {
        System.out.println("Enter fear");
        try {
            double in = Double.parseDouble(scanner.nextLine());
            if (in < 0 || in > 10) {
                System.err.println("Fear can't be negative or greater that zero!");
                return enterFear(scanner);
            }

            return in;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        return enterFear(scanner);
    }

    private static double enterDespair(Scanner scanner) {
        System.out.println("Enter despair");
        try {
            double in = Double.parseDouble(scanner.nextLine());
            if (in < 0 || in > 10) {
                System.err.println("Despair can't be negative or greater that zero!");
                return enterDespair(scanner);
            }

            return in;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        return enterDespair(scanner);
    }


    private static Person choosePerson(Scanner scanner, SessionFactory factory) {
        System.out.println("Choose and enter id from following people");

        try (Session session = factory.openSession()){
            CriteriaQuery<Person> query = session.getCriteriaBuilder().createQuery(Person.class);

            query.select(query.from(Person.class));
            List<Person> list = session.createQuery(query).getResultList();

            list.forEach(System.out::println);
            try {
                long id = Long.parseLong(scanner.nextLine());

                return list.stream().filter((person) -> person.getId() == id)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Choose number that listed!"));

            } catch (IllegalArgumentException e){
                System.err.println(e.getMessage());
                return choosePerson(scanner,factory);
            }
        }
    }
}
