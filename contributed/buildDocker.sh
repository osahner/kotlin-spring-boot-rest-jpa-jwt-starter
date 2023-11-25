#!/usr/bin/env bash

color_restore='\033[0m'
color_red='\033[0;31m'
color_light_green='\033[1;32m'
color_light_blue='\033[1;34m'
color_light_cyan='\033[1;36m'

cd "$(dirname "${0:a}")" || exit

VERSION=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="version"]/text()' ../pom.xml)
ARTIFACTID=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="artifactId"]/text()' ../pom.xml)
GROUPID=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="groupId"]/text()' ../pom.xml)
SPRING_BOOT_VERSION=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="parent"]/*[local-name()="version"]/text()' ../pom.xml)
JAR_FILE=${ARTIFACTID}-${VERSION}.jar

p=0
while getopts 'p?h' flag; do
  case "${flag}" in
  p) p=1 ;;
  h | ?)
    echo -e "${color_light_green}option -p (production)${color_restore}"
    exit
    ;;
  esac
done

echo -e "${color_light_blue}=> ${color_light_green}Build${color_restore}"
cd ../
mvn clean package -Dmaven.test.skip=true -Ddockerfile.skip=true
PLATFORM="linux/arm64/v8"
if [[ $p -eq 1 ]]; then
  PLATFORM="linux/amd64"
fi
echo -e " ${color_light_cyan}+ for platform ${PLATFORM}${color_restore}"
DOCKER_BUILDKIT=1 docker build --build-arg JAR_FILE="${JAR_FILE}" --build-arg SPRING_BOOT_VERSION="${SPRING_BOOT_VERSION}" \
  --platform=${PLATFORM} --no-cache --pull --load --rm -f Dockerfile -t "${GROUPID}/${ARTIFACTID}:${VERSION}" .
