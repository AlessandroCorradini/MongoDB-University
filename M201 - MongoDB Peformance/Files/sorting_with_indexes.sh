#!/usr/bin/env bash

# import the people dataset if you haven't already
mongoimport -d m201 -c people --drop people.json

# connect to the server
mongo
