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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

import java.util.ArrayList;
import java.util.List;

public class AgentClassBuilder {
    private final ByteBuddy byteBuddy = new ByteBuddy();
    private String[] classNames;
    private ClassLoader classLoader;
    private List<TransformerDefinition> transformerDefinitions;
    private ClassFileLocator classFileLocator;
    private TypePool typePool;

    public AgentClassBuilder() {
        this.byteBuddy.ignore(ElementMatchers.isSynthetic())
                .ignore(ElementMatchers.nameStartsWith("sun."))
                .ignore(ElementMatchers.nameStartsWith("com.sun."))
                .ignore(ElementMatchers.nameStartsWith("brave."))
                .ignore(ElementMatchers.nameStartsWith("zipkin2."))
                .ignore(ElementMatchers.nameStartsWith("com.fasterxml"))
                .ignore(ElementMatchers.nameStartsWith("org.apache.logging"))
                .ignore(ElementMatchers.nameStartsWith("kotlin."))
                .ignore(ElementMatchers.nameStartsWith("javax."))
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy."))
                .ignore(ElementMatchers.nameStartsWith("com\\.sun\\.proxy\\.\\$Proxy.+"))
                .ignore(ElementMatchers.nameStartsWith("java\\.lang\\.invoke\\.BoundMethodHandle\\$Species_L.+"))
                .ignore(ElementMatchers.nameStartsWith("org.junit."))
                .ignore(ElementMatchers.nameStartsWith("junit."))
                .ignore(ElementMatchers.nameStartsWith("com.intellij."));
    }

    public AgentClassBuilder classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.classFileLocator = ClassFileLocator.ForClassLoader.of(classLoader);
        this.typePool = TypePool.Default.of(classFileLocator);
        return this;
    }

    public AgentClassBuilder transformerDefinitions(List<TransformerDefinition> transformerDefinitions) {
        this.transformerDefinitions = transformerDefinitions;
        return this;
    }

    public AgentClassBuilder classNames(String[] classNames) {
        this.classNames = classNames;
        return this;
    }


    public List<Class<?>> loadClasses() {
        List<TypeDescription> typeDescriptions = new ArrayList<>();
        for (String className : this.classNames) {
            typeDescriptions.add(this.typePool.describe(className).resolve());
        }
        List<Class<?>> classes = new ArrayList<>();
        for (TypeDescription typeDescription : typeDescriptions) {
            Class<?> aClass = this.loadClass(typeDescription, this.transformerDefinitions);
            if (aClass != null) {
                classes.add(aClass);
            }
        }
        return classes;
    }

    private Class<?> loadClass(TypeDescription typeDescription, List<TransformerDefinition> transformerDefinitions) {
        for (TransformerDefinition transformerDefinition : transformerDefinitions) {
            if (!transformerDefinition.type().matches(typeDescription)) {
                continue;
            }
            AgentBuilder.Transformer transformer = transformerDefinition.compoundTransformer();
            DynamicType.Builder<?> builder = this.byteBuddy.redefine(typeDescription, this.classFileLocator);
            return transformer.transform(builder, typeDescription, classLoader, null)
                    .make().load(this.classLoader, ClassLoadingStrategy.Default.INJECTION).getLoaded();
        }
        return null;
    }
}
