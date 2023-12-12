package pl.mwasyluk.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {
    CustomArrayList<TestElement> testList;
    TestElement e1 = new TestElement(6, "Test1");
    TestElement e2 = new TestElement(2, "Test2");
    TestElement e3 = new TestElement(4, "Test3");

    @BeforeEach
    void setUp() {
        testList = new CustomArrayList<>();
    }

    CustomArrayList<TestElement> getListWithFilledArray(){
        CustomArrayList <TestElement> list = new CustomArrayList<>(6);
        list.add(e1);
        list.add(e1);
        list.add(e1);
        list.add(e1);
        list.add(e1);
        list.add(e1);
        return list;
    }

    @Nested
    @DisplayName("add method")
    class AddMethodTest {

        @Test
        @DisplayName("adds element when list is empty")
        void addsElementWhenListIsEmpty(){
            testList.add(e1);

            assertEquals(e1, testList.get(0));
            assertEquals(1, testList.size());
        }

        @Test
        @DisplayName("adds element when list is not empty")
        void addsElementWhenListIsNotEmpty(){
            testList.add(e1);
            testList.add(e2);

            assertEquals(e1, testList.get(0));
            assertEquals(e2, testList.get(1));
            assertEquals(2, testList.size());
        }

        @Test
        @DisplayName("increases array length when required")
        void increasesArrayLengthWhenRequired() throws NoSuchFieldException, IllegalAccessException {
            Field arrayField = CustomArrayList.class.getDeclaredField("array");
            arrayField.setAccessible(true);

            CustomArrayList<TestElement> listWithFilledArray = getListWithFilledArray();
            Object[] startArray = (Object[]) arrayField.get(listWithFilledArray);
            listWithFilledArray.add(e1);
            Object[] endArray = (Object[]) arrayField.get(listWithFilledArray);

            assertTrue(startArray.length < endArray.length);
            assertEquals(startArray.length + (startArray.length >> 1), endArray.length);
        }

        @Test
        @DisplayName("adds element when it is null")
        void addsElementWhenItIsNull(){
            testList.add(null);

            assertEquals(1, testList.size());
            assertNull( testList.get(0));
        }

        @Test
        @DisplayName("adds both elements when they are the same object")
        void addsBothElementsWhenTheyAreTheSameObject(){
            testList.add(e1);
            testList.add(e1);

            assertEquals(2, testList.size());
            assertEquals(testList.get(0), testList.get(1));
        }

        @Test
        @DisplayName("adds element in correct position when already removed another")
        void addsElementInCorrectPositionWhenAlreadyRemovedAnother(){
            testList.add(e1);
            testList.add(e2);
            testList.remove(1);
            testList.add(e3);

            assertEquals(1, testList.indexOf(e3));
        }
    }

    @Nested
    @DisplayName("assertIndexInBounds method")
    class AssertIndexInBoundsMethodTest{
        Method assertIndexInBoundsMethod;

        public AssertIndexInBoundsMethodTest() throws NoSuchMethodException {
            this.assertIndexInBoundsMethod = CustomArrayList.class.getDeclaredMethod("assertIndexInBounds", int.class);
            this.assertIndexInBoundsMethod.setAccessible(true);
        }

        void invokeAssertIndexInBounds(CustomArrayList<?> list, int i) throws IllegalAccessException {
            try {
                assertIndexInBoundsMethod.invoke(list, i);
            } catch (InvocationTargetException e) {
                if (e.getCause() != null &&
                        e.getCause().getClass() == IndexOutOfBoundsException.class){
                    throw new IndexOutOfBoundsException();
                }
                throw new RuntimeException(e);
            }
        }

        void invokeAssertIndexInBounds(int i) throws IllegalAccessException {
            invokeAssertIndexInBounds(testList, i);
        }

        @Test
        @DisplayName("throws exception when list is empty")
        void throwsExceptionWhenListIsEmpty(){
            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(0));
            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(1));
            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(Integer.MAX_VALUE));
        }
        
        @Test
        @DisplayName("throws exception when index is negative")
        void throwsExceptionWhenIndexIsNegative(){
            getListWithFilledArray();

            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(-1));
            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(-3));
            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(Integer.MIN_VALUE));
        }

        @Test
        @DisplayName("throws exception when given index is not populated")
        void throwsExceptionWhenGivenIndexIsNotPopulated(){
            testList.add(e1);

            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(1));
        }

        @Test
        @DisplayName("throws exception after size adjustment when given index is not populated yet")
        void throwsExceptionAfterSizeAdjustmentWhenGivenIndexIsNotPopulatedYet(){
            CustomArrayList<TestElement> listWithFilledArray = getListWithFilledArray();

            listWithFilledArray.add(e2);

            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(listWithFilledArray, listWithFilledArray.size()));
        }

        @Test
        @DisplayName("throws exception when given index is no longer populated")
        void throwsExceptionWhenGivenIndexIsNoLongerPopulated(){
            testList.add(e1);
            testList.remove(0);

            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(0));
        }

        @Test
        @DisplayName("does not throw exception when given index is populated")
        void doesNotThrowExceptionWhenGivenIndexIsPopulated(){
            testList.add(e1);
            testList.add(null);

            assertDoesNotThrow(() -> invokeAssertIndexInBounds(0));
            assertDoesNotThrow(() -> invokeAssertIndexInBounds(1));
        }

        @Test
        @DisplayName("does not throw exception after size adjustment when given index is populated")
        void doesNotThrowExceptionAfterSizeAdjustmentWhenGivenIndexIsPopulated(){
            CustomArrayList<TestElement> listWithFilledArray = getListWithFilledArray();

            listWithFilledArray.add(e2);

            assertDoesNotThrow(() -> invokeAssertIndexInBounds(
                    listWithFilledArray, listWithFilledArray.size() - 1));
        }
    }

    @Nested
    @DisplayName("get method")
    class GetMethodTest {
        @Test
        @DisplayName("returns element when index is in bounds")
        void returnsElementWhenIndexIsInBounds(){
            testList.add(e1);
            testList.add(null);
            testList.add(e2);

            assertEquals(e1, testList.get(0));
            assertNull(testList.get(1));
            assertEquals(e2, testList.get(2));
        }

        @Test
        @DisplayName("returns element after size adjustment when index is in bounds")
        void returnsElementAfterSizeAdjustmentWhenIndexIsInBounds(){
            TestElement element = new TestElement(12, "inner");
            CustomArrayList<TestElement> listWithFilledArray = getListWithFilledArray();

            listWithFilledArray.add(element);

            assertEquals(element, listWithFilledArray.get(listWithFilledArray.size() - 1));
        }
    }

    @Nested
    @DisplayName("set method")
    class SetMethodTest{
        @Test
        @DisplayName("throws exception when index out of bounds")
        void throwsExceptionWhenIndexOutOfBounds(){
            assertAll(() -> {
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> testList.set(0, e1));
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> testList.set(1, null));
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> testList.set(-1, e2));
            });
        }

        @Test
        @DisplayName("replaces correct element when index in bounds")
        void replacesCorrectElementWhenIndexInBounds(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.set(1, e1);

            assertEquals(e1, testList.get(0));
            assertEquals(e1, testList.get(1));
            assertEquals(e3, testList.get(2));
            assertFalse(testList.contains(e2));
        }

        @Test
        @DisplayName("replaces last element")
        void replacesLastElement(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.set(testList.size() - 1, e1);

            assertEquals(e1, testList.get(0));
            assertEquals(e2, testList.get(1));
            assertEquals(e1, testList.get(2));
            assertFalse(testList.contains(e3));
        }

        @Test
        @DisplayName("replaces first element")
        void replacesFirstElement(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.set(0, e2);

            assertEquals(e2, testList.get(0));
            assertEquals(e2, testList.get(1));
            assertEquals(e3, testList.get(2));
            assertFalse(testList.contains(e1));
        }

        @Test
        @DisplayName("replaces element when new element is null")
        void replacesElementWhenNewElementIsNull(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.set(1, null);

            assertEquals(e1, testList.get(0));
            assertNull(testList.get(1));
            assertEquals(e3, testList.get(2));
            assertFalse(testList.contains(e2));
        }

        @Test
        @DisplayName("does not throw exception when replacing null with null")
        void doesNotThrowExceptionWhenReplacingNullWithNull(){
            testList.add(null);

            assertDoesNotThrow(() -> testList.set(0, null));
            assertEquals(1, testList.size());
            assertNull(testList.get(0));
        }

        @Test
        @DisplayName("does not increase list size")
        void doesNotIncreaseListSize(){
            testList.add(e1);
            testList.add(e2);
            int size = testList.size();

            testList.set(0, e2);

            assertEquals(size, testList.size());
        }
    }

    @Nested
    @DisplayName("remove method")
    class RemoveMethodTest{
        @Test
        @DisplayName("removes correct element when first in list")
        void removesCorrectElementWhenFirstInList(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.remove(0);

            assertEquals(2, testList.size());
            assertEquals(e2, testList.get(0));
            assertEquals(e3, testList.get(1));
        }

        @Test
        @DisplayName("removes correct element when last in list")
        void removesCorrectElementWhenLastInList(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.remove(2);

            assertEquals(2, testList.size());
            assertEquals(e1, testList.get(0));
            assertEquals(e2, testList.get(1));
        }

        @Test
        @DisplayName("removes correct element when in middle of list")
        void removesCorrectElementWhenInMiddleOfList(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.remove(1);

            assertEquals(2, testList.size());
            assertEquals(e1, testList.get(0));
            assertEquals(e3, testList.get(1));
        }

        @Test
        @DisplayName("removes single instance when multiple present")
        void removesSingleInstanceWhenMultiplePresent(){
            testList.add(e1);
            testList.add(e1);
            testList.add(e1);

            testList.remove(0);

            assertEquals(2, testList.size());
            assertEquals(e1, testList.get(0));
            assertEquals(e1, testList.get(1));
        }

        @Test
        @DisplayName("can remove all elements when multiple present")
        void canRemoveAllElementsWhenMultiplePresent(){
            testList.add(e1);
            testList.add(e1);
            testList.add(e1);

            testList.remove(0);
            testList.remove(0);
            testList.remove(0);

            assertEquals(0, testList.size());
            assertTrue(testList.isEmpty());
            assertFalse(testList.contains(e1));
        }
    }

    @Nested
    @DisplayName("size method")
    class SizeMethodTest{
        @Test
        @DisplayName("returns correct size when list is empty")
        void returnsCorrectSizeWhenListIsEmpty(){
            assertEquals(0, testList.size());
        }

        @Test
        @DisplayName("returns correct size when list is not empty")
        void returnsCorrectSizeWhenListIsNotEmpty(){
            testList.add(e1);
            testList.add(e1);
            testList.add(e1);
            assertEquals(3, testList.size());
        }

        @Test
        @DisplayName("returns correct size when element got removed")
        void returnsCorrectSizeWhenElementGotRemoved(){
            int startSize = testList.size();

            testList.add(e1);
            testList.remove(0);

            assertEquals(startSize, testList.size());
        }

        @Test
        @DisplayName("returns correct size when array got adjusted")
        void returnsCorrectSizeWhenArrayGotAdjusted(){
            CustomArrayList<TestElement> listWithFilledArray = getListWithFilledArray();
            int startSize = listWithFilledArray.size();

            listWithFilledArray.add(e1);

            assertEquals(startSize + 1, listWithFilledArray.size());
        }
    }

    @Nested
    @DisplayName("isEmpty method")
    class IsEmptyMethodTest{
        @Test
        @DisplayName("returns true when list is empty")
        void returnsTrueWhenListIsEmpty(){
            assertTrue(testList.isEmpty());
        }

        @Test
        @DisplayName("returns true when last element got removed")
        void returnsTrueWhenLastElementGotRemoved(){
            testList.add(e1);
            testList.remove(0);

            assertTrue(testList.isEmpty());
        }

        @Test
        @DisplayName("returns false when list contains non null element")
        void returnsFalseWhenListContainsNonNullElement(){
            testList.add(e1);

            assertFalse(testList.isEmpty());
        }

        @Test
        @DisplayName("returns false when list contains only null element")
        void returnsFalseWhenListContainsOnlyNullElement(){
            testList.add(null);

            assertFalse(testList.isEmpty());
        }
    }

    @Nested
    @DisplayName("contains method")
    class ContainsMethodTest{
        @Test
        @DisplayName("returns false when list is empty")
        void returnsFalseWhenListIsEmpty(){
            assertFalse(testList.contains(e1));
            assertFalse(testList.contains(e2));
            assertFalse(testList.contains(e3));
        }

        @Test
        @DisplayName("returns false when list contains different elements")
        void returnsFalseWhenListContainsDifferentElements(){
            testList.add(e1);
            testList.add(null);
            testList.add(e3);

            assertFalse(testList.contains(e2));
        }

        @Test
        @DisplayName("returns true when element is present")
        void returnsTrueWhenElementIsPresent(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            assertTrue(testList.contains(e1));
            assertTrue(testList.contains(e2));
            assertTrue(testList.contains(e3));
        }

        @Test
        @DisplayName("returns true when element is null")
        void returnsTrueWhenElementIsNull(){
            testList.add(null);

            assertTrue(testList.contains(null));
        }
    }

    @Nested
    @DisplayName("indexOf method")
    class IndexOfMethodTest{
        @Test
        @DisplayName("returns negative when list is empty")
        void returnsNegativeWhenListIsEmpty(){
            assertEquals(-1, testList.indexOf(e1));
        }

        @Test
        @DisplayName("returns negative when list contains different element")
        void returnsNegativeWhenListContainsDifferentElement(){
            testList.add(e1);

            assertEquals(-1, testList.indexOf(e2));
        }

        @Test
        @DisplayName("returns index when element is null")
        void returnsIndexWhenElementIsNull(){
            testList.add(e1);
            testList.add(null);

            assertEquals(1, testList.indexOf(null));
        }

        @Test
        @DisplayName("returns first index when multiple same elements present")
        void returnsFirstIndexWhenMultipleSameElementsPresent(){
            testList.add(e2);
            testList.add(e1);
            testList.add(e1);
            testList.add(e1);
            testList.add(null);
            testList.add(null);

            assertEquals(1, testList.indexOf(e1));
            assertEquals(4, testList.indexOf(null));
        }

        @Test
        @DisplayName("returns index when element is present")
        void returnsIndexWhenElementIsPresent(){
            testList.add(e1);
            testList.add(e2);

            assertEquals(1, testList.indexOf(e2));
        }
    }

    @Nested
    @DisplayName("toArray method")
    class ToArrayMethodTest{
        @Test
        @DisplayName("returns empty array when list is empty")
        void returnsEmptyArrayWhenListIsEmpty(){
            Object[] objects = testList.toArray();

            assertEquals(0, objects.length);
        }

        @Test
        @DisplayName("returns array with exact number of nulls when only nulls present")
        void returnsArrayWithExactNumberOfNullsWhenOnlyNullsPresent(){
            testList.add(null);
            testList.add(null);
            testList.add(null);

            Object[] objects = testList.toArray();

            assertAll(() -> {
                assertEquals(3, objects.length);
                assertNull(objects[0]);
                assertNull(objects[1]);
                assertNull(objects[2]);
            });
        }

        @Test
        @DisplayName("returns array when some nulls present")
        void returnsArrayWhenSomeNullsPresent(){
            testList.add(e1);
            testList.add(null);
            testList.add(null);

            Object[] objects = testList.toArray();

            assertAll(() -> {
                assertEquals(3, objects.length);
                assertEquals(e1, objects[0]);
                assertNull(objects[1]);
                assertNull(objects[2]);
            });
        }

        @Test
        @DisplayName("returns arrays with correct elements")
        void returnsArraysWithCorrectElements(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            Object[] objects = testList.toArray();

            assertAll(() -> {
                assertEquals(3, objects.length);
                assertEquals(e1, objects[0]);
                assertEquals(e2, objects[1]);
                assertEquals(e3, objects[2]);
            });
        }

        @Test
        @DisplayName("returns correct array when list got reduced")
        void returnsCorrectArrayWhenListGotReduced(){
            testList.add(e1);
            testList.add(e2);
            testList.add(e3);

            testList.remove(1);
            Object[] objects = testList.toArray();

            assertAll(() -> {
                assertEquals(2, objects.length);
                assertEquals(e1, objects[0]);
                assertEquals(e3, objects[1]);
            });
        }
    }
}