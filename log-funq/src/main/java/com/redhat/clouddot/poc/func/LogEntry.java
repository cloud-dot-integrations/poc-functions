package com.redhat.clouddot.poc.func;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "history")
public class LogEntry extends PanacheEntityBase {

    @Id
    public String requestId;

    public String account;
    public Boolean success;
    public String outcome;
    long ctime;
    public long mtime;

    public LogEntry() {
        // Required by JPA
    }

    public LogEntry(String account, String requestId, Boolean success, String outcome) {
        this(account, requestId);
        this.success = success;
        this.outcome = outcome;
        this.mtime = System.currentTimeMillis();
    }

    public LogEntry(String account, String requestId) {

        this.account = account;
        this.requestId = requestId;
        this.ctime = System.currentTimeMillis();
    }

    public static LogEntry findByRequestId(String requestId) {
        return find("requestId = :requestId", Parameters.with("requestId", requestId)).firstResult();
    }
}
