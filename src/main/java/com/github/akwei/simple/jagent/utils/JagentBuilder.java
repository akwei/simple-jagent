//package com.github.akwei.simple.jagent.utils;
//
//import net.bytebuddy.ByteBuddy;
//import net.bytebuddy.agent.builder.AgentBuilder;
//import net.bytebuddy.description.type.TypeDescription;
//import net.bytebuddy.dynamic.ClassFileLocator;
//import net.bytebuddy.dynamic.DynamicType;
//import net.bytebuddy.pool.TypePool;
//
//import java.util.List;
//
//public class JagentBuilder {
//
//    private ByteBuddy byteBuddy;
//
//    public static ByteBuddy init() {
//        return new ByteBuddy();
//    }
//
//    public void load(ByteBuddy byteBuddy, ClassLoader cl, List<JagentTransformer> jagentTransformers) {
////        ClassFileLocator classFileLocator = ClassFileLocator.ForClassLoader.of(cl);
////        TypePool typePool = TypePool.Default.of(classFileLocator);
////        for (JagentTransformer jagentTransformer : jagentTransformers) {
////            TypeDescription typeDescription = typePool.describe(jagentTransformer.getType()).resolve();
////            for (AgentBuilder.Transformer transformer : jagentTransformer.getTransformers()) {
////                DynamicType.Builder<?> builder = byteBuddy.redefine(typeDescription, classFileLocator);
////                builder = transformer.transform(builder, typeDescription, cl, null);
////            }
////        }
//    }
//}
