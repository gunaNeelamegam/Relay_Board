#!/bin/bash

if [ "$1" == "clean" ];
then
	mvn clean;
	rm -rf ./target/
	cd deb/ && make clean
fi 

if [ "$#" == "0" ]
then
mvn clean
if mvn package ;
then
	printf "###########################\n"
	echo -e "\e[032mBuild Success \e[0m"
	printf "###########################\n"

	cd deb/
	if make ;
	then
		printf "###########################\n"
		echo -e "\e[032mDEB package Created\e[0m"
		printf "###########################\n"
	fi
	cd ..
fi
fi
exit 0
