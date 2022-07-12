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


import com.github.akwei.simple.jagent.core.ContextInfo;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.apache.commons.lang3.StringUtils;

public class BeanDynamicAction {

    @Advice.OnMethodEnter
    public void onEnter(@Advice.This Object self,
                        @Advice.Origin("#m") String method,
                        @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
                        ContextInfo contextInfo
    ) {
        System.out.println("BeanDynamicAction begin =======");
        String substring = StringUtils.substring("1234567890123", 0, 3);
        System.out.println(substring);
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public Object onExit(
            @Advice.This Object invoker,
            @Advice.Origin("#m") String method,
            @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object retValue,
            @Advice.Thrown Throwable throwable,
            @Advice.Enter ContextInfo contextInfo
    ) {
        System.out.println("BeanDynamicAction end ---------");
        return retValue + " - auto by byte-buddy";
    }


}
