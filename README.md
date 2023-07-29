# Pet project hibernate + gradle

java-version: 17

hibernate-version: 5.5.6 Final

В 6 версии есть deprecated аннотации, используемые в коде!

gradle-version: 8.2.1  

-----------------------------------

Intellij Idea кириллица в консоли Что бы исправить Help menu -> Edit Custom VM Options добавляем в конец

> -Dconsole.encoding=UTF-8
>
> -Dfile.encoding=UTF-8

-----------------------------------

Создание образа postgresql 
> $ docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -p5433:5432 -d postgres
>
> $ docker ps

-----------------------------------

interface EntityPersister -> mapped sql query with ours entity.

1. Evaluate expression: getFactory().getMetamodel()
2. see field entityPersisterMap (keeps all ours entity) key -> entity value -> SingleTableEntityPersister
3. see field typeConfiguration (keeps all ours types: basic and added: 
for example configuration.registerTypeOverride(new JsonBinaryType());

----------------------------------------
field persistenceContext into session -> first level cache for each session
    внутри persistenceContext hashMap entitiesByKey -> хранит сущности по ключу 
Если в сессии два раза запросим одну и ту же сущность то выполнится один запрос к базе
При session.close кеш очищается.
Любое изменение сущности связанное с persistenceContext отразится на базе данных.
Понятие dirty session

---------------------------------------------

Entity lifecycle: transient, persistent, detached, removed.
Отложенное выполнение запросов в Hibernate. Метод flush()

-----------------------------------------------

session.refresh(entity) -> синх. состояние entity с базой данных (поместить в persistenceContext если entity не было)
session.merge(entity) -> сохр. измененное состояние entity в базу данных (поместить в persistenceContext если entity не было)

------------------------------------------------


> Java Persistence API (JPA) - спецификация Java, которая представляет набор интерфейсов/аннотаций
> для возможности сохранять в удобном виде Java объекты в базу данных и наоборот извлекать информацию
> из баз данных в виде Java объектов (ORM)
>
> Hibernate (ORM framefork) - одна за самых распространенных JPA реализаций.

См. find <-> get  OR save <-> persist

---
Логирование

slf4j -> binding -> log4j OR jdk14l OR simple

Паттерн вывода
https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html

Log levels:
FATAL <- ERROR <- WARN <- INFO <- DEBUG <- TRACE

---

Embedded components -> class ComponentType

Составные первичные ключи

---

В persistent сущности должен быть id
Разное поведение hibernate когда таблица сама генерирует id или когда мы управляем sequence
См. дебаг

---

@Access(AccessType.FIELD) - по умолчанию
@Access(AccessType.PROPERTY) -> устарел, аннотации над геттерами

@Transient - не сериализовать поле

@Temporal(TemporalType.TIMESTAMP)
private Date date;
TIMESTAMP -> localDateTime
DATE -> localDate
TIME -> localTime

@ColumnTransformers, @Formula

---

@ManyToOne(optional = false, fetch = FetchType.LAZY)
В зависимости от optional выполнится outer/inner join

---

CollectionType into Hibernate (для работы с OneToMany)

---

интерфейс LazyInitializer
(почитать) Закрытие сессии на уровне сервисов

---

чтобы заменить LEFT OUTER JOIN на INNER JOIN нужно добавить @ManyToOne(optional=false)

---

почитать про PersistentBag List and Collection

---

почитать https://h2database.github.io/html/features.html#products_work_with 
почитать про migration frameworks: FlyWay OR Liquibase
пока используем генератор хибернейта. !Неоходимо перепроверять за ним!
Стратегии генерации ddl
<property name="hibernate.hbm2ddl.auto">(create, create-drop, update, validate)</property>

https://9to5answer.com/h2-database-null-not-allowed-for-column-quot-id-quot-when-inserting-record-using-jdbctemplate

---

https://java.testcontainers.org/

---
var manager11 = session.get(User.class, 2L) смотри запрос. использует UNION ALL
д.б. одинаковое кол-во столбцов в таблицах, поэтому к программисту добавится null::varchar as projectName, а к менеджеру null::varchar as language
столбец для определения сущности (напр.1 - программист, 2 - менеджер) чтобы знать какую именно создавать.

---
СТРАТЕГИИ МАППИНГА НАСЛЕДОВАНИЯ В ХИБЕРНЕЙТ
---

##TABLE_PER_CLASS

минусы: 
-

- дублируем общие поля в обоих таблицах. Если необходимо добавить общее поле, меняем обе таблицы
- если получаем список всех сущностей, то используем UNION ALL
- используется общий sequence для всех таблиц

плюсы: 
- 
если нужен конкретный наследник, обращаемся только к нужной таблице (Напр. программист)

---
##SINGLE_TABLE (используется по умолчанию)

минусы:
-
- общая таблица со всеми полями наследников, нет возможности наложить специализированный для отдельной сущности constraint 
- низкая селективность колонки type сколько наследников столько и вариантов значений поля type

плюсы: 
- 
- простая в реализации
- можем использовать identity, а не sequence
- запрос к единственной таблице

---
##JOINED

минусы:
-
- изменения вносятся в две таблицы общие поля и специализированные
- для получения конкретной сущности используется INNER JOIN на базовую таблицу
- получение всех сущностей LEFT OUTER JOIN на обе таблицы

плюсы:
- 
- данные нормализованы, сущности в своих таблицах