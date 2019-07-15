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
VALUES ('Russia', 'Moscow','8 Marta 10-5', '117623', (SELECT contact_id FROM contact WHERE surname = 'Petrov' AND name = 'Ivan'));

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Belarus', 'Minsk','Dvinskaya 15-23', '220037', (SELECT contact_id FROM contact WHERE surname = 'Komarov' AND name = 'Pavel'));

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Russia', 'Moscow','Taganskaya 34A', '109147', (SELECT contact_id FROM contact WHERE surname = 'Lodochkin' AND name = 'Yuri'));

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('USA', 'New York','247 Cessna Drive', '46818', (SELECT contact_id FROM contact WHERE surname = 'Solnceva' AND name = 'Irina'));

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Russia', 'SPB','Sedova 11', '192019', (SELECT contact_id FROM contact WHERE surname = 'Kalinina' AND name = 'Maria'));

INSERT INTO address (country, city, street, postcode, contact_id) 
VALUES ('Belarus', 'Mogilev','Minina 13-1', '212037', (SELECT contact_id FROM contact WHERE surname = 'Zinina' AND name = 'Karina'));

/*insert phone*/

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '495','7556983', 'home', NULL, (SELECT contact_id FROM contact WHERE surname = 'Petrov' AND name = 'Ivan'));

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+375', '29','3278567', 'mobile', NULL, (SELECT contact_id FROM contact WHERE surname = 'Komarov' AND name = 'Pavel'));

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '499','7345465', 'home', NULL, (SELECT contact_id FROM contact WHERE surname = 'Lodochkin' AND name = 'Yuri'));

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '926','4564778', 'mobile', NULL, (SELECT contact_id FROM contact WHERE surname = 'Lodochkin' AND name = 'Yuri'));

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('1', '212','3482626', 'home', NULL, (SELECT contact_id FROM contact WHERE surname = 'Solnceva' AND name = 'Irina'));

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+7', '903','5467667', 'mobile', NULL, (SELECT contact_id FROM contact WHERE surname = 'Kalinina' AND name = 'Maria'));

INSERT INTO phone (country_code, operator_code, phone_number, phone_type, COMMENT, contact_id) 
VALUES ('+375', '33','3455654', 'mobile', NULL, (SELECT contact_id FROM contact WHERE surname = 'Zinina' AND name = 'Karina'));

/*insert attachment*/
INSERT INTO attachment (attachment_name, attachment_link, upload_date, contact_id) 
VALUES ('passport', 'https://clck.ru/GP4ri', CURDATE(), (SELECT contact_id FROM contact WHERE surname = 'Petrov' AND name = 'Ivan'));


