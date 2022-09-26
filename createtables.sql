CREATE TABLE Problems(
	destination VARCHAR(255) NOT NULL,
	name VARCHAR(255) NOT NULL,
	area VARCHAR(255),
	subarea VARCHAR(255) NOT NULL,
	boulder VARCHAR(255) NOT NULL,
	grade VARCHAR(3),
	stars INT,
	FA VARCHAR(255),
	FA_year INT,
	style_icon_1 VARCHAR(100),
	style_icon_2 VARCHAR(100),
	style_icon_3 VARCHAR(100),
	style_icon_4 VARCHAR(100),
	description VARCHAR(2000),
	CONSTRAINT PK_Problems PRIMARY KEY (destination, name, subarea, boulder, description)
)

CREATE TABLE Lists(
    name VARCHAR(255) PRIMARY KEY,
    Top_100 VARCHAR(1),
    Top_25_Highballs VARCHAR(1),
    Bouldering_Alone VARCHAR(1),
    The_Crack_List VARCHAR(1),
    The_Impossible_7 VARCHAR(1),
    Squamish_7_Terrors VARCHAR(1),
)

CREATE TABLE Users(
    username VARCHAR(20) PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    email VARCHAR(255),
    saltedPass VARBINARY(20),
    hashedPass VARBINARY(20),
    flashGrade VARCHAR(3),
    projectGrade VARCHAR(3),
    heightFeet INT,
    heightInches INT,
    apeIndex INT,
    gender VARCHAR(255)
)

CREATE TABLE Parking (
    name VARCHAR(255) PRIMARY KEY,
    lat DOUBLE PRECISION,
    long DOUBLE PRECISION,
)

CREATE TABLE Areas (
    destination VARCHAR(255),
    area VARCHAR(255),
    subarea VARCHAR(255),
    numProblems INT,
    approachTerrain VARCHAR(255),
    approachTimeMin INT,
    sun VARCHAR(255),
    parkingLot VARCHAR(255),
    rainCovered VARCHAR(1),
    driesQuick VARCHAR(1)
)
