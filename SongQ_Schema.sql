DROP DATABASE IF EXISTS SongQ;
CREATE DATABASE SongQ;

USE SongQ;
CREATE TABLE Users(
id VARCHAR(50) NOT NULL,
username VARCHAR(50) NOT NULL,
PRIMARY KEY (id));

CREATE TABLE Playlists(
identifier INT NOT NULL AUTO_INCREMENT,
roomCode VARCHAR(50) NOT NULL,
playlistId VARCHAR(100) NOT NULL,
owner VARCHAR(50) NOT NULL,
playlistName VARCHAR(50) NOT NULL,
latitude DECIMAL(17,14),
longitude DECIMAL(17,14),
PRIMARY KEY(identifier),
FOREIGN KEY(owner) REFERENCES Users(id));

CREATE TABLE Requests(
requestId INT NOT NULL AUTO_INCREMENT,
roomCode VARCHAR(50) NOT NULL,
owner VARCHAR(50) NOT NULL,
songId VARCHAR(100) NOT NULL,
songName VARCHAR(100),
artists VARCHAR(100),
album VARCHAR(100),
serviced BOOLEAN NOT NULL,
accepted BOOLEAN,
rejected BOOLEAN,
PRIMARY KEY(requestId));