///*
// * Copyright 2022 akwei
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.github.akwei.simple.jagent.agent.component.bean;
//
//import com.github.akwei.simple.jagent.agent.component.BaseActionTest;
//import com.github.akwei.simple.jagent.core.DefinitionModule;
//import com.github.akwei.simple.jagent.core.ObjectInitializer;
//import com.google.inject.Module;
//import net.bytebuddy.ByteBuddy;
//import net.bytebuddy.asm.Advice;
//import net.bytebuddy.dynamic.ClassFileLocator;
//import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
//import net.bytebuddy.pool.TypePool;
//import org.junit.jupiter.api.Test;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//import static net.bytebuddy.matcher.ElementMatchers.named;
//
//public class DemoTest extends BaseActionTest {
//
//
//    @Test
//    public void create() throws Exception {
//
//        List<Module> modules = new ArrayList<>();
//        modules.add(new BeanActionModule());
//
//        List<DefinitionModule> definitionModules = new ArrayList<>();
//        definitionModules.add(new BeanDefinitionModule());
//
//        ObjectInitializer objectInitializer = new ObjectInitializer();
//        objectInitializer.init(modules, definitionModules);
//
//        ClassLoader classLoader = getClass().getClassLoader();
//        ClassFileLocator classFileLocator = ClassFileLocator.ForClassLoader.of(classLoader);
//        TypePool typePool = TypePool.Default.of(classFileLocator);
//        ByteBuddy byteBuddy = new ByteBuddy();
//        Class<?> aClass = byteBuddy.redefine(typePool.describe("com.github.akwei.simple.jagent.agent.component.bean.UserBean").resolve(), classFileLocator)
//                .visit(
//                        Advice.to(typePool.describe("com.github.akwei.simple.jagent.plugins.demo.bean.component.BeanAdvice").resolve(), classFileLocator)
//                                .on(named("say"))
//                )
//                .make()
//                .load(classLoader, ClassLoadingStrategy.Default.INJECTION)
//                .getLoaded();
//        UserBean o = (UserBean) aClass.getConstructor().newInstance();
//        o.say("akwei");
//    }
//
//    @Test
//    public void create1() throws Exception {
//
//        List<Module> modules = new ArrayList<>();
//        modules.add(new BeanActionModule());
//
//        List<DefinitionModule> definitionModules = new ArrayList<>();
//        definitionModules.add(new BeanDefinitionModule());
//
//        ObjectInitializer objectInitializer = new ObjectInitializer();
//        objectInitializer.init(modules, definitionModules);
//
//        List<Class<?>> classes = new AgentClassBuilder()
//                .classNames(new String[]{"com.github.akwei.simple.jagent.agent.component.bean.UserBean"})
//                .classLoader(getClass().getClassLoader())
//                .transformerDefinitions(objectInitializer.getTransformerDefinitions())
//                .loadClasses();
//        UserBean o = (UserBean) classes.get(0).getConstructor().newInstance();
//        o.say("akwei");
//
//        Field dynName = o.getClass().getDeclaredField("dynName");
//        System.out.println(dynName);
//    }
//
//}
