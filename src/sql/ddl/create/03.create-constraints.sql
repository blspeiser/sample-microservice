-- Location Types
ALTER TABLE ONLY TIMES_SERVICE.TS_LOCATION_TYPES
    ADD CONSTRAINT PK_TS_LOCATION_TYPES_ID PRIMARY KEY (ID);

--Locations
ALTER TABLE ONLY TIMES_SERVICE.TS_LOCATIONS
    ADD CONSTRAINT PK_TS_LOCATIONS_ID PRIMARY KEY (ID);

ALTER TABLE ONLY TIMES_SERVICE.TS_LOCATIONS
    ADD CONSTRAINT UNQ_TS_LOCATIONS_NAME UNIQUE (NAME);

ALTER TABLE ONLY TIMES_SERVICE.TS_LOCATIONS
    ADD CONSTRAINT FK_TS_LOCATIONS_TYPE FOREIGN KEY (TYPE) REFERENCES TIMES_SERVICE.TS_LOCATION_TYPES(ID);

