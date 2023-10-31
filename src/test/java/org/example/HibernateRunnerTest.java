package org.example;

import lombok.Cleanup;
import org.example.entity.*;
import org.example.util.HibernateTestUtil;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.FlushModeType;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkHql() {
        try (var factory = HibernateTestUtil.buildSessionFactory();
             var session = factory.openSession()) {
            session.beginTransaction();

//            HQL / JPQL
//            select u.* from users u where u.firstname = 'Ivan'
            String name = "Ivan";
            var result = session.createNamedQuery(
                    "findUserByName", User.class)
//                    .setParameter(1, name)
                    .setParameter("firstname", name)
                    .setParameter("companyName", "Google")
                    .setFlushMode(FlushModeType.COMMIT)
                    .setHint(QueryHints.FETCH_SIZE, "50")
                    .list();

            var countRows = session.createQuery("update User u set u.role = 'ADMIN'")
                    .executeUpdate();

            session.createNativeQuery("select u.* from users u where u.firstname = 'Ivan'", User.class);

            session.getTransaction().commit();
        }
    }

    @Test
    void LocaleInfo() {
        try (var factory = HibernateUtil.buildSessionFactory();
             var session = factory.openSession()) {
            session.beginTransaction();

            var company = session.get(Company.class, 1);
//            company.getLocales().add(LocaleInfo.of("ru", "Описание на русском"));
//            company.getLocales().add(LocaleInfo.of("en", "English description"));
//            System.out.println(company.getLocales());
            company.getUsers().forEach((k, v) -> System.out.println(v));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToMany() {
        try (var factory = HibernateUtil.buildSessionFactory();
             var session = factory.openSession()) {
            session.beginTransaction();

            var user = session.get(User.class, 4L);
            var chat = session.get(Chat.class, 1L);

            var userChat = UserChat.builder()
//                    .createdAt(Instant.now())
//                    .createdBy(user.getUsername())
                    .build();
            userChat.setUser(user);
            userChat.setChat(chat);

            session.save(userChat);

//            user.getChats().clear();

//            var chat = Chat.builder()
//                    .name("chatik")
//                    .build();
//            user.addChat(chat);
//
//            session.save(chat);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        try (var factory = HibernateUtil.buildSessionFactory();
             var session = factory.openSession()) {
            session.beginTransaction();

            var user = session.get(User.class, 4L);
            System.out.println();

//            var user = User.builder()
//                    .username("test4@gmail.com")
//                    .build();
//            var profile = Profile.builder()
//                    .language("ru")
//                    .street("broadway")
//                    .build();
//            profile.setUser(user);
//            session.save(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrphanRemoval() {
        try (var factory = HibernateUtil.buildSessionFactory();
             var session = factory.openSession()) {
            session.beginTransaction();

            var company = session.get(Company.class, 2);
//            company.getUsers().removeIf(user -> user.getId().equals(5L));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company = null;
        try (var factory = HibernateUtil.buildSessionFactory();
             var session = factory.openSession()) {
            session.beginTransaction();

//            company = session.get(Company.class, 2);
            company = session.getReference(Company.class, 2); //get HibernateProxy

            session.getTransaction().commit();
        }
//        Set<User> users = company.getUsers();
//        System.out.println(users.size());
    }

    @Test
    void getCompanyById() {
        @Cleanup SessionFactory factory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = factory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);
//        Hibernate.initialize(company.getUsers());
        System.out.println();

        session.getTransaction().commit();
    }

    @Test
    void deleteCompany() {
        @Cleanup SessionFactory factory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = factory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 2);
        session.delete(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup SessionFactory factory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = factory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Facebook")
                .build();

//        User user = User.builder()
//                .username("sveta@gmail.com")
//                .build();
//        user.setCompany(company);
//        company.getUsers().add(user);
//        company.addUser(user);

        session.save(company);


        session.getTransaction().commit();
    }

    @Test
    void checkedGetReflectionApi() throws
            SQLException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("firstname");
        resultSet.getString("lastname");

        Class<User> clazz = User.class;

        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = null;

        String sql = """
                insert
                into
                %s
                (%s)
                values
                (%s)
                """;
        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

//        Connection connection = null;
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        for (Field declaredField : declaredFields) {
//            declaredField.setAccessible(true);
//            preparedStatement.setObject(1, declaredField.get(user));
//        }
    }
}