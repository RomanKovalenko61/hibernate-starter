package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Birthday;
import org.example.entity.Company;
import org.example.entity.PersonalInfo;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        Company company = Company.builder()
                .name("Google")
                .build();
        User user = User.builder()
                .username("petr@gmail.com")
                .personalInfo(PersonalInfo
                        .builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .birthDate(new Birthday(LocalDate.of(2000, 1, 2)))
                        .build())
                .company(company)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();

                User user1 = session1.get(User.class, 1L);
                Company company1 = user1.getCompany();
                String name = company1.getName();
//                session1.saveOrUpdate(company);
//                session1.saveOrUpdate(user);
                Object unproxy = Hibernate.unproxy(company1);

                session1.getTransaction().commit();
            }
        }
    }
}
