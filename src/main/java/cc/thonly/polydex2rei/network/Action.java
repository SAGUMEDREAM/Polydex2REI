package cc.thonly.polydex2rei.network;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

import lombok.Generated;

public enum Action {
    LEFT(0),
    RIGHT(1),
    MID(2);


    private final int id;
    public static final Map<Integer, Action> ENUM_MAP;

    static {
        ENUM_MAP = new Object2ObjectOpenHashMap<>(Map.of(0, LEFT, 1, RIGHT, 2, MID));
    }

    Action(int id) {
        this.id = id;
    }

    public static synchronized Action get(int id) {
        for (Action action : Action.values()) {
            if (action.getId() != id) continue;
            return action;
        }
        return LEFT;
    }

    @Generated
    public int getId() {
        return this.id;
    }
}
