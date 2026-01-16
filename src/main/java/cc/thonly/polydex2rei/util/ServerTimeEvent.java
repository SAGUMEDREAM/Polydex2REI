package cc.thonly.polydex2rei.util;

import java.util.ArrayList;
import java.util.List;

public class ServerTimeEvent {
    public static final List<ServerTimeEvent> LIST = new ArrayList<>();
    public int tick;
    public boolean removed = false;
    public final Runnable action;

    public ServerTimeEvent(Runnable action, int maxTick) {
        this.action = action;
        this.tick = maxTick;
    }

    public void onTick() {
        this.tick--;
        if (this.tick == 0) {
            this.action.run();
            this.removed = true;
        }
    }

    public static void add(ServerTimeEvent event) {
        LIST.add(event);
    }

    public static void tick() {
        LIST.removeIf(event->{
            event.onTick();
            return event.removed;
        });
    }
}
