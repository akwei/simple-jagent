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

package com.github.akwei.simple.jagent.plugins.demo.ctrl.component;

import com.github.akwei.simple.jagent.core.Action;
import com.github.akwei.simple.jagent.core.annotation.BindAdvice;

import java.util.Map;

@BindAdvice(adviceClass = CtrlAdvice.class)
public class CtrlAction implements Action {
    @Override
    public void onEnter(Object self, String method, Object[] args, Map<String, Object> context) {
        System.out.println("CtrlAction args: " + args[0].getClass().getName());
    }

    @Override
    public Object onExit(Object self, String method, Object[] args, Object retValue, Throwable throwable, Map<String, Object> context) {
        System.out.println("CtrlAction " + " " + method + " onExit");
        return retValue;
    }
}
