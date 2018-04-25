package com.butterknife.complier;

import com.butterknife.annotations.BindView;
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
        /*
        annotations.add(BindAnim.class);
        annotations.add(BindArray.class);
        annotations.add(BindBitmap.class);
        annotations.add(BindBool.class);
        annotations.add(BindColor.class);
        annotations.add(BindDimen.class);
        annotations.add(BindDrawable.class);
        annotations.add(BindFloat.class);
        annotations.add(BindFont.class);
        annotations.add(BindInt.class);
        annotations.add(BindString.class);
        annotations.add(BindViews.class);
        annotations.addAll(LISTENERS);*/

        //需要解析的自定义注解
        annotations.add(BindView.class);


        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //process方法代表的是， 有注解就都会进来。
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
       /* for (Element element:
             elements) {
            //System.out.println("---------------------------------->"+element.getSimpleName().toString());
            Element enclosingElement = element.getEnclosingElement();
            System.out.println("----------------------->"+enclosingElement.getSimpleName().toString()
            +""+element.getSimpleName().toString());
        }*/
        //解析属性， 一个activity对应的应该是一个List<Element>

        Map<Element,List<Element>> elementMap = new LinkedHashMap<>();
        for (Element element: elements){
            Element enclosingElement = element.getEnclosingElement();

            List<Element> viewBindElements = elementMap.get(enclosingElement);
            if(viewBindElements == null){
                viewBindElements = new ArrayList<>();
                elementMap.put(enclosingElement,viewBindElements);
            }
            viewBindElements.add(element);
        }

        //生成代码


        for (Map.Entry<Element,List<Element>> entry:elementMap.entrySet()){
            Element enclosingElement = entry.getKey();
            List<Element> viewBindElements = entry.getValue();
            //实现Unbind方法
            ClassName callSuperClassName = ClassName.get("android.support.annotation","CallSuper");
            MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("unbind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                    .addAnnotation(callSuperClassName);


            //拼接 public final class xxx
            ClassName unbinderClassName = ClassName.get("com.butterknife","Unbinder");
            TypeSpec.Builder classBuidler = TypeSpec.classBuilder(enclosingElement.getSimpleName().toString()+
                    "_ViewBinding")
                    .addModifiers(Modifier.FINAL,Modifier.PUBLIC)
                    .addSuperinterface(unbinderClassName);



            classBuidler.addMethod(unbindMethodBuilder.build());



            //生成类，看一下效果
            String packageName = mElementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
            try {
                System.out.println("---------------------------》》生成了");
                JavaFile.builder(packageName,classBuidler.build())
                        .addFileComment("butterknife 自动生成")
                    .build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("---------------------------》》报异常了");
            }
        }

        return false;
    }
}
