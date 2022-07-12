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

import com.github.akwei.simple.jagent.core.ObjectInitializer;
import com.github.akwei.simple.jagent.core.TransformerDefinition;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class Agent {

    private static final File temp;

    static {
        try {
            temp = Files.createTempDirectory("tmp").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
        System.out.println("================= begin agent premain =================");
        System.out.println("Agent classloader: " + Agent.class.getClassLoader().getClass().getName());
        loadFromBootstrapClassloader(instrumentation);
        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(new AgentListener())
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(AgentBuilder.LocationStrategy.ForClassLoader.STRONG.withFallbackTo(ClassFileLocator.ForClassLoader.ofBootLoader()))
                .ignore(ElementMatchers.isSynthetic())
                .or(ElementMatchers.nameStartsWith("sun."))
                .or(ElementMatchers.nameStartsWith("com.sun."))
                .or(ElementMatchers.nameStartsWith("brave."))
                .or(ElementMatchers.nameStartsWith("zipkin2."))
                .or(ElementMatchers.nameStartsWith("com.fasterxml"))
                .or(ElementMatchers.nameStartsWith("org.apache.logging"))
                .or(ElementMatchers.nameStartsWith("kotlin."))
                .or(ElementMatchers.nameStartsWith("javax."))
                .or(ElementMatchers.nameStartsWith("net.bytebuddy."))
                .or(ElementMatchers.nameStartsWith("com\\.sun\\.proxy\\.\\$Proxy.+"))
                .or(ElementMatchers.nameStartsWith("java\\.lang\\.invoke\\.BoundMethodHandle\\$Species_L.+"))
                .or(ElementMatchers.nameStartsWith("org.junit."))
                .or(ElementMatchers.nameStartsWith("junit."))
                .or(ElementMatchers.nameStartsWith("com.intellij."));

        ObjectInitializer objectInitializer = new ObjectInitializer();
        objectInitializer.init("com.github.akwei.simple.jagent.plugins");
        agentBuilder = installTransformer(agentBuilder, objectInitializer);
        agentBuilder.installOn(instrumentation);
    }

    private static void loadFromBootstrapClassloader(Instrumentation instrumentation) throws Exception {
        injectToBootstrapClassLoader(instrumentation,
                "com.github.akwei.simple.jagent.core.ActionHolder");
        injectToBootstrapClassLoader(instrumentation,
                "com.github.akwei.simple.jagent.core.Action");
        injectToBootstrapClassLoader(instrumentation,
                "com.github.akwei.simple.jagent.core.AgentFieldAccessor");
        injectToBootstrapClassLoader(instrumentation, "com.github.akwei.simple.jagent.core.AgentAdvice");
        injectToBootstrapClassLoader(instrumentation, "com.github.akwei.simple.jagent.core.AgentFieldAccessor");
        injectToBootstrapClassLoader(instrumentation, "com.github.akwei.simple.jagent.core.ContextInfo");
        injectToBootstrapClassLoader(instrumentation, "com.github.akwei.simple.jagent.core.DynamicAction");
        injectToBootstrapClassLoader(instrumentation, "com.github.akwei.simple.jagent.core.DynamicActionHolder");
    }

    private static AgentBuilder installTransformer(AgentBuilder agentBuilder, ObjectInitializer objectInitializer) {
        List<TransformerDefinition> transformerDefinitions = objectInitializer.getTransformerDefinitions();
        ClassLoader agentClassLoader = Agent.class.getClassLoader();
        for (TransformerDefinition transformerDefinition : transformerDefinitions) {
            AgentBuilder.Identified.Narrowable type = agentBuilder.type(transformerDefinition.type());

            agentBuilder = type.transform((builder, typeDescription, classLoader, module) -> {
                addUserClassLoader(agentClassLoader, classLoader);
                builder = transformerDefinition.compoundTransformer().transform(builder, typeDescription, classLoader, module);
                return builder;
            }).asTerminalTransformation();
        }
        return agentBuilder;
    }

    private static void injectToBootstrapClassLoader(Instrumentation instrumentation, String className) throws Exception {
        ClassFileLocator classFileLocator = ClassFileLocator.ForClassLoader.of(Agent.class.getClassLoader());
        TypePool typePool = TypePool.Default.of(classFileLocator);
        TypePool.Resolution describe = typePool.describe(className);

        ClassInjector.UsingInstrumentation
                .of(temp, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, instrumentation)
                .inject(Collections.singletonMap(describe.resolve(),
                        classFileLocator.locate(className).resolve()));
    }

    static void addUserClassLoader(ClassLoader agentClassLoader, ClassLoader userClassLoader) {
        try {
            Method method = agentClassLoader.getClass().getDeclaredMethod("addClassLoader", ClassLoader.class);
            method.invoke(agentClassLoader, userClassLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
