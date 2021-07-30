#!/usr/bin/env bash

# Sets up loki+promtail+grafana. Runs loki in minimal single process mode. PVC is used for storage.

oc new-project loki
oc process -f 10-loki.yaml | oc apply -n loki -f -

# oc create ns openshift-logging
oc apply -n openshift-logging -f 20-logging-operator.yaml

oc wait -n openshift-logging --timeout=10m subscription cluster-logging --for=condition=CatalogSourcesUnhealthy=false
#oc wait -n openshift-logging --timeout=10m subscription subscription cluster-logging --for=condition=InstallPlanPending=false

oc apply -n openshift-logging -f 21-cluster-logging.yaml
oc process -f 22-promtail.yaml | oc apply -n openshift-logging -f -

oc new-project grafana
oc apply -n grafana -f 30-grafana-operator.yaml
