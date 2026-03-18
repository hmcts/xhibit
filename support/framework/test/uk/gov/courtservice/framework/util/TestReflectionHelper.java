package uk.gov.courtservice.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * <p>
 * Title: TestReflectionHelper
 * </p>
 * <p>
 * Description: Test case for ReflectionHelper class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author AW Daley
 */
/*
 * Ref Date Author Description
 * 
 * 83,52539 29-04-2003 AW Daley Initial version.
 */
public class TestReflectionHelper extends TestCase {

    public TestReflectionHelper(String s) {
        super(s);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testGetFieldFromClassHierarchy() {
        try {
            Class cls1 = new TestComplexValue("TestValue").getClass();
            String fieldName2 = "basicValueVar";
            Field fieldRet = ReflectionHelper.getFieldFromClassHierarchy(cls1, fieldName2);

            assertEquals(fieldRet.getName(), fieldName2);
        }

        catch (NoSuchFieldException success) {
            fail("NoSuchFieldException thrown");
        }

        /** @todo: Insert test code here. Use assertEquals(), for example. */
    }

    public void testGetInvalidFieldFromClassHierarchy() {
        try {
            Class cls1 = new TestComplexValue("TestValue").getClass();
            String fieldName2 = "invalidField";
            ReflectionHelper.getFieldFromClassHierarchy(cls1, fieldName2);
            fail("NoSuchFieldException not thrown");
        }

        catch (NoSuchFieldException success) {
            assertEquals(success.getLocalizedMessage(), "invalidField");
        }

    }

    public void testGetMethodFromClassHierarchy() {
        try {
            Class cls1 = new TestComplexValue("TestValue").getClass();
            String methodName2 = "getBasicValueVar";
            Class[] parameterTypes = {};
            Method methodRet = ReflectionHelper.getMethodFromClassHierarchy(cls1, methodName2, parameterTypes);
            assertEquals(methodRet.getName(), methodName2);
        }

        catch (NoSuchMethodException success) {
            fail("NoSuchMethodException thrown");
        }
    }

    public void testGetInvalidMethodFromClassHierarchy() {
        try {
            Class cls1 = new TestComplexValue("TestValue").getClass();
            String methodName2 = "getInvalidField";
            Class[] parameterTypes = {};
            ReflectionHelper.getMethodFromClassHierarchy(cls1, methodName2, parameterTypes);
            fail("NoSuchMethodException not thrown");
        }

        catch (NoSuchMethodException success) {
            assertEquals(success.getLocalizedMessage(), "getInvalidField");
        }
    }

    public void testGetFieldFromFieldPath() {
        Class cls1 = new TestComplexValue("TestValue").getClass();
        String fieldPath2 = "complexValueVar.containedClassVar";

        try {
            Field fieldRet = ReflectionHelper.getFieldFromFieldPath(cls1, fieldPath2, ".");
            assertEquals(fieldRet.getName(), "containedClassVar");
        }

        catch (NoSuchFieldException success) {
            fail("NoSuchFieldException thrown");
        }
    }

    public void testGetFieldFromInvalidFieldPath() {
        Class cls1 = new TestComplexValue("TestValue").getClass();
        String fieldPath2 = "complexValueVar.invalidField";

        try {
            ReflectionHelper.getFieldFromFieldPath(cls1, fieldPath2, ".");
            fail("NoSuchFieldException not thrown");
        }

        catch (NoSuchFieldException success) {
            assertEquals(success.getLocalizedMessage(), "invalidField");
        }
    }

    public void testGetGetterMethodForFieldPath() {
        Class cls1 = new TestComplexValue("TestValue").getClass();
        String fieldPath2 = "complexValueVar.containedClassVar";
        try {
            Method[] methodRet = ReflectionHelper.getGetterMethodForFieldPath(cls1, fieldPath2, ".");

            assertEquals(methodRet[0].getName(), "getComplexValueVar");
            assertEquals(methodRet[1].getName(), "getContainedClassVar");
        }

        catch (NoSuchFieldException success) {
            fail("NoSuchFieldException thrown");
        }

        catch (NoSuchMethodException success) {
            fail("NoSuchMethodException thrown");
        }
    }

    public void testGetGetterMethodForInvalidFieldPath() {
        Class cls1 = new TestComplexValue("TestValue").getClass();
        String fieldPath2 = "complexValueVar.invalidField";
        try {
            ReflectionHelper.getGetterMethodForFieldPath(cls1, fieldPath2, ".");
            fail("NoSuchFieldException not thrown");
        }

        catch (NoSuchFieldException success) {

            assertEquals(success.getLocalizedMessage(), "invalidField");
        }

        catch (NoSuchMethodException success) {
            assertEquals(success.getLocalizedMessage(), "invalidField");
        }
    }

    /**
     * <p>
     * Title: TestComplexValue
     * </p>
     * <p>
     * Description: Test data form ReflectionHelper
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author AW Daley
     * @version 1.0
     */
    public class TestComplexValue extends TestBasicValue {
        private TestContainedClass complexValueVar;

        public TestComplexValue(String complexValueVar) {
            super(complexValueVar);
            this.complexValueVar = new TestContainedClass(complexValueVar);
        }

        public TestContainedClass getComplexValueVar() {
            return complexValueVar;
        }

        public void setComplexValueVar(TestContainedClass ComplexValueVar) {
            this.complexValueVar = ComplexValueVar;
        }
    }

    /**
     * <p>
     * Title: TestBasicValue
     * </p>
     * <p>
     * Description: Test data form ReflectionHelper
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author AW Daley
     * @version 1.0
     */
    public class TestBasicValue {
        protected String basicValueVar;

        public TestBasicValue(String basicValueVar) {
            this.basicValueVar = basicValueVar;
        }

        protected String getBasicValueVar() {
            return basicValueVar;
        }

        protected void setBasicValueVar(String basicValueVar) {
            this.basicValueVar = basicValueVar;
        }
    }

    /**
     * <p>
     * Title: TestContainedClass
     * </p>
     * <p>
     * Description: Test data form ReflectionHelper
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author AW Daley
     * @version 1.0
     */
    public class TestContainedClass {
        private String containedClassVar;

        public TestContainedClass(String containedClassVar) {
            this.containedClassVar = containedClassVar;
        }

        public String getContainedClassVar() {

            return containedClassVar;
        }

        public void setContainedClassVar(String containedClassVar) {
            this.containedClassVar = containedClassVar;
        }
    }
}
