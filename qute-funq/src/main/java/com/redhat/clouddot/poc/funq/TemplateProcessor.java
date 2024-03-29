package com.redhat.clouddot.poc.funq;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateException;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class TemplateProcessor {
    @Inject
    Engine engine;


    String render(String bundle, String app, String event_type, Map<String, Object> payload) {
        // Get the template code from the DB
        String tmpl = getTemplate(bundle, app, event_type, "instant_mail");

        String result = renderTemplate(tmpl, payload);
        return result;
    }

    String renderTemplate(String tmpl, Map<String, Object> payload) {
        // Parse it
        Template t = engine.parse(tmpl);

        // Instantiate it with the payload of the Notification's action
        TemplateInstance ti = t.data(payload);

        // Render the template with the values
        // Quarkus 2 is more strict than Q1, so we need the try/catch block
        String result = null;
        try {
            result = ti.render();
        } catch (Exception e) {
            e.printStackTrace();  // TODO: Customise this generated block
            if (e instanceof TemplateException) {
                result = "ERROR: " + e.getCause().getMessage();
            }
        }
        return result;
    }

    String getTemplate(String bundle, String app, String event_type, String type) {

        QTemplate qTemplate = getqTemplate(bundle, app, event_type, type);

        if (qTemplate == null )
            return "- NO TEMPLATE FOUND -";

        return qTemplate.body;

    }

    private QTemplate getqTemplate(String bundle, String app, String event_type, String type) {
        String bae = bundle + ":" + app + ":" + event_type;

        PanacheQuery<QTemplate> qt = QTemplate.find("bae = :bae AND type = :type AND subtype = :subtype",
                Parameters.with("bae", bae)
                        .and("type", type)
                        .and("subtype", "body") // TODO don't hardcode
        );

        QTemplate qTemplate = qt.firstResult();
        return qTemplate;
    }

    public void updateTemplate(String bundle, String app, String event_type, String type, String body) {
        QTemplate qt = getqTemplate(bundle,app,event_type,type);

        if (qt==null) {
            String bae = bundle + ":" + app + ":" + event_type;
            qt = new QTemplate();
            qt.bae = bae;
            qt.body = body;
            qt.type = type;
            qt.subtype = "body"; // TODO don't hardcode
            QTemplate.persist(qt);
        } else {
            qt.body = body;
            qt.store();
            qt.flush();
        }
    }
}
