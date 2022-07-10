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

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransformerDefinition {

    private ElementMatcher<NamedElement> type;

    private final List<AgentBuilder.Transformer> transformers = new ArrayList<>();

    private final List<MatcherDefinition> matcherDefinitions = new ArrayList<>();

    private final Set<String> dynamicFieldNames = new HashSet<>();

    public ElementMatcher<NamedElement> type() {
        return type;
    }

    public TransformerDefinition type(ElementMatcher<NamedElement> typeElementMatcher) {
        this.type = typeElementMatcher;
        return this;
    }

    public TransformerDefinition addMatcherDefinition(MatcherDefinition matcherDefinition) {
        this.matcherDefinitions.add(matcherDefinition);
        return this;
    }

    public List<MatcherDefinition> matcherDefinitions() {
        return matcherDefinitions;
    }

    public TransformerDefinition addTransformer(AgentBuilder.Transformer transformer) {
        this.transformers.add(transformer);
        return this;
    }

    public TransformerDefinition addDynamicFieldName(String fieldName) {
        this.dynamicFieldNames.add(fieldName);
        return this;
    }

    public Set<String> dynamicFieldNames() {
        return dynamicFieldNames;
    }

    public List<AgentBuilder.Transformer> transformers() {
        return transformers;
    }

    public AgentBuilder.Transformer compoundTransformer() {
        List<AgentBuilder.Transformer> list = new ArrayList<>(this.transformers);
        for (MatcherDefinition matcherDefinition : this.matcherDefinitions) {
            list.add((builder, typeDescription, classLoader, module) -> new AgentBuilder.Transformer.ForAdvice()
                    .include(this.getClass().getClassLoader())
                    .advice(matcherDefinition.matcher(), matcherDefinition.adviceName())
                    .transform(builder, typeDescription, classLoader, module));
        }
        for (String dynamicFieldName : this.dynamicFieldNames) {
            list.add((builder, typeDescription, classLoader, module) -> builder.defineField(dynamicFieldName, Object.class, Visibility.PRIVATE)
                    .implement(AgentFieldAccessor.class)
                    .intercept(FieldAccessor.ofField(dynamicFieldName))
            );
        }
        return new CompoundTransformer(list);
    }

}
