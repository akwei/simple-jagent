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

package com.github.akwei.simple.jagent.agent.component.ctrl;

import com.github.akwei.simple.jagent.agent.core.AbstractDefinitionModule;
import com.github.akwei.simple.jagent.agent.core.MatcherDefinition;
import com.github.akwei.simple.jagent.agent.core.TransformerDefinition;
import com.github.akwei.simple.jagent.agent.core.annotation.BindDefinitionModule;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.nameEndsWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

@BindDefinitionModule
public class CtrlDefinitionModule extends AbstractDefinitionModule {
    @Override
    public void configure(List<TransformerDefinition> transformerDefinitions) {
        transformerDefinitions.add(
                new TransformerDefinition()
                        .type(nameEndsWith("Ctrl"))
                        .addMatcherDefinition(
                                new MatcherDefinition()
                                        .matcher(named("execute"))
                                        .actionClass(CtrlAction.class)
                        )
        );
    }
}
