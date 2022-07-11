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

public class UserBean {

    private int userId;

    public String say(String name) {
        System.out.println("user say: " + name);
        return name;
    }

    public static void main(String[] args) {
        UserBean userBean = new UserBean();
        String say = userBean.say("akwei");
        System.out.println("say result: " + say);
    }
}
