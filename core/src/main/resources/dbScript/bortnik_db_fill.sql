/*insert contact*/

INSERT INTO contact (surname, NAME, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) 
VALUES ('Petrov', 'Ivan','Sergeevich', '1989/01/01', 'male', 'russian', 'single', 'www.google.com', 'petrovivan@gmail.com', 'Google', null);

INSERT INTO contact (surname, NAME, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) 
VALUES ('Komarov', 'Pavel','Ivanovich', '1997/03/05', 'male', 'belarusian', 'married', 'www.yandex.com', 'komarovpavel@yandex.ru', 'Yandex', null);

INSERT INTO contact (surname, NAME, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) 
VALUES ('Lodochkin', 'Yuri','Varfolomeevich', '1978/04/26', 'male', 'russian', 'divorced', 'www.itechart.com', 'lodochkinyuri@gmail.com', 'iTechArt', null);

INSERT INTO contact (surname, NAME, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) 
VALUES ('Solnceva', 'Irina', NULL , '1991/05/09', 'female', 'belarusian', 'single', 'www.itechart.com', 'irinasolnceva@mail.ru', 'iTechArt', null);

INSERT INTO contact (surname, NAME, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) 
VALUES ('Kalinina', 'Maria','Vasilevna', '1988/06/10', 'female', 'russian', 'married', 'www.epam.com', 'kalininamaria@gmail.com', 'Epam', null);

INSERT INTO contact (surname, NAME, patronymic, birthday, gender, nationality, marital_status, website, email, workplace, photo_link) 
VALUES ('Zinina', 'Karina','Andreevna', '1985/07/05', 'female', 'belarusian', 'divorced', 'www.google.com', 'karinazinina@gmail.com', 'Google', null);

/*insert address*/

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Russia', 'Moscow','8 Marta 10-5', '117623', 1);

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Belarus', 'Minsk','Dvinskaya 15-23', '220037', 2);

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Russia', 'Moscow','Taganskaya 34A', '109147', 3);

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('USA', 'New York','247 Cessna Drive', '46818', 4);

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Russia', 'SPB','Sedova 11', '192019', 5);

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Belarus', 'Mogilev','Minina 13-1', '212037', 6);

/*insert phone*/

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '495','7556983', 'home', NULL, 1);

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+375', '29","3278567', 'mobile', NULL, 2);

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '499','7345465', 'home', NULL, 3);

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '926','4564778', 'mobile', NULL, 3);

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('1', '212','3482626', 'home', NULL, 4);

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '903','5467667', 'mobile', NULL, 5);

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+375', '33','3455654', 'mobile', NULL, 6);

/*insert attachment*/
INSERT INTO attachment (attachment_name, attachment_link, upload_date, contact_id) 
VALUES ('passport', 'https://clck.ru/GP4ri', CURDATE(), 1);


