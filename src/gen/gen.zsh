#!/usr/bin/env zsh
SCRIPT_PATH="${0:A:h}"

autoload -Uz zmv
alias zcp='noglob zmv -C'

lowercase="${(L)1}"
capitalized="${(C)lowercase}"

mkdir "$SCRIPT_PATH/../main/kotlin/osahner/business/$lowercase"
zcp "$SCRIPT_PATH/REPLACEME(*)" "$SCRIPT_PATH/../main/kotlin/osahner/business/$lowercase/$capitalized\$1"
perl -pi -e "s/REPLACEME/$capitalized/g" "$SCRIPT_PATH/../main/kotlin/osahner/business/$lowercase/"*
perl -pi -e "s/replaceme/$lowercase/g" "$SCRIPT_PATH/../main/kotlin/osahner/business/$lowercase/"*
