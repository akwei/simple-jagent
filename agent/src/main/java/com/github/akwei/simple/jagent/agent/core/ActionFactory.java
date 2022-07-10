package com.github.akwei.simple.jagent.agent.core;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {

    private final Map<Class, Action> actionMap = new HashMap<>();

    public void add(Action action) {
        actionMap.put(action.getClass(), action);
    }

    public Action get(Class<Action> clazz) {
        return actionMap.get(clazz);
    }

}
