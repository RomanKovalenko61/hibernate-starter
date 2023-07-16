package org.example;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
//        configuration.addAnnotatedClass(User.class);
//        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure("hibernate.cfg.xml");

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            var user = User.builder()
//                    .username("ivan9@gmail.com")
//                    .firstname("Ivan")
//                    .lastname("Ivanov")
//                    .info("""
//                            {
//                            "name": "Ivan",
//                            "id": 25
//                            }
//                            """)
//                    .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
//                    .role(Role.ADMIN)
//                    .build();
//            session.save(user);
//            session.saveOrUpdate(user);
//            session.delete(user);
            User user1 = session.get(User.class, "ivan@gmail.com");
//            User user2 = session.get(User.class, "ivan@gmail.com");
            user1.setLastname("Petrov");

            System.out.println(session.isDirty());
//            //operation with cache first level
//            session.evict(user1); // delete user from hashmap entitiesByKey
//            session.clear(); //clear hashmap entitiesByKey

            session.getTransaction().commit();
        }
    }
}
