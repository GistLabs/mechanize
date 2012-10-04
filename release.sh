#!/bin/sh

mvn -DperformRelease=true release:clean release:prepare release:perform
