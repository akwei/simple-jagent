//package com.github.akwei.simple.jagent;
//
//import com.sun.istack.internal.Nullable;
//import net.bytebuddy.ByteBuddy;
//import net.bytebuddy.asm.Advice;
//import net.bytebuddy.dynamic.ClassFileLocator;
//import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
//import net.bytebuddy.pool.TypePool;
//import org.junit.jupiter.api.Test;
//
//import static net.bytebuddy.matcher.ElementMatchers.*;
//
//public class DemoTest {
//
//    @Test
//    public void create() throws Exception {
//        ClassLoader classLoader = getClass().getClassLoader();
//        ClassFileLocator classFileLocator = ClassFileLocator.ForClassLoader.of(classLoader);
//        TypePool typePool = TypePool.Default.of(classFileLocator);
//        ByteBuddy byteBuddy = new ByteBuddy();
//        Class<?> aClass = byteBuddy.redefine(typePool.describe("com.github.akwei.simple.jagent.User").resolve(), classFileLocator)
//                .visit(
//                        Advice.to(typePool.describe("com.github.akwei.simple.jagent.AdviceAction").resolve(), classFileLocator)
//                                .on(named("say"))
//                )
//                .make()
//                .load(classLoader, ClassLoadingStrategy.Default.INJECTION)
//                .getLoaded();
//        User o = (User) aClass.getConstructor().newInstance();
//        o.say("akwei");
//    }
//
//    @Test
//    public void create2() throws Exception {
//        ClassLoader classLoader = getClass().getClassLoader();
//        ClassFileLocator classFileLocator = ClassFileLocator.ForClassLoader.of(classLoader);
//        TypePool typePool = TypePool.Default.of(classFileLocator);
//        ByteBuddy byteBuddy = new ByteBuddy();
////        String clsName="com.github.akwei.simple.jagent.DemoTest$LocalMemoryDatabase";
//        String clsName = "com.github.akwei.simple.jagent.MemoryDatabase";
//        byteBuddy.redefine(typePool.describe(clsName).resolve(), classFileLocator)
//                .visit(
//                        Advice.to(typePool.describe("com.github.akwei.simple.jagent.AdviceAction").resolve(), classFileLocator)
//                                .on(named("say")
////                                        .and(isOverriddenFrom(typePool.describe("com.github.akwei.simple.jagent.MemoryDatabase").resolve()))
//                                )
//                )
//                .make()
//                .load(classLoader, ClassLoadingStrategy.Default.INJECTION);
//
//        clsName = "com.github.akwei.simple.jagent.DemoTest$LocalMemoryDatabase";
//        Class<?> aClass = byteBuddy.redefine(typePool.describe(clsName).resolve(), classFileLocator)
////                .visit(
////                        Advice.to(typePool.describe("com.github.akwei.simple.jagent.AdviceAction").resolve(), classFileLocator)
////                                .on(named("say")
//////                                        .and(isOverriddenFrom(typePool.describe("com.github.akwei.simple.jagent.MemoryDatabase").resolve()))
////                                )
////                )
//                .make()
//                .load(classLoader, ClassLoadingStrategy.Default.INJECTION)
//                .getLoaded();
//        LocalMemoryDatabase o = (LocalMemoryDatabase) aClass.getConstructor().newInstance();
//        o.say("akwei");
//    }
//
//    public static class LocalMemoryDatabase extends MemoryDatabase {
////        @Override
////        public void say(String name) {
////            super.say(name);
////        }
//    }
//
//}
