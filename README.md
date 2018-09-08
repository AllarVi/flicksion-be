# Project "Flicksion"

[![Build Status](https://travis-ci.com/AllarVi/flicksion-be.svg?branch=develop)](https://travis-ci.com/AllarVi/flicksion-be)

* Demand-based, enhanced customer experience
* Universal event aggregator (movies, plays, party etc.)
* Machine learning powered customized offers
* Explorable (quick and easy to use)

## Motivation

### Problem

Pole aega ja viitsimist surfata erinevatel kino saitidel.

Tahaks aeg-ajalt kinos käia, aga selle jaoks peaks pidevalt uurima mida hetkel näidatakse.
Lisaks, on tülikas pidevalt uurida, kas on filme, mis vastavad mu isiklikele eelistustele 
(lemmikud näitlejad, žanrid, režissöörid jne).

### Lahendus

App, mis annab teada erinevatesse kinodesse jõudnud filmidest vastavalt kasutaja sisestatud
eelistustele.

* App käib kindla aja tagant "uurimas", mis filmid kinos jooksevad (kinode APId)
* App täiustab kasutaja eelistusi tema tagasiside põhjal (masinõpe, andmeanalüüs)
* Appi saab sisestada enda isiklikke eelistusi
* App annab teada kui kinno on jõudnud film, mis vastab kasutaja eelistustele
* Appilt on võimalik küsida soovitust filmide kohta, mida hetkel näidatakse
* App näitab filmide skoore populaarsemate andmebaaside põhjal (IMDB, Rotten Tomatoes jne)

## General Architecture

![general architecture of the example app](img/flick_subsystem_design_level_2.png)

## Endpoint examples

Get aggregated events

`http://localhost:8080/events`

Get aggregated events (include omdb search results)

`http://localhost:8080/events?searchResults=true`

Get aggregated events by actors 

`http://localhost:8080/events/actors/Jonah%20Hill,Jack%20Black`
