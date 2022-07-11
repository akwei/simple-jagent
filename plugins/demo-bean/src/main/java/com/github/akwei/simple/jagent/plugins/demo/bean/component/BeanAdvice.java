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

import com.github.akwei.simple.jagent.core.ActionHolder;
import com.github.akwei.simple.jagent.core.AgentAdvice;
import com.github.akwei.simple.jagent.core.OnMethodEnterResult;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.util.Arrays;

public class BeanAdvice implements AgentAdvice {

    private static final String NAME = "com.github.akwei.simple.jagent.plugins.demo.bean.component.BeanAdvice";

    @Advice.OnMethodEnter
    public static OnMethodEnterResult onEnter(@Advice.This Object self,
                                              @Advice.Origin("#m") String method,
                                              @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args) {
        OnMethodEnterResult result = new OnMethodEnterResult();
        Object[] objects = Arrays.copyOf(args, args.length);
        ActionHolder.getAction(NAME).onEnter(self, method, objects, result.getContext());
        args = objects;
        return result;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void onExit(@Advice.Enter OnMethodEnterResult onMethodEnterResult,
                              @Advice.This Object invoker,
                              @Advice.Origin("#m") String method,
                              @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
                              @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object retValue,
                              @Advice.Thrown Throwable throwable) {
        Object[] objects = Arrays.copyOf(args, args.length);
        retValue = ActionHolder.getAction(NAME).onExit(invoker, method, objects, retValue, throwable, onMethodEnterResult.getContext());
        args = objects;
    }

}
