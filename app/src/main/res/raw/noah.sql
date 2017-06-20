--
-- File generated with SQLiteStudio v3.1.0 on �ܶ� 6�� 13 13:55:35 2017
--
-- Text encoding used: System
--
--PRAGMA foreign_keys = off;
--BEGIN TRANSACTION;

-- Table: City
CREATE TABLE Area (DBID INTEGER PRIMARY KEY AUTOINCREMENT, Id VARCHAR (16), Pid VARCHAR (16), Name VARCHAR (64), Level VARCHAR (4), Code VARCHAR (8), PostCode VARCHAR (32), TelCode VARCHAR (32), Sort VARCHAR (8), ShortName VARCHAR (32), Spell VARCHAR (128), ShortSpell VARCHAR (32), Lng VARCHAR (32), Lat VARCHAR (32));

--COMMIT TRANSACTION;
--PRAGMA foreign_keys = on;
