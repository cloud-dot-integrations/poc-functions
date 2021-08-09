#!/usr/bin/env bash

export ENV_NAME=integrations-dev

curl https://raw.githubusercontent.com/RedHatInsights/insights-rbac/2fd28158b8069eecf6ba13946133c68bd007eced/deploy/rbac-clowdapp.yml | \
oc process \
-p ENV_NAME=integrations-dev \
-p PERMISSION_SEEDING_ENABLED=true \
-p ROLE_SEEDING_ENABLED=true \
-p GROUP_SEEDING_ENABLED=true \
-p DISABLE_MIGRATE=false \
-p IMAGE_TAG=2fd2815 \
-p EPH_ENV=True \
-f - | oc apply -n rbac -f -
