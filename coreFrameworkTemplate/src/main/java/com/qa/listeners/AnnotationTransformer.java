package com.qa.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.qa.base.TestBase;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.CustomAttribute;
import org.testng.annotations.ITestAnnotation;

public class AnnotationTransformer extends TestBase implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

        annotation.setRetryAnalyzer(Retry.class);

        CustomAttribute[] attributes = annotation.getAttributes();

        if(attributes.length > 0) {
            if (attributes[0].name().equalsIgnoreCase("data-driven")) {
                annotation.setDataProvider("getDataDriven");
                annotation.setDataProviderClass(TestBase.class);
            }

            excelFilePath.set(attributes[0].values()[0]);
        }
    }
}
