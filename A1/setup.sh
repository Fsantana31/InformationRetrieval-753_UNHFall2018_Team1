# Script to run when first setting this maven project on a new computer

if [ ! -e ../lib/lucene-7.4.0/core/lucene-core-7.4.0.jar ] \
|| [ ! -e ../lib/trec-car-tools-java/target/trec-car-tools-java-15.jar ]
then
	echo "Please run ../setup.sh to download dependencies before"
	echo "installing them with this script"
	exit 1
fi

mvn install:install-file \
	-Dfile=../lib/lucene-7.4.0/core/lucene-core-7.4.0.jar \
	-DgroupId=org.apache.lucene \
	-DartifactId=lucene \
	-Dversion=7.4.0 \
	-Dpackaging=jar

mvn install:install-file \
	-Dfile=../lib/trec-car-tools-java/target/trec-car-tools-java-15.jar \
	-DgroupId=cs.unh.trec-car-tools \
	-DartifactId=trec-car-tools \
	-Dversion=15 -Dpackaging=jar
