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