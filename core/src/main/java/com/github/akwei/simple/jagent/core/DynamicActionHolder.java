/*
 * Copyright 2022 akwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.akwei.simple.jagent.core;

import java.util.HashMap;
import java.util.Map;

public class DynamicActionHolder {

    private static final Map<String, DynamicAction> ACTION_PROXY_MAP = new HashMap<>();

    private static final Map<Class<?>, Class<? extends DynamicAction>> ORIGIN_CLS_DYNAMIC_ACTION_CLS_MAP = new HashMap<>();


    public static void add(String name, DynamicAction dynamicAction) {
        ACTION_PROXY_MAP.put(name, dynamicAction);
        ORIGIN_CLS_DYNAMIC_ACTION_CLS_MAP.put(dynamicAction.getTarget().getClass(), dynamicAction.getClass());
    }

    public static void addOriginActionCls(Class<?> originActionCls, Class<? extends DynamicAction> dynamicActionCls) {
        ORIGIN_CLS_DYNAMIC_ACTION_CLS_MAP.put(originActionCls, dynamicActionCls);
    }

    public static DynamicAction getDynamicAction(String name) {
        DynamicAction dynamicAction = ACTION_PROXY_MAP.get(name);
        if (dynamicAction == null) {
            throw new RuntimeException("can not find actionProxy for name[" + name + "]");
        }
        return dynamicAction;
    }

    public static Class<?> getDynamicAdviceClass(Class<?> originActionClass) {
        return ORIGIN_CLS_DYNAMIC_ACTION_CLS_MAP.get(originActionClass);
    }
}
