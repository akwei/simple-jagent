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

package com.github.akwei.simple.jagent.agent.component.bean;

import com.github.akwei.simple.jagent.agent.core.Action;
import com.github.akwei.simple.jagent.agent.core.annotation.BindAdvice;
import org.apache.commons.lang3.StringUtils;

@BindAdvice(adviceClass = BeanAdvice.class)
public class BeanAction implements Action {

    @Override
    public void onEnter(Object self, String method, Object[] args) {
        System.out.println("begin =======");
        String substring = StringUtils.substring("1234567890123", 0, 3);
        System.out.println(substring);
    }

    @Override
    public Object onExit(Object invoker, String method, Object[] args, Object retValue, Throwable throwable) {
        System.out.println("end ---------");
        return retValue + " - auto by byte-buddy";
    }
}
