package com.butterknife.complier;

import com.butterknife.annotations.BindView;
import com.butterknife.annotations.OnClick;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class ButterknifeProcessor extends AbstractProcessor {

    private Filer filer;
    private Elements mElementUtils;
    private List<Element> viewElements;
    private List<Element> clickElements;
    private final ClassName unbinderClassName = ClassName.get("com.butterknife","Unbinder");
    private final ClassName onClickClassName = ClassName.get("android.view.View","OnClickListener");
    private final ClassName viewClass = ClassName.get("android.view","View");
    private Map<Element,List<Element>> elementMapView = new LinkedHashMap<>();
    private  Map<Element,List<Element>> elementMapOnclick = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    //指定处理的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    //给到需要处理的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        //需要解析的自定义注解
        annotations.add(BindView.class);

        annotations.add(OnClick.class);
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //解析OnClick
        parserOnClick(roundEnvironment);
        //解析BindView
        parserBindView(roundEnvironment);
        //写java文件
        for (Map.Entry<Element,List<Element>> entry:elementMapView.entrySet()){
            Element enclosingElement = entry.getKey();
            List<Element> viewBindElements = entry.getValue();
            List<Element> elementListOnclick = elementMapOnclick.get(enclosingElement);
            String activityClassNamestr = enclosingElement.getSimpleName().toString();
            ClassName activityClassName = ClassName.bestGuess(activityClassNamestr);
            TypeSpec.Builder classBuidler = TypeSpec.classBuilder(activityClassNamestr+
                    "_ViewBinding")
                    .addModifiers(Modifier.FINAL,Modifier.PUBLIC)
                    .addSuperinterface(unbinderClassName)
                    .addField(activityClassName,"target",Modifier.PRIVATE)
                    .addField(viewClass,"view",Modifier.PRIVATE);
            //解析注解
            MethodSpec.Builder unbindMethodBuilder = writeUnbind(activityClassName);
            MethodSpec.Builder constructor = writeConstructor(viewBindElements, activityClassName, unbindMethodBuilder);
            if(elementListOnclick != null) {
                writeOnclick(elementListOnclick, classBuidler, constructor);
            }
            classBuidler.addMethod(constructor.build());
            classBuidler.addMethod(unbindMethodBuilder.build());
            //生成类
            String packageName = mElementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
            try {
                JavaFile.builder(packageName,classBuidler.build())
                        .addFileComment("  ")
                        .build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private void parserBindView(RoundEnvironment roundEnvironment){
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element: elements){
            Element enclosingElement = element.getEnclosingElement();
            List<Element> viewBindElements = elementMapView.get(enclosingElement);
            if(viewBindElements == null){
                viewBindElements = new ArrayList<>();
                elementMapView.put(enclosingElement,viewBindElements);
            }
            viewBindElements.add(element);
        }
    }
    private void parserOnClick(RoundEnvironment roundEnvironment){
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        for (Element element: elementsAnnotatedWith){
            Element enclosingElement = element.getEnclosingElement();

            List<Element> viewBindElements = elementMapOnclick.get(enclosingElement);
            if(viewBindElements == null){
                viewBindElements = new ArrayList<>();
                elementMapOnclick.put(enclosingElement,viewBindElements);
            }
            viewBindElements.add(element);
        }
    }


    private MethodSpec.Builder writeUnbind(ClassName activityClassName){
        //实现Unbind方法
        ClassName callSuperClassName = ClassName.get("android.support.annotation","CallSuper");
        MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("unbind")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addAnnotation(callSuperClassName);
        unbindMethodBuilder.addStatement("$T target = this.target",activityClassName);
        unbindMethodBuilder.addStatement("if(target == null) throw new IllegalStateException(\"Binding already cleared.\")");

        return unbindMethodBuilder;
    }

    private MethodSpec.Builder writeConstructor(List<Element> viewBindElements, ClassName activityClassName, MethodSpec.Builder unbindMethodBuilder){
        //构造函数
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder()
                .addParameter(activityClassName,"target")
                .addParameter(viewClass,"view");
        constructorMethodBuilder.addStatement("this.target = target")
                .addStatement("this.view= view");

        ClassName utilsClassName = ClassName.get("com.butterknife","Utils");


        //获取定义的单个属性
        for (Element viewBindElement: viewBindElements){
            String filedName = viewBindElement.getSimpleName().toString();
            int resId = viewBindElement.getAnnotation(BindView.class).value();
            constructorMethodBuilder.addStatement("target.$L = $T.findViewById(view,$L)",filedName,utilsClassName,resId);
            unbindMethodBuilder.addStatement("target.$L = null",filedName);
        }

        return constructorMethodBuilder;
    }


    private void writeOnclick(List<Element> elementListOnclick,TypeSpec.Builder classBuidler, MethodSpec.Builder constructorMethodBuilder){
        MethodSpec.Builder clickMethodBuilder = MethodSpec.methodBuilder("onClick")
                .addParameter(viewClass,"view")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        if(elementListOnclick != null){
            classBuidler.addSuperinterface(onClickClassName);
            clickMethodBuilder.beginControlFlow("switch (view.getId())");
            for (Element element:elementListOnclick){

                int[] value = element.getAnnotation(OnClick.class).value();
                String methodName = element.getSimpleName().toString();
                for (int i=0;i < value.length;i++){
                    constructorMethodBuilder.addStatement("view.findViewById($L).setOnClickListener(this)",value[i]);
                    clickMethodBuilder.addStatement("case $L:\ntarget.$L()", value[i],methodName);
                    clickMethodBuilder.addStatement("break");
                }
            }
            clickMethodBuilder.endControlFlow();
        }
        classBuidler.addMethod(clickMethodBuilder.build());
    }

}
