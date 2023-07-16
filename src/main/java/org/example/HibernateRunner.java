package org.example;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        User user = User.builder()
                .username("ivan@gmail.com")
                .firstname("Ivan")
                .lastname("Ivanov")
                .build();
        LOG.info("User entity is in transient state, object: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();
                LOG.trace("Transaction is created, {}", transaction);

                session1.saveOrUpdate(user);
                LOG.trace("User is in persistent state: {}, session {}", user, session1);

                session1.getTransaction().commit();
            }
            LOG.warn("User is in detached state: {}, session {}", user, session1);
        } catch (Exception exception) {
            LOG.error("Exception occurred", exception);
            throw exception;
        }
    }
}
