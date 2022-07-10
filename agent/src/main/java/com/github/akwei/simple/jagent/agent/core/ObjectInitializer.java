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

import com.github.akwei.simple.jagent.agent.core.annotation.BindActionModule;
import com.github.akwei.simple.jagent.agent.core.annotation.BindDefinitionModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ObjectInitializer {

    private final List<TransformerDefinition> transformerDefinitions = new ArrayList<>();

    private Injector injector;

    public void init(String scanPackagePrefix) {
        Reflections reflections = new Reflections(scanPackagePrefix);
        this.init(this.findActionModules(reflections), this.findDefinitionModules(reflections));
    }

    public void init(List<Module> modules, List<DefinitionModule> definitionModules) {
        this.initInjector(modules);
        this.initDefinitionConfig(definitionModules);
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
                Class<? extends Action> aClass = matcherDefinition.actionClass();
                Action action = injector.getInstance(aClass);
                ActionHolder.add(matcherDefinition.adviceName(), action);
            }
        }
    }

    private List<DefinitionModule> findDefinitionModules(Reflections reflections) {
        Set<Class<?>> classesOfDefinitionConfig = reflections.get(TypesAnnotated.with(BindDefinitionModule.class).asClass());
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
        Set<Class<?>> classesOfActionModule = reflections.get(TypesAnnotated.with(BindActionModule.class).asClass());
        List<Module> modules = new ArrayList<>();
        for (Class<?> aClass : classesOfActionModule) {
            AbstractModule module = (AbstractModule) newInstance(aClass);
            modules.add(module);
        }
        return modules;
    }

    private <T> T newInstance(Class<T> aClass) {
        try {
            return aClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
