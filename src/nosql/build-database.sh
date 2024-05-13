mongosh --eval "use app" --eval  "db.dropDatabase()"
sh load.sh data/required/Locations.json
sh load.sh data/initial/Locations.json