#!/usr/bin/env zsh
SCRIPT_PATH="${0:A:h}"

if [ -z "$1" ]; then echo "usage: $0 *feature*"; exit; fi
autoload -Uz zmv
alias zcp='noglob zmv -C'

lowercase="${(L)1}"
capitalized="${(C)lowercase}"

mkdir "$SCRIPT_PATH/../main/kotlin/osahner/api/$lowercase"
zcp "$SCRIPT_PATH/REPLACEME(*)" "$SCRIPT_PATH/../main/kotlin/osahner/api/$lowercase/$capitalized\$1"
perl -pi -e "s/REPLACEME/$capitalized/g" "$SCRIPT_PATH/../main/kotlin/osahner/api/$lowercase/"*
perl -pi -e "s/replaceme/$lowercase/g" "$SCRIPT_PATH/../main/kotlin/osahner/api/$lowercase/"*

echo "generated src/main/kotlin/osahner/api/$lowercase/"
