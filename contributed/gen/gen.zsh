#!/usr/bin/env zsh
SCRIPT_PATH="${0:A:h}"
BASEPACKAGE="osahner/api"
CANONICAL=$(cd -P -- "$(dirname -- "${SCRIPT_PATH}/../../src/main/kotlin/${BASEPACKAGE}/")" &&
printf '%s\n' "$(pwd -P)/$(basename -- "${SCRIPT_PATH}/../../src/main/kotlin/${BASEPACKAGE}/")")

if [ -z "$1" ]; then echo "usage: $0 *feature*"; exit; fi

autoload -Uz zmv
alias zcp='noglob zmv -C'

lowercase="${(L)1}"
capitalized="${(C)lowercase}"
pluralLowercase="${lowercase}s"
if [[ $pluralLowercase =~ .*sss$ ]]; then pluralLowercase="${lowercase}"; fi

if [[ -d "${CANONICAL}/${lowercase}" ]]; then echo "feature: ${lowercase} already exists"; exit; fi

mkdir "${CANONICAL}/${lowercase}"
zcp "${SCRIPT_PATH}/REPLACEME(*)" "${CANONICAL}/${lowercase}/${capitalized}\$1"
perl -pi -e "s/REPLACEME/${capitalized}/g" "${CANONICAL}/${lowercase}/"*
perl -pi -e "s/replaceme/${lowercase}/g" "${CANONICAL}/${lowercase}/"*
perl -pi -e "s/ReplacePluralLowercase/${pluralLowercase}/g" "${CANONICAL}/${lowercase}/"*

echo "generated src/main/kotlin/${BASEPACKAGE}/${lowercase}/"
