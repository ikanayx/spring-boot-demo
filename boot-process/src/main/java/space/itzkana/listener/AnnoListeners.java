package space.itzkana.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnnoListeners {

    @EventListener({ ApplicationStartedEvent.class })
    public void onApplicationStarted() {
        System.out.println("project listener(anno) => application started.");
    }

    @EventListener({ ContextRefreshedEvent.class })
    public void onContextRefreshed() {
        System.out.println("project listener(anno) => context refreshed.");
    }
}
