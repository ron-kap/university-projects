-- Part 2.3 select.sql
--

-- DO NOT use these SQL commands in your submission(they will cause an
--  error on the NMS database server):
-- CREATE SCHEMA
-- USE


-- 1. Average Female Salary

SELECT AVG(dailySalary) as Average_Female_Salary
FROM Participant
WHERE gender = 'F';
-- No rounding to the nearest penny, in GBP - returns null if none found.

-- 2. Coaching Report.

SELECT Coach.name AS Coach_Name, COUNT(Contender.idCoach) AS Number_Of_Contenders
FROM Coach
LEFT JOIN Contender
ON (Coach.idCoach = Contender.idCoach)
GROUP BY Coach.idCoach;

-- 3. Coach Monthly Attendance Report

SELECT Coach.idCoach AS Coach_ID, Coach.name AS Coach_Name, COUNT(CoachInShow.idCoach) AS March_Attendance, NULL AS April_Attendance
FROM Coach, CoachInShow, TVShow
WHERE CoachInShow.idShow = TVShow.idShow AND MONTH(TVShow.date) = 3 AND Coach.idCoach = CoachInShow.idCoach
GROUP BY Coach.idCoach

UNION ALL

SELECT Coach.idCoach AS Coach_ID, Coach.name AS Coach_Name, NULL AS March_Attendance, COUNT(CoachInShow.idCoach) AS April_Attendance
FROM Coach, CoachInShow, TVShow
WHERE CoachInShow.idShow = TVShow.idShow AND MONTH(TVShow.date) = 4 AND Coach.idCoach = CoachInShow.idCoach
GROUP BY Coach.idCoach;


-- 4. Most Expensive Contender

SELECT Contender.idContender AS Contender_ID, Contender.stageName AS Stage_Name, SUM(Participant.dailySalary) AS Highest_Salary
FROM Participant, Contender
WHERE Participant.idContender = Contender.idContender
GROUP BY Participant.idContender
ORDER BY dailySalary DESC LIMIT 1;

-- 5. Well Formed Groups!

-- Test for below query, left in
INSERT INTO Contender
(stageName, type, idCoach)
VALUES
('Test Contender', 'Group', 2);

INSERT INTO Participant
(name, surname, DoB, phone, dailySalary, gender, idContender)
VALUES
('Bob', 'TEST', STR_TO_DATE('20-05-1980', '%d-%m-%Y'), '07976543231', 100.00, 'M', 6);

-- Well formed query
SELECT IF (COUNT(*) > 0, 'True', 'False') AS Violation
FROM Contender
WHERE type = 'Group' AND (
  SELECT COUNT(*)
  FROM Participant
  WHERE Participant.idContender = Contender.idContender) <2;
