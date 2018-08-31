# Project "Flicksion"

[![Build Status](https://travis-ci.com/AllarVi/flicksion-be.svg?branch=develop)](https://travis-ci.com/AllarVi/flicksion-be)

* Demand-based, enhanced customer experience
* Universal event aggregator (movies, plays, party etc.)
* Machine learning powered customized offers
* Explorable (quick and easy to use)

## General Architecture

![general architecture of the example app](img/flick_subsystem_design_level_2.png)

## Endpoint examples

Get aggregated events

`http://localhost:8080/events`

Get aggregated events (include omdb search results)

`http://localhost:8080/events?searchResults=true`

Get aggregated events by actors 

`http://localhost:8080/events/actors/Jonah%20Hill,Jack%20Black`
