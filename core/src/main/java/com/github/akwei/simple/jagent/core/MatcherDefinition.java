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

import com.github.akwei.simple.jagent.core.annotation.BindAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public class MatcherDefinition {

    private ElementMatcher<? super MethodDescription> matcher;
    private Class<? extends Action> actionClass;


    public MatcherDefinition matcher(ElementMatcher<? super MethodDescription> matcher) {
        this.matcher = matcher;
        return this;
    }

    public ElementMatcher<? super MethodDescription> matcher() {
        return matcher;
    }

    public Class<? extends Action> actionClass() {
        return actionClass;
    }

    public MatcherDefinition actionClass(Class<? extends Action> actionClass) {
        this.actionClass = actionClass;
        return this;
    }

    public String adviceName() {
        if (this.actionClass == null) {
            throw new IllegalArgumentException("can not get adviceName from null actionClass");
        }
        BindAdvice bindAdvice = this.actionClass.getAnnotation(BindAdvice.class);
        Class<? extends AgentAdvice> adviceClass = bindAdvice.adviceClass();
        return adviceClass.getName();
    }
}
