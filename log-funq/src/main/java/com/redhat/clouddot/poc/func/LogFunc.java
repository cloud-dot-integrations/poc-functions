package com.redhat.clouddot.poc.func;

import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;

import javax.transaction.Transactional;

/**
 *
 */
public class LogFunc {

    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    @Funq
    @CloudEventMapping(trigger = "*")
    @Transactional
    public Map<String,Object> process(Map<String,Object> in,  @Context CloudEvent eventInfo) {

        if (eventInfo == null ) {
            // We have no cloud event, so we need to stop here
            log.warning("Did not receive a cloud event, just passing input along");
            return in;
        }

        String id = eventInfo.id();
        log.info("Got event with id " + id);

        String account = (String) eventInfo.extensions().get("rhaccount");
        if (account == null || account.isBlank()) {
            log.warning("No rhaccount header for message with id " + id);
            return in;
        }

        LogEntry le = LogEntry.findByRequestId(id);
        if (le != null) {
            le.outcome = in.toString();
            if (in.containsKey("status")) {
                String tmp = in.get("status").toString().toLowerCase(Locale.ROOT);
                le.success = !tmp.startsWith("f") && !tmp.startsWith("e"); // Success if if does not start like fail or error
            } else {
                le.success = false; // Payload had no status, assume failure
            }
            le.mtime = System.currentTimeMillis();
        }
        else {
            log.info("No previous log entry with id " + id);
            le = new LogEntry(account,id);
            le.outcome = in.toString();
            if (in.containsKey("status")) {
                String tmp = in.get("status").toString().toLowerCase(Locale.ROOT);
                le.success = !tmp.startsWith("f") && !tmp.startsWith("e"); // Success if if does not start like fail or error
            } else {
                le.success = false; // Payload had no status, assume failure
            }
            le.mtime = System.currentTimeMillis();
            le.persist();
        }

        return in;
    }

}
