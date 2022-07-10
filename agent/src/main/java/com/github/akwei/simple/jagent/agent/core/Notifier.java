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

package com.github.akwei.simple.jagent.agent.core;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Notifier {
    /**
     * A java.lang.invoke.MethodHandles.Lookup representing the source method.
     * A String representing the method's name.
     * A java.lang.invoke.MethodType representing the type that is requested for binding.
     * A String of the binary target class name.
     * A int with value 0 for an enter advice and {code 1} for an exist advice.
     * A Class representing the class implementing the instrumented method.
     * A String with the name of the instrumented method.
     * A java.lang.invoke.MethodHandle representing the instrumented method unless the target is the type's static initializer.
     */
    public static ConstantCallSite callback(MethodHandles.Lookup lookup,
                                            String methodName,
                                            MethodType methodType,
                                            String targetClassName,
                                            int adviceCode,
                                            Class<?> clazz,
                                            String instrumentedMethodName,
                                            MethodHandle methodHandle
    ) {
        System.out.println("Notifier callback =======");
        return null;
    }

}
