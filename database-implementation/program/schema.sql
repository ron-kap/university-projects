-- Part 2.1 schema.sql
--


CREATE TABLE Coach (
	idCoach		INTEGER		UNSIGNED	NOT NULL	AUTO_INCREMENT,
	name			VARCHAR(15)	NOT NULL,
	surname		VARCHAR(15),
	DoB			DATE,
	phone		VARCHAR(20),
	dailySalary	DECIMAL(6,2) UNSIGNED,
	gender		CHAR(1),
	PRIMARY KEY(idCoach)
);

CREATE TABLE Contender (
	idContender	INTEGER		UNSIGNED	NOT NULL	AUTO_INCREMENT,
	stageName	VARCHAR(100)	NOT NULL,
	`type`		VARCHAR(10)	NOT NULL,
	idCoach		INTEGER		UNSIGNED	NOT NULL,
	PRIMARY KEY(idContender),
	FOREIGN KEY(idCoach) REFERENCES Coach(idCoach)	ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Participant (
	idParticipant INTEGER	UNSIGNED	NOT NULL	AUTO_INCREMENT,
	name		VARCHAR(15)	NOT NULL,
	surname		VARCHAR(15)	NOT NULL,
	DoB			DATE,
	phone		VARCHAR(20),
	dailySalary	DECIMAL(6,2) UNSIGNED,
	gender		CHAR(1),
	idContender	INTEGER		UNSIGNED	NOT NULL,
	PRIMARY KEY(idParticipant),
	FOREIGN KEY(idContender) REFERENCES Contender(idContender)	ON DELETE CASCADE
);

CREATE TABLE TVShow (
	idShow		INTEGER		UNSIGNED	NOT NULL	AUTO_INCREMENT,
	`date`		DATE,
	startTime	TIME		NOT NULL,
	endTime		TIME		NOT NULL,
	location	VARCHAR(50),
	-- Can be null if located in Television Studio
	PRIMARY KEY(idShow)
);

CREATE TABLE CoachInShow (
	idCoach		INTEGER		UNSIGNED	NOT NULL,
	idShow		INTEGER		UNSIGNED	NOT NULL,
	PRIMARY KEY(idCoach, idShow),
	FOREIGN KEY(idCoach) REFERENCES Coach(idCoach)	ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY(idShow) REFERENCES TVShow(idShow)	ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE ContenderInShow (
	idContender	INTEGER		UNSIGNED	NOT NULL,
	idShow		INTEGER		UNSIGNED	NOT NULL,
	PRIMARY KEY(idContender, idShow),
	FOREIGN KEY(idContender) REFERENCES Contender(idContender)	ON DELETE CASCADE,
	FOREIGN KEY(idShow) REFERENCES TVShow(idShow)	ON DELETE RESTRICT ON UPDATE CASCADE
);

-- DO NOT use these SQL commands in your submission(they will cause an
--  error on the NMS database server):
-- CREATE SCHEMA
-- USE
