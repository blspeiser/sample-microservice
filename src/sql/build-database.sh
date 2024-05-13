#!/bin/bash
if [ -z "$PGPASSWORD" ]; then 
    echo "Environment variable PGPASSWORD is not set";
    exit 1; 
fi
# DDL
sh sql.sh ddl/manage/00.drop-all.sql
sh sql.sh ddl/create/01.create-schema.sql
sh sql.sh ddl/create/02.create-tables.sql
sh sql.sh ddl/create/03.create-constraints.sql
sh sql.sh ddl/create/04.create-indexes.sql
sh sql.sh ddl/create/05.create-sequences.sql
# Data
sh sql.sh data/required/TIMES_SERVICE.TS_LOCATION_TYPES.sql
sh sql.sh data/required/TIMES_SERVICE.TS_LOCATIONS.sql
sh sql.sh data/initial/TIMES_SERVICE.TS_LOCATIONS.sql

echo "Done."