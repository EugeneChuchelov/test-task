INSERT INTO DOCTOR (ID, FIRST_NAME, LAST_NAME, SECOND_NAME, SPECIALIZATION)
VALUES (1, 'Олег', 'Петров', 'Сергеевич', 'Терпевт'), (2, 'Егор', 'Летов', 'Сидорович', 'Хирург');

INSERT INTO PATIENT (ID, FIRST_NAME, LAST_NAME, SECOND_NAME, PHONE_NUMBER) VALUES
(1, 'Ирина', 'Каштанова', 'Олеговна', '89274928602'),
(2, 'Василий', 'Григорьев', 'Григорьевич', '88005553535');

INSERT INTO RECIPE (ID, DESCRIPTION, PATIENT, DOCTOR, CREATION_DATE, EXPIRATION_DATE, PRIORITY) VALUES
(1, 'Антижопаболин 5 мг 11 раз в день', 1, 1, '2020-08-13', '2020-10-13', 'CITO'),
(2, 'Ампутация головы', 2, 2, '2020-08-13', '2020-08-14', 'STATIM'),
(3, 'Ежедневные перевязки', 2, 1, '2020-08-27', '2020-09-17', 'STANDARD'),
(4, 'Монооксид дигидрогена 3000-4000 см2 в день', 1, 2, '2020-08-14', '2020-11-20', 'STANDARD')