package org.example.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.entity.Payment;
import org.example.entity.User;
import org.hibernate.Session;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    public List<User> findAll(Session session) {
        return session.createQuery("select u from User u", User.class).list();
    }

    public List<User> findAllByFirstName(Session session, String firstName) {
        return session.createQuery("select u from User u " +
                        "where u.personalInfo.firstname = :firstname", User.class)
                .setParameter("firstname", firstName)
                .list();
    }

    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        return session.createQuery("select u from User u order by u.personalInfo.birthDate", User.class)
                .setMaxResults(limit)
//                .setFirstResult(offset)
                .list();
    }

    public List<User> findAllByCompanyName(Session session, String companyName) {
        return session.createQuery("select u from Company c " +
                        "join c.users u " +
                        "where c.name = :companyName", User.class)
                .setParameter("companyName", companyName)
                .list();
    }

    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        return session.createQuery("select p from Payment p " +
                        "join p.receiver u " +
                        "join u.company c " +
                        "where c.name = :companyName " +
                        "order by u.personalInfo.firstname, p.amount", Payment.class)
                .setParameter("companyName", companyName)
                .list();
    }

    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        return session.createQuery("select avg(p.amount) from Payment p " +
                        "join p.receiver u " +
                        "where u.personalInfo.firstname = :firstName " +
                        " and u.personalInfo.lastname = :lastName", Double.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .uniqueResult();
    }

    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        return session.createQuery("select c.name, avg(p.amount) from Company c " +
                        "join c.users u " +
                        "join u.payments p " +
                        "group by c.name " +
                        "order by c.name", Object[].class)
                .list();
    }

    public List<Object[]> isItPossible(Session session) {
        return session.createQuery("select u, avg(p.amount) from User u " +
                        "join u.payments p " +
                        "group by u " +
                        "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
                        "order by u.personalInfo.firstname", Object[].class)
                .list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
