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
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.util.ArrayList;
import java.util.List;

public class CompoundTransformer implements AgentBuilder.Transformer {
    private final List<AgentBuilder.Transformer> transformers;

    public CompoundTransformer(List<AgentBuilder.Transformer> transformers) {
        this.transformers = new ArrayList<>();
        for (AgentBuilder.Transformer transformer : transformers) {
            if (transformer instanceof CompoundTransformer) {
                this.transformers.addAll(((CompoundTransformer) transformer).transformers);
                continue;
            }
            this.transformers.add(transformer);
        }
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
        for (AgentBuilder.Transformer transformer : this.transformers) {
            builder = transformer.transform(builder, typeDescription, classLoader, module);
        }
        return builder;
    }
}
