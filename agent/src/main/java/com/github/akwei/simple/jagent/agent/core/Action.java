package com.github.akwei.simple.jagent.agent.core;

public interface Action {

    void onEnter(Object self, String method, Object[] args);

    Object onExit(Object self, String method, Object[] args, Object retValue, Throwable throwable);

}
