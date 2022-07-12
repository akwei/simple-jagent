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

import com.github.akwei.simple.jagent.core.annotation.BindActionModule;
import com.github.akwei.simple.jagent.core.annotation.BindDefinitionModule;
import com.github.akwei.simple.jagent.core.annotation.BindDynamicActionProxy;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectInitializer {

    private final List<TransformerDefinition> transformerDefinitions = new ArrayList<>();

    private Injector injector;

    public void init(String scanPackagePrefix) {
        Reflections reflections = new Reflections(scanPackagePrefix);
        this.init(this.findActionModules(reflections), this.findDefinitionModules(reflections), this.findDynamicAction(reflections));
    }

    public void init(List<Module> modules, List<DefinitionModule> definitionModules, List<DynamicAction> dynamicActions) {
        this.initInjector(modules);
        this.initDefinitionConfig(definitionModules);
        this.initDynamicActionProxy(dynamicActions);
    }

    public List<TransformerDefinition> getTransformerDefinitions() {
        return transformerDefinitions;
    }

    private void initDefinitionConfig(List<DefinitionModule> definitionModules) {
        for (DefinitionModule definitionModule : definitionModules) {
            transformerDefinitions.addAll(definitionModule.getTransformerDefinitions());
        }
        for (TransformerDefinition transformerDefinition : transformerDefinitions) {
            for (MatcherDefinition matcherDefinition : transformerDefinition.matcherDefinitions()) {
                Class<?> aClass = matcherDefinition.actionClass();
                Action action = (Action) injector.getInstance(aClass);
                ActionHolder.add( action);
            }
        }
    }

    private List<DefinitionModule> findDefinitionModules(Reflections reflections) {
        Set<Class<?>> classesOfDefinitionConfig = reflections.get(Scanners.TypesAnnotated.with(BindDefinitionModule.class).asClass());
        List<DefinitionModule> definitionModules = new ArrayList<>();
        for (Class<?> aClass : classesOfDefinitionConfig) {
            definitionModules.add((DefinitionModule) newInstance(aClass));
        }
        return definitionModules;
    }

    private void initInjector(List<Module> modules) {
        this.injector = Guice.createInjector(modules);
    }

    private List<Module> findActionModules(Reflections reflections) {
        Set<Class<?>> classesOfActionModule = reflections.get(Scanners.TypesAnnotated.with(BindActionModule.class).asClass());
        List<Module> modules = new ArrayList<>();
        for (Class<?> aClass : classesOfActionModule) {
            AbstractModule module = (AbstractModule) newInstance(aClass);
            modules.add(module);
        }
        return modules;
    }

    private List<DynamicAction> findDynamicAction(Reflections reflections) {
        Set<Class<?>> classesOfBindDynamicActionProxy = reflections.get(Scanners.TypesAnnotated.with(BindDynamicActionProxy.class).asClass());
        List<DynamicAction> dynamicActions = new ArrayList<>();
        for (Class<?> aClass : classesOfBindDynamicActionProxy) {
            if (!aClass.getSuperclass().equals(DynamicAction.class)) {
                throw new IllegalArgumentException(aClass.getName() + " must extends " + DynamicAction.class.getName());
            }
            BindDynamicActionProxy bindDynamicActionProxy = aClass.getAnnotation(BindDynamicActionProxy.class);
            Object targetAction = injector.getInstance(bindDynamicActionProxy.targetActionClass());
            DynamicAction dynamicAction = (DynamicAction) newInstance(aClass, targetAction);
            dynamicActions.add(dynamicAction);
        }
        return dynamicActions;
    }

    private void initDynamicActionProxy(List<DynamicAction> dynamicActions) {
        for (DynamicAction dynamicAction : dynamicActions) {
            BindDynamicActionProxy bindDynamicActionProxy = dynamicActions.getClass().getAnnotation(BindDynamicActionProxy.class);
            DynamicActionHolder.addOriginActionCls(bindDynamicActionProxy.targetActionClass(), dynamicAction.getClass());
        }
    }

    private <T> T newInstance(Class<T> aClass, Object... initArgs) {
        try {
            return aClass.getConstructor().newInstance(initArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
