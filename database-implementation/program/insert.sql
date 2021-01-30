-- Part 2.2 insert.sql
--


  -- Coaches


INSERT INTO Coach
(name, surname, DoB, phone, dailySalary, gender)
VALUES
('Michael', 'Kolling', STR_TO_DATE('10-06-1975', '%d-%m-%Y'), '07965485231', 500.00, 'M');

INSERT INTO Coach
(name, surname, DoB, phone, dailySalary, gender)
VALUES
('Stefan', 'Edelkamp', STR_TO_DATE('25-12-1990', '%d-%m-%Y'), '+23354685626', 250.01, 'M');

INSERT INTO Coach
(name, surname, DoB, phone, dailySalary, gender)
VALUES
('Agi', 'Kurucz', STR_TO_DATE('01-04-1950', '%d-%m-%Y'), '+8506455855', 300.00, 'F');


-- Contenders


INSERT INTO Contender
(stageName, type, idCoach)
VALUES
('Finite Automaton', 'Individual', 1);

INSERT INTO Contender
(stageName, type, idCoach)
VALUES
('Memory $Cache$ Gang', 'Group', 2);

INSERT INTO Contender
(stageName, type, idCoach)
VALUES
('Tick', 'Individual', 2);

INSERT INTO Contender
(stageName, type, idCoach)
VALUES
('But can Eclispse do THIS, Bluej only $3.99', 'Individual', 1);

INSERT INTO Contender
(stageName, type, idCoach)
VALUES
('Smart Cypto and Stuff', 'Group', 1);


  -- Participants


INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Hongbin', 'Liu', STR_TO_DATE('01-10-1933', '%d-%m-%Y'), '355645564', 01.01, 'M', 2);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Matthew', 'Howard', STR_TO_DATE('10-01-1966', '%d-%m-%Y'), '48454545', 10.01, 'F', 2);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Sanjay', 'Modgil', STR_TO_DATE('18-09-1982', '%d-%m-%Y'), '07645486445', 99.99, 'M', 5);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Laurence', 'Tratt', STR_TO_DATE('29-11-1990', '%d-%m-%Y'), '548454534', 88.60, 'M', 1);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Grigorios', 'Loukides', STR_TO_DATE('01-08-2000', '%d-%m-%Y'), '+246451878', 66.99, 'F', 3);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Ali', 'Asad', STR_TO_DATE('10-01-1969', '%d-%m-%Y'), '+4484515845', 03.99, 'F', 4);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Jeffrey', 'Raphael', STR_TO_DATE('11-04-1987', '%d-%m-%Y'), '23156484556', 80.00, 'M', 5);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Simon', 'Miles', STR_TO_DATE('19-10-1974', '%d-%m-%Y'), '+8484648754', 62.96, 'F', 5);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Luca', 'Vigano', STR_TO_DATE('09-09-1990', '%d-%m-%Y'), '1545487845', 60.99, 'M', 5);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Ernest', 'Kamavuako', STR_TO_DATE('19-01-1919', '%d-%m-%Y'), '2154544231', 50.50, 'M', 2);


-- TVShows

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('04-03-2017', '%d-%m-%Y'), '11:20:00', '13:20:00', 'London');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('05-03-2017', '%d-%m-%Y'), '17:00:00', '19:20:00', 'Glasgow');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('11-03-2017', '%d-%m-%Y'), '14:45:00', '16:45:00', 'Llanfairpwllgwyngyll');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('12-03-2017', '%d-%m-%Y'), '01:20:00', '03:20:00', 'Manchester');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('18-03-2017', '%d-%m-%Y'), '19:00:00', '21:00:00', 'Leeds');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('19-03-2017', '%d-%m-%Y'), '21:30:00', '22:30:00', 'Manchester');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('25-03-2017', '%d-%m-%Y'), '17:20:00', '19:20:00', 'Glasgow');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('26-03-2017', '%d-%m-%Y'), '17:20:00', '19:20:00', 'Llanfairpwllgwyngyll');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('01-04-2017', '%d-%m-%Y'), '20:00:00', '22:00:00', 'London');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('02-04-2017', '%d-%m-%Y'), '23:20:00', '23:20:00', 'London');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('08-04-2017', '%d-%m-%Y'), '20:45:00', '22:45:00', 'Llanfairpwllgwyngyll');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('09-04-2017', '%d-%m-%Y'), '19:15:00', '21:15:00', 'Llanfairpwllgwyngyll');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('15-04-2017', '%d-%m-%Y'), '01:00:00', '13:00:00', 'Leeds');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('16-04-2017', '%d-%m-%Y'), '02:10:00', '04:10:00', 'Leeds');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('22-04-2017', '%d-%m-%Y'), '12:00:00', '14:00:00', 'Manchester');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('23-04-2017', '%d-%m-%Y'), '19:45:00', '21:45:00', 'Glasgow');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('29-04-2017', '%d-%m-%Y'), '20:20:00', '22:20:00', 'Llanfairpwllgwyngyll');

INSERT INTO TVShow
(date, startTime, endTime, location)
VALUES
(STR_TO_DATE('30-04-2017', '%d-%m-%Y'), '10:20:00', '12:20:00', 'Leeds');


  -- Coaches in Shows

-- 1
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 1);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 1);

-- 2
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 2);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 2);

-- 3
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 3);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 3);

-- 4
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 4);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 4);

-- 5
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 5);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 5);

-- 6
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 6);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 6);

-- 7
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 7);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 7);

-- 8
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 8);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 8);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 8);

-- 9
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 9);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 9);

-- 10
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 10);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 10);

-- 11
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 11);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 11);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 11);

-- 12
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 12);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 12);

-- 13
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 13);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 13);

-- 14
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 14);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 14);

-- 15
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 15);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 15);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 15);

-- 16
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 16);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 16);

-- 17
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 17);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(3, 17);

-- 18
INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(1, 18);

INSERT INTO CoachInShow
(idCoach, idShow)
VALUES
(2, 18);


  -- Contenders in Shows

-- 1
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 1);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 1);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 1);

-- 2
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 2);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 2);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 2);

-- 3
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 3);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 3);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 3);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 3);

-- 4
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 4);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 4);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 4);

-- 5
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 5);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 5);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 5);

-- 6
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 6);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 6);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 6);

-- 7
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 7);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 7);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 7);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 7);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 7);

-- 8
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 8);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 8);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 8);

-- 9
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 9);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 9);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 9);

-- 10
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 10);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 10);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 10);

-- 11
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 11);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 11);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 11);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 11);

-- 12
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 12);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 12);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 12);

-- 13
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 13);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 13);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 13);

-- 14
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 14);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 14);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 14);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 14);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 14);

-- 15
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 15);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 15);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 15);

-- 16
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 16);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 16);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 16);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(5, 16);

-- 17
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(1, 17);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 17);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 17);

-- 18
INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(2, 18);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(3, 18);

INSERT INTO ContenderInShow
(idContender, idShow)
VALUES
(4, 18);


-- DO NOT use these SQL commands in your submission(they will cause an
--  error on the NMS database server):
-- CREATE SCHEMA
-- USE
