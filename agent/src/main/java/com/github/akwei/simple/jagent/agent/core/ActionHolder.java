package com.github.akwei.simple.jagent.agent.core;

import java.util.HashMap;
import java.util.Map;

public class ActionHolder {

    private static final Map<String, Action> ACTION_MAP = new HashMap<>();

    public static void add(String name, Action action) {
        ACTION_MAP.put(name, action);
    }

    public static Action getAction(String name) {
        Action action = ACTION_MAP.get(name);
        if (action == null) {
            throw new RuntimeException("can not find action for name[" + name + "]");
        }
        return action;
    }
}
