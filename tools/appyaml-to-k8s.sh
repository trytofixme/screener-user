#!/usr/bin/env bash
set -euo pipefail

APP_YML="${1:-src/main/resources/application.yml}"
NAMESPACE="${2:-screener-user}"

if ! command -v yq >/dev/null 2>&1; then
  echo "Install yq first: https://github.com/mikefarah/yq" >&2
  exit 1
fi

# Плоская мапа KEY=VALUE в формате SPRING_ENV (UPPER_SNAKE_CASE)
flatten_yaml () {
  yq -o=json '. as $in ireduce ({}; . * $in )' "$APP_YML" \
  | yq -p=json -o=tsv '.. | select(tag == "!!str" or tag == "!!int" or tag == "!!bool" or tag == "!!float") | path | @csv + "\t" + .' \
  | awk -F'\t' '
    function upcase(s){ gsub(/[^A-Za-z0-9]+/, "_", s); return toupper(s) }
    {
      gsub(/["\[\]]/, "", $1);
      n=$1; v=$2;
      split(n, a, ",");
      key="";
      for(i=1;i<=length(a);i++){
        part=a[i];
        gsub(/^ +| +$/, "", part);
        key = key (i>1?"_":"") upcase(part);
      }
      print key "\t" v;
    }'
}

# Хелпер: хештег-секретные ключи
is_secret () {
  echo "$1" | grep -E '(PASS(WORK)?|PASSWORD|SECRET|TOKEN|API(_)?KEY|ACCESS_KEY|PRIVATE_KEY|WEBHOOK|BEARER|CREDENTIAL|CLIENT_SECRET)' -qi
}

TMP_CFG=$(mktemp)
TMP_SEC=$(mktemp)

flatten_yaml | while IFS=$'\t' read -r K V; do
  if is_secret "$K"; then
    printf "%s=%s\n" "$K" "$V" >> "$TMP_SEC"
  else
    printf "%s=%s\n" "$K" "$V" >> "$TMP_CFG"
  fi
done

kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -

# Secret
if [ -s "$TMP_SEC" ]; then
  kubectl -n "$NAMESPACE" delete secret screener-user-secret --ignore-not-found
  kubectl -n "$NAMESPACE" create secret generic screener-user-secret \
    --from-env-file="$TMP_SEC"
else
  echo "No secret-like keys found; skipping Secret."
fi

# ConfigMap
if [ -s "$TMP_CFG" ]; then
  kubectl -n "$NAMESPACE" delete configmap screener-user-config --ignore-not-found
  kubectl -n "$NAMESPACE" create configmap screener-user-config \
    --from-env-file="$TMP_CFG"
else
  echo "No non-secret keys found; skipping ConfigMap."
fi

echo "Done. Secret/ConfigMap applied in namespace $NAMESPACE"
