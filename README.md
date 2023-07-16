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