//package uk.gov.courtservice.framework.util;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
//
///**
// * <p>
// * Title: TestReflectionEqualsHelper
// * </p>
// * <p>
// * Description: Test case for ReflectionEqualsHelper class. For this test case
// * to work CSAbstractValue will need to implement the 'equals' method by calling
// * 'equals' method on the ReflectionEqualsHelper. To implement the equals method
// * for a value object using this helper is deemed too risky at such a late
// * stage.
// * </p>
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// * <p>
// * Company: EDS
// * </p>
// *
// * @author AW Daley
// */
///*
// * Ref Date Author Description . 29-04-2003 AW Daley Initial version.
// */
//public class TestReflectionEqualsHelper extends TestCase {
//
//    public TestReflectionEqualsHelper(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testEquals() {
//
//        // TestBasicValue uses the RefelctionEqualsHelper to check for
//        // equality by extending CSAbstractValue which uses
//        // the RelectionEqualsHelper to implement its 'equals' method.
//
//        // Check returns true for objects that are equal
//        TestComplexValue obj1 = new TestComplexValue("TestValue1", 1);
//        TestComplexValue obj2 = new TestComplexValue("TestValue1", 1);
//        assertEquals("complex value 1 vs complex value 2", obj1, obj2);
//        assertEquals("complex value 2 vs complex value 1", obj2, obj1);
//
//        // Check return false for objects that are not equal.
//        TestComplexValue obj3 = new TestComplexValue("TestValue3", 3);
//        assertTrue("complex value 1 != complex value 3", !obj1.equals(obj3));
//
//        // Ensure equality does not work for objects of differing types
//        TestBasicValue obj4 = new TestBasicValue("TestValue4");
//        assertTrue("complex value 1 != basic value 4", !obj1.equals(obj4));
//
//        // If any attribute is null then objects the equality should fail
//        TestBasicValue obj5 = new TestComplexValue(null, 1);
//        assertTrue("complex value 1 != complex value 5 with null id", !obj1.equals(obj5));
//    }
//
//    /**
//     * <p>
//     * Title: TestComplexValue
//     * </p>
//     * <p>
//     * Description: Test data form ReflectionHelper
//     * </p>
//     * <p>
//     * Copyright: Copyright (c) 2003
//     * </p>
//     * <p>
//     * Company: EDS
//     * </p>
//     *
//     * @author AW Daley
//     * @version 1.0
//     */
//    public class TestComplexValue extends TestBasicValue {
//        private TestContainedClass complexValueVar;
//
//        private int primativeVar;
//
//        private Collection collectionVar;
//
//        public TestComplexValue(String complexValueVar, int primativeVar) {
//            super(complexValueVar);
//            this.complexValueVar = new TestContainedClass(complexValueVar);
//            this.primativeVar = primativeVar;
//
//            collectionVar = new ArrayList();
//            collectionVar.add(new TestContainedClass(complexValueVar));
//            collectionVar.add(new TestContainedClass(complexValueVar));
//
//        }
//
//        public TestContainedClass getComplexValueVar() {
//            return complexValueVar;
//        }
//
//        public void setComplexValueVar(TestContainedClass ComplexValueVar) {
//            this.complexValueVar = ComplexValueVar;
//        }
//
//        public int getPrimativeVar() {
//            return this.primativeVar;
//        }
//
//        public void setPrimativeVar(int primativeVar) {
//            this.primativeVar = primativeVar;
//        }
//
//        public Collection getCollectionVar() {
//            return this.collectionVar;
//        }
//
//        public void setCollectionVar(Collection collectionVar) {
//            this.collectionVar = collectionVar;
//        }
//    }
//
//    /**
//     * <p>
//     * Title: TestBasicValue
//     * </p>
//     * <p>
//     * Description: Test data form ReflectionHelper
//     * </p>
//     * <p>
//     * Copyright: Copyright (c) 2003
//     * </p>
//     * <p>
//     * Company: EDS
//     * </p>
//     *
//     * @author AW Daley
//     * @version 1.0
//     */
//    public class TestBasicValue extends CSAbstractValue {
//        protected String basicValueVar;
//
//        public TestBasicValue(String basicValueVar) {
//            this.basicValueVar = basicValueVar;
//        }
//
//        protected String getBasicValueVar() {
//            return basicValueVar;
//        }
//
//        protected void setBasicValueVar(String basicValueVar) {
//            this.basicValueVar = basicValueVar;
//        }
//    }
//
//    /**
//     * <p>
//     * Title: TestContainedClass
//     * </p>
//     * <p>
//     * Description: Test data form ReflectionHelper
//     * </p>
//     * <p>
//     * Copyright: Copyright (c) 2003
//     * </p>
//     * <p>
//     * Company: EDS
//     * </p>
//     *
//     * @author AW Daley
//     * @version 1.0
//     */
//    public class TestContainedClass extends CSAbstractValue {
//        private String containedClassVar;
//
//        public TestContainedClass(String containedClassVar) {
//            this.containedClassVar = containedClassVar;
//        }
//
//        public String getContainedClassVar() {
//
//            return containedClassVar;
//        }
//
//        public void setContainedClassVar(String containedClassVar) {
//            this.containedClassVar = containedClassVar;
//        }
//    }
//}
//