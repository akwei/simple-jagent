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


import com.github.akwei.simple.jagent.core.AbstractDefinitionModule;
import com.github.akwei.simple.jagent.core.MatcherDefinition;
import com.github.akwei.simple.jagent.core.TransformerDefinition;
import com.github.akwei.simple.jagent.core.annotation.BindDefinitionModule;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

@BindDefinitionModule
public class BeanDefinitionModule extends AbstractDefinitionModule {

    @Override
    public void configure(List<TransformerDefinition> transformerDefinitions) {
        transformerDefinitions.add(
                new TransformerDefinition()
                        .type(ElementMatchers.nameEndsWith("Bean"))
                        .addMatcherDefinition(
                                new MatcherDefinition()
                                        .matcher(ElementMatchers.named("say"))
                                        .actionClass(BeanAction.class)
                        )
                        .addDynamicFieldName("dynName")
//                        .addTransformer((builder, typeDescription, classLoader, module) -> builder.defineField("dynName", Object.class, Visibility.PRIVATE)
//                                .implement(AgentFieldAccessor.class).intercept(FieldAccessor.ofField("dynName")))
        );
        transformerDefinitions.add(
                new TransformerDefinition()
                        .type(ElementMatchers.nameEndsWith("Bean"))
                        .addMatcherDefinition(
                                new MatcherDefinition()
                                        .matcher(ElementMatchers.named("say"))
                                        .actionClass(BeanDynamicAction.class)
                        )
                        .addDynamicFieldName("dynName")
        );

    }
}
