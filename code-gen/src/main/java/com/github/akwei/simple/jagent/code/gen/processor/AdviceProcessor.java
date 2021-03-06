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

import com.github.akwei.simple.jagent.core.annotation.BindDynamicAdvice;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.github.akwei.simple.jagent.core.annotation.BindDynamicAdvice"})
@AutoService(Processor.class)
public class AdviceProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeUtils = processingEnv.getTypeUtils();
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindDynamicAdvice.class);
        for (Element element : elements) {
            PackageElement packageOf = this.elementUtils.getPackageOf(element);
            String packageName = packageOf.getQualifiedName().toString();
            String actionFullClassName = ((TypeElement) element).getQualifiedName().toString();
            String adviceClassName = element.getSimpleName().toString() + "DynamicAdvice";
            AdviceGenerator.createAdviceCodeFile(packageName, adviceClassName, actionFullClassName, this.filer);
            System.out.println("generate dynamic advice: [" + packageName + "." + adviceClassName + "]");
        }
        return false;
    }
}
