DROP DATABASE IF EXISTS SongQ;
CREATE DATABASE SongQ;

USE SongQ;
CREATE TABLE Users(
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
username VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
PRIMARY KEY (username));

CREATE TABLE Playlists(
roomCode VARCHAR(50) NOT NULL,
owner VARCHAR(50) NOT NULL,
PRIMARY KEY(roomCode),
FOREIGN KEY(owner) REFERENCES Users(username));

CREATE TABLE Requests(
roomCode VARCHAR(50) NOT NULL,
owner VARCHAR(50) NOT NULL,
songId VARCHAR(100) NOT NULL,
songName VARCHAR(100) NOT NULL,
artists VARCHAR(100) NOT NULL,
album VARCHAR(100) NOT NULL,
serviced BOOLEAN NOT NULL,
accepted BOOLEAN,
rejected BOOLEAN);