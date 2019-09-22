#!/usr/bin/env bash

# import the restaurants dataset
mongoimport -d m201 -c restaurants --drop restaurants.json