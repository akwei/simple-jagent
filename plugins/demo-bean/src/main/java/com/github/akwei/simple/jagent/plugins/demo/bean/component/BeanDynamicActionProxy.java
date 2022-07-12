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

package com.github.akwei.simple.jagent.plugins.demo.bean.component;


import com.github.akwei.simple.jagent.core.ContextInfo;
import com.github.akwei.simple.jagent.core.DynamicAction;

public class BeanDynamicActionProxy implements DynamicAction {

    private final BeanDynamicAction dynamicAction;

    public BeanDynamicActionProxy(BeanDynamicAction dynamicAction) {
        this.dynamicAction = dynamicAction;
    }

    @Override
    public Object getTarget() {
        return this.dynamicAction;
    }

    @Override
    public void onEnterDynamic(Object... args) {
        this.dynamicAction.onEnter(args[0], (String) args[1], (Object[]) args[2], (ContextInfo) args[3]);
    }

    @Override
    public Object onExitDynamic(Object... args) {
        return this.dynamicAction.onExit(args[0], (String) args[1], (Object[]) args[2], args[3], (Throwable) args[4], (ContextInfo) args[5]);
    }

}
