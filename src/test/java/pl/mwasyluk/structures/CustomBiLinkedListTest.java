package pl.mwasyluk.structures;

import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CustomBiLinkedListTest {
    CustomBiLinkedList<TestElement> list;
    TestElement e1 = new TestElement(6, "Test1");
    TestElement e2 = new TestElement(2, "Test2");
    TestElement e3 = new TestElement(4, "Test3");

    @BeforeEach
    void setUp() {
        list = new CustomBiLinkedList<>();
    }

    @Nested
    @DisplayName("assertIndexInBounds method")
    class AssertIndexInBoundsMethodTest{
        Method assertIndexInBoundsMethod;

        public AssertIndexInBoundsMethodTest() throws NoSuchMethodException {
            this.assertIndexInBoundsMethod = CustomBiLinkedList.class.getDeclaredMethod("assertIndexInBounds", int.class);
            this.assertIndexInBoundsMethod.setAccessible(true);
        }

        void invokeAssertIndexInBounds(int i) throws IllegalAccessException {
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
            list.add(e1);
            list.add(e2);
            list.add(e3);

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
            list.add(e1);

            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(1));
        }

        @Test
        @DisplayName("throws exception when given index is no longer populated")
        void throwsExceptionWhenGivenIndexIsNoLongerPopulated(){
            list.add(e1);
            list.remove(0);

            assertThrowsExactly(IndexOutOfBoundsException.class,
                    () -> invokeAssertIndexInBounds(0));
        }

        @Test
        @DisplayName("does not throw exception when given index is populated")
        void doesNotThrowExceptionWhenGivenIndexIsPopulated(){
            list.add(e1);
            list.add(null);

            assertDoesNotThrow(() -> invokeAssertIndexInBounds(0));
            assertDoesNotThrow(() -> invokeAssertIndexInBounds(1));
        }
    }

    @Nested
    @DisplayName("getNode method")
    class GetNodeMethodTest{
        Method getNodeMethod;

        public GetNodeMethodTest() throws NoSuchMethodException {
            this.getNodeMethod = CustomBiLinkedList.class.getDeclaredMethod("assertIndexInBounds", int.class);
            this.getNodeMethod.setAccessible(true);
        }

        void invokeGetNode(int i) throws IllegalAccessException {
            try {
                getNodeMethod.invoke(list, i);
            } catch (InvocationTargetException e) {
                if (e.getCause() != null &&
                        e.getCause().getClass() == IndexOutOfBoundsException.class){
                    throw new IndexOutOfBoundsException();
                }
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("throws exception when list is empty")
        void throwsExceptionWhenListIsEmpty(){
            assertAll(() -> {
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> invokeGetNode(0));
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> invokeGetNode(1));
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> invokeGetNode(14));
            });
        }

        @Test
        @DisplayName("throws exception when index is negative")
        void throwsExceptionWhenIndexIsNegative(){
            list.add(e1);
            list.add(e2);
            list.add(e3);

            assertAll(() -> {
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> invokeGetNode(-1));
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> invokeGetNode(-2));
            });
        }

        @Test
        @DisplayName("throws exception when index is not populated")
        void throwsExceptionWhenIndexIsNotPopulated(){
            list.add(e1);
            list.add(e2);
            list.add(e3);

            assertAll(() -> {
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> invokeGetNode(3));
                assertThrowsExactly(IndexOutOfBoundsException.class,
                        () -> invokeGetNode(4));
            });
        }

        @Test
        @DisplayName("does not throw exception when index in bounds")
        void doesNotThrowExceptionWhenIndexInBounds(){
            list.add(e1);
            list.add(e2);
            list.add(e3);

            assertAll(() -> {
                assertDoesNotThrow(() -> invokeGetNode(0));
                assertDoesNotThrow(() -> invokeGetNode(1));
                assertDoesNotThrow(() -> invokeGetNode(2));
            });
        }
    }

    @Nested
    @DisplayName("add method")
    class AddMethodTest {
        @Test
        @DisplayName("adds element when list is empty")
        void addsElementToEmptyList(){
            list.add(e1);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertEquals(1, list.size());
            });
        }

        @Test
        @DisplayName("adds elements multiple times")
        void addsElementsMultipleTimes(){
            list.add(e1);
            list.add(e2);
            list.add(e3);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertEquals(e2, list.get(1));
                assertEquals(e3, list.get(2));
                assertEquals(3, list.size());
            });
        }

        @Test
        @DisplayName("adds null element")
        void addsNullElement(){
            list.add(e1);
            list.add(null);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertNull(list.get(1));
                assertEquals(2, list.size());
            });
        }

        @Test
        @DisplayName("adds null element multiple times")
        void addsNullElementMultipleTimes(){
            list.add(null);
            list.add(null);
            list.add(null);

            assertAll(() -> {
                assertNull(list.get(0));
                assertNull(list.get(1));
                assertNull(list.get(2));
                assertEquals(3, list.size());
            });
        }

        @Test
        @DisplayName("adds element when another got removed")
        void addsElementWhenOtherGotRemoved(){
            list.add(e1);
            list.add(e2);
            list.remove(1);

            list.add(e3);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertEquals(e3, list.get(1));
                assertEquals(2, list.size());
            });
        }

        @Test
        @DisplayName("increases list size")
        void increasesListSize(){
            int size0 = list.size();
            list.add(e1);
            int size1 = list.size();
            list.add(e2);
            int size2 = list.size();
            list.add(e3);

            assertAll(() -> {
                assertEquals(0, size0);
                assertEquals(1, size1);
                assertEquals(2, size2);
                assertEquals(3, list.size());
            });
        }
    }

    @Nested
    @DisplayName("get method")
    class GetMethodTest{
        @Test
        @DisplayName("returns correct element")
        void returnsCorrectElement(){
            list.add(e1);
            list.add(e2);
            list.add(e3);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertEquals(e2, list.get(1));
                assertEquals(e3, list.get(2));
            });
        }

        @Test
        @DisplayName("returns correct element when null present")
        void returnsCorrectElementWhenNullPresent(){
            list.add(e1);
            list.add(null);
            list.add(e3);
            list.add(null);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertNull(list.get(1));
                assertEquals(e3, list.get(2));
                assertNull(list.get(3));
            });
        }

        @Test
        @DisplayName("returns correct element when only null present")
        void returnsCorrectElementWhenOnlyNullPresent(){
            list.add(null);
            list.add(null);
            list.add(null);

            assertAll(() -> {
                assertNull(list.get(0));
                assertNull(list.get(1));
                assertNull(list.get(2));
            });
        }
    }

    @Nested
    @DisplayName("set method")
    class SetMethodTest{
        @Test
        @DisplayName("can replace every element")
        void canReplaceEveryElement(){
            list.add(e1);
            list.add(e1);
            list.add(e1);

            list.set(0, e2);
            list.set(1, e2);
            list.set(2, e2);

            assertAll(() -> {
                assertEquals(e2, list.get(0));
                assertEquals(e2, list.get(1));
                assertEquals(e2, list.get(2));
                assertFalse(list.contains(e1));
            });
        }

        @Test
        @DisplayName("replaces correct element when in the middle")
        void replacesCorrectElementWhenInTheMiddle(){
            list.add(e1);
            list.add(e1);
            list.add(e1);

            list.set(1, e2);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertEquals(e2, list.get(1));
                assertEquals(e1, list.get(2));
            });
        }

        @Test
        @DisplayName("replaces correct element when at edges")
        void replacesCorrectElementWhenAtEdges(){
            list.add(e1);
            list.add(e1);
            list.add(e1);
            list.add(e1);
            list.add(e1);

            list.set(0, e2);
            list.set(4, e3);

            assertAll(() -> {
                assertEquals(e2, list.get(0));
                assertEquals(e1, list.get(1));
                assertEquals(e1, list.get(2));
                assertEquals(e1, list.get(3));
                assertEquals(e3, list.get(4));
            });
        }

        @Test
        @DisplayName("replaces null element")
        void replacesNullElement(){
            list.add(e2);
            list.add(null);
            list.add(e2);

            list.set(1, e1);

            assertAll(() -> {
                assertEquals(e2, list.get(0));
                assertEquals(e2, list.get(2));
                assertFalse(list.contains(null));
            });
        }

        @Test
        @DisplayName("replaces element when given is null")
        void replacesElementWhenGivenIsNull(){
            list.add(e1);

            list.set(0, null);

            assertAll(() -> {
                assertNull(list.get(0));
                assertFalse(list.contains(e1));
            });
        }

        @Test
        @DisplayName("does not change size")
        void doesNotChangeSize(){
            list.add(e1);
            list.add(e1);
            list.add(e1);
            int expectedSize = list.size();

            list.set(0, null);
            list.set(1, null);
            list.set(2, null);

            assertEquals(expectedSize, list.size());
        }

        @Test
        @DisplayName("replaces element when it is between nulls")
        void replacesElementWhenItIsBetweenNulls(){
            list.add(null);
            list.add(e1);
            list.add(null);

            list.set(1, e2);


            assertAll(() -> {
                assertNull(list.get(0));
                assertEquals(e2, list.get(1));
                assertNull(list.get(2));
            });
        }
    }

    @Nested
    @DisplayName("remove method")
    class RemoveMethodTest{
        @Test
        @DisplayName("removes element when one is present")
        void removesElementWhenOneIsPresent(){
            list.add(e1);

            list.remove(0);

            assertAll(() -> {
                assertFalse(list.contains(e1));
                assertEquals(0, list.size());
            });
        }
        
        @Test
        @DisplayName("does not block further addition")
        void doesNotBlockFurtherAddition(){
            list.add(e1);
            list.add(e1);

            list.remove(0);
            list.remove(0);
            list.add(e2);
            list.add(e2);

            assertAll(() -> {
                assertFalse(list.contains(e1));
                assertEquals(e2, list.get(0));
                assertEquals(e2, list.get(1));
                assertEquals(2, list.size());
            });
        }

        @Test
        @DisplayName("correctly reduces list size")
        void correctlyReducesListSize(){
            list.add(e1);
            list.add(e2);
            list.add(e3);
            int size = list.size();

            list.remove(0);
            list.remove(0);
            list.remove(0);

            assertAll(() -> {
                assertEquals(3, size);
                assertEquals(0, list.size());

            });
        }

        @Test
        @DisplayName("removes null element")
        void removesNullElement(){
            list.add(e1);
            list.add(null);
            list.add(e3);

            list.remove(1);

            assertAll(() -> {
                assertEquals(e1, list.get(0));
                assertEquals(e3, list.get(1));
                assertFalse(list.contains(null));
            });
        }

        @Test
        @DisplayName("correctly removes first element")
        void correctlyRemovesFirstElement(){
            list.add(e1);
            list.add(e2);
            list.add(e2);
            list.add(e2);

            list.remove(0);

            assertAll(() -> {
                assertEquals(e2, list.get(0));
                assertEquals(e2, list.get(1));
                assertFalse(list.contains(e1));
            });
        }

        @Test
        @DisplayName("correctly removes last element")
        void correctlyRemovesLastElement(){
            list.add(e2);
            list.add(e2);
            list.add(e2);
            list.add(e1);

            list.remove(3);

            assertAll(() -> {
                assertEquals(3, list.size());
                assertEquals(e2, list.get(2));
                assertFalse(list.contains(e1));
            });
        }
    }

    @Nested
    @DisplayName("size method")
    class SizeMethodTest{
        @Test
        @DisplayName("returns correct size when list is empty")
        void returnsCorrectSizeWhenListIsEmpty(){
            assertEquals(0, list.size());
        }

        @Test
        @DisplayName("returns correct size when list is not empty")
        void returnsCorrectSizeWhenListIsNotEmpty(){
            list.add(e1);
            list.add(e1);
            list.add(e1);
            assertEquals(3, list.size());
        }

        @Test
        @DisplayName("returns correct size when element got removed")
        void returnsCorrectSizeWhenElementGotRemoved(){
            int startSize = list.size();

            list.add(e1);
            list.remove(0);

            assertEquals(startSize, list.size());
        }
    }

    @Nested
    @DisplayName("isEmpty method")
    class IsEmptyMethodTest{
        @Test
        @DisplayName("returns true when list is empty")
        void returnsTrueWhenListIsEmpty(){
            assertTrue(list.isEmpty());
        }

        @Test
        @DisplayName("returns true when last element got removed")
        void returnsTrueWhenLastElementGotRemoved(){
            list.add(e1);
            list.remove(0);

            assertTrue(list.isEmpty());
        }

        @Test
        @DisplayName("returns false when list contains non null element")
        void returnsFalseWhenListContainsNonNullElement(){
            list.add(e1);

            assertFalse(list.isEmpty());
        }

        @Test
        @DisplayName("returns false when list contains only null element")
        void returnsFalseWhenListContainsOnlyNullElement(){
            list.add(null);

            assertFalse(list.isEmpty());
        }
    }

    @Nested
    @DisplayName("contains method")
    class ContainsMethodTest{
        @Test
        @DisplayName("returns false when list is empty")
        void returnsFalseWhenListIsEmpty(){
            assertFalse(list.contains(e1));
            assertFalse(list.contains(e2));
            assertFalse(list.contains(e3));
        }

        @Test
        @DisplayName("returns false when list contains different elements")
        void returnsFalseWhenListContainsDifferentElements(){
            list.add(e1);
            list.add(null);
            list.add(e3);

            assertFalse(list.contains(e2));
        }

        @Test
        @DisplayName("returns true when element is present")
        void returnsTrueWhenElementIsPresent(){
            list.add(e1);
            list.add(e2);
            list.add(e3);

            assertTrue(list.contains(e1));
            assertTrue(list.contains(e2));
            assertTrue(list.contains(e3));
        }

        @Test
        @DisplayName("returns true when element is null")
        void returnsTrueWhenElementIsNull(){
            list.add(null);

            assertTrue(list.contains(null));
        }
    }

    @Nested
    @DisplayName("indexOf method")
    class IndexOfMethodTest{
        @Test
        @DisplayName("returns negative when list is empty")
        void returnsNegativeWhenListIsEmpty(){
            assertEquals(-1, list.indexOf(e1));
        }

        @Test
        @DisplayName("returns negative when list contains different element")
        void returnsNegativeWhenListContainsDifferentElement(){
            list.add(e1);

            assertEquals(-1, list.indexOf(e2));
        }

        @Test
        @DisplayName("returns index when element is null")
        void returnsIndexWhenElementIsNull(){
            list.add(e1);
            list.add(null);

            assertEquals(1, list.indexOf(null));
        }

        @Test
        @DisplayName("returns first index when multiple same elements present")
        void returnsFirstIndexWhenMultipleSameElementsPresent(){
            list.add(e2);
            list.add(e1);
            list.add(e1);
            list.add(e1);
            list.add(null);
            list.add(null);

            assertEquals(1, list.indexOf(e1));
            assertEquals(4, list.indexOf(null));
        }

        @Test
        @DisplayName("returns index when element is present")
        void returnsIndexWhenElementIsPresent(){
            list.add(e1);
            list.add(e2);

            assertEquals(1, list.indexOf(e2));
        }

        @Test
        @DisplayName("returns index when element is after null")
        void returnsIndexWhenElementIsAfterNull(){
            list.add(null);
            list.add(e1);

            assertEquals(1, list.indexOf(e1));
        }
    }

    @Nested
    @DisplayName("toArray method")
    class ToArrayMethodTest{
        @Test
        @DisplayName("returns empty array when list is empty")
        void returnsEmptyArrayWhenListIsEmpty(){
            Object[] objects = list.toArray();

            assertEquals(0, objects.length);
        }

        @Test
        @DisplayName("returns array with exact number of nulls when only nulls present")
        void returnsArrayWithExactNumberOfNullsWhenOnlyNullsPresent(){
            list.add(null);
            list.add(null);
            list.add(null);

            Object[] objects = list.toArray();

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
            list.add(e1);
            list.add(null);
            list.add(null);

            Object[] objects = list.toArray();

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
            list.add(e1);
            list.add(e2);
            list.add(e3);

            Object[] objects = list.toArray();

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
            list.add(e1);
            list.add(e2);
            list.add(e3);

            list.remove(1);
            Object[] objects = list.toArray();

            assertAll(() -> {
                assertEquals(2, objects.length);
                assertEquals(e1, objects[0]);
                assertEquals(e3, objects[1]);
            });
        }
    }
}