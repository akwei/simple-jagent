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

package com.github.akwei.simple.jagent.code.gen.processor;

import com.github.akwei.simple.jagent.core.ActionHolder;
import com.github.akwei.simple.jagent.core.ContextInfo;
import com.squareup.javapoet.*;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;


public class AdviceGenerator {

    public static void createAdviceCodeFile(String packageName, String className, String actionName, Filer filer) {

        // createOnExit
        MethodSpec onEnter = MethodSpec.methodBuilder("onEnter")
                .addAnnotation(Advice.OnMethodEnter.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ContextInfo.class)
                .addParameter(
                        ParameterSpec.builder(Object.class, "self")
                                .addAnnotation(Advice.This.class)
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(String.class, "method")
                                .addAnnotation(
                                        AnnotationSpec.builder(Advice.Origin.class)
                                                .addMember("value", "$S", "#m")
                                                .build())
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(Object[].class, "args")
                                .addAnnotation(
                                        AnnotationSpec.builder(Advice.AllArguments.class)
                                                .addMember("readOnly", "false")
                                                .addMember("typing", "$T.DYNAMIC", Assigner.Typing.class)
                                                .build())
                                .build()
                )
                .addStatement("$T contextInfo = new $T()", ContextInfo.class, ContextInfo.class)
                .addStatement("Object[] objects = $T.copyOf(args, args.length)", Arrays.class)
                .addStatement("$T.getAction(ACTION_NAME).onEnter(self, method, objects, contextInfo)", ActionHolder.class)
                .addStatement("args = objects")
                .addStatement("return contextInfo")
                .build();

        // createOnExit
        MethodSpec onExit = MethodSpec.methodBuilder("onExit")
                .addAnnotation(AnnotationSpec.builder(Advice.OnMethodExit.class)
                        .addMember("onThrowable", "$T.class", Throwable.class)
                        .build())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(
                        ParameterSpec.builder(Object.class, "self")
                                .addAnnotation(Advice.This.class)
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(String.class, "method")
                                .addAnnotation(
                                        AnnotationSpec.builder(Advice.Origin.class)
                                                .addMember("value", "$S", "#m")
                                                .build())
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(Object[].class, "args")
                                .addAnnotation(
                                        AnnotationSpec.builder(Advice.AllArguments.class)
                                                .addMember("readOnly", "false")
                                                .addMember("typing", "$T.DYNAMIC", Assigner.Typing.class)
                                                .build())
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(Object.class, "retValue")
                                .addAnnotation(
                                        AnnotationSpec.builder(Advice.Return.class)
                                                .addMember("readOnly", "false")
                                                .addMember("typing", "$T.DYNAMIC", Assigner.Typing.class)
                                                .build())
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(Throwable.class, "throwable")
                                .addAnnotation(
                                        AnnotationSpec.builder(Advice.Thrown.class)
                                                .build())
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(ContextInfo.class, "contextInfo")
                                .addAnnotation(
                                        AnnotationSpec.builder(Advice.Enter.class)
                                                .build())
                                .build()
                )
                .addStatement("Object[] objects = $T.copyOf(args, args.length)", Arrays.class)
                .addStatement("retValue = $T.getAction(ACTION_NAME).onExit(self, method, objects, retValue, throwable, contextInfo)", ActionHolder.class)
                .addStatement("args = objects")
                .build();


        TypeSpec advice = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(FieldSpec.builder(String.class, "ACTION_NAME", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("$S", actionName).build())
                .addMethod(onEnter)
                .addMethod(onExit)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, advice)
                .indent("  ")
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
