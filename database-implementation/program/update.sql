-- Part 2.4 update.sql
--

-- Add hourly salary to Coaches and drop daily salary
ALTER TABLE Coach ADD hourlySalary DECIMAL(6,2) UNSIGNED;

UPDATE Coach
SET hourlySalary = (dailySalary/4);
ALTER TABLE Coach DROP dailySalary;

-- Add hourly salary to Participant and drop daily salary
ALTER TABLE Participant ADD hourlySalary DECIMAL(6,2) UNSIGNED;

UPDATE Participant
SET hourlySalary = (dailySalary/4);
ALTER TABLE Participant DROP dailySalary;

-- Add arrival and depart time to CoachInShow
ALTER TABLE CoachInShow ADD arrivalTime TIME;
ALTER TABLE CoachInShow ADD departTime TIME;

-- Set arrival and departure times
UPDATE CoachInShow
SET arrivalTime = (
  SELECT TVShow.startTime
  FROM TVShow
  WHERE TVShow.idShow = CoachInShow.idShow
) - 10000,
departTime = (
  SELECT TVShow.endTime
  FROM TVShow
  WHERE TVShow.idShow = CoachInShow.idShow
) + 10000;

-- Add arrival and depart time to ContenderInShow
ALTER TABLE ContenderInShow ADD arrivalTime TIME;
ALTER TABLE ContenderInShow ADD departTime TIME;

-- Set arrival and departure times
UPDATE ContenderInShow
SET arrivalTime = (
  SELECT TVShow.startTime
  FROM TVShow
  WHERE TVShow.idShow = ContenderInShow.idShow
) - 10000,
departTime = (
  SELECT TVShow.endTime
  FROM TVShow
  WHERE TVShow.idShow = ContenderInShow.idShow
) + 10000;


-- DO NOT use these SQL commands in your submission(they will cause an
--  error on the NMS database server):
-- CREATE SCHEMA
-- USE
