package com.teamtreehouse.techdegrees.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    // Testing Getters and Setters
    @Test
    void getIdShouldReturnCorrectId() {
        Todo todo = new Todo("Test", true);

        todo.setId(1);

        assertEquals(1, todo.getId(), "getId should return the correct ID.");
    }

    @Test
    void setIdShouldSetIdCorrectly() {
        Todo todo = new Todo("Test", true);

        todo.setId(3);

        assertEquals(3, todo.getId(), "setId should set ID correctly.");
    }

    @Test
    void getNameShouldReturnCorrectName() {
        Todo todo = new Todo("Test", true);

        assertEquals("Test", todo.getName(), "getName should return the correct name.");
    }

    @Test
    void setNameShouldSetNameCorrectly() {
        Todo todo = new Todo("Test", true);

        todo.setName("New Name");

        assertEquals("New Name", todo.getName(), "setName should set the name correctly.");
    }

    @Test
    void isCompletedShouldReturnCorrectCompletionStatus() {
        Todo todo = new Todo("Test", true);

        assertTrue(todo.isCompleted(), "isCompleted should return true when the todo is marked as completed.");
    }

    @Test
    void setCompletedShouldSetCompletionStatusCorrectly() {
        Todo todo = new Todo("Test", true);

        todo.setCompleted(false);

        assertFalse(todo.isCompleted(), "setCompleted should set the completion status correctly.");
    }

    // Test that Todo model correctly implements equality checks
    @Test
    void testEqualsSameAttributesShouldReturnTrue() {
        Todo todo1 = new Todo("Test", true);
        todo1.setId(1);

        Todo todo2 = new Todo("Test", true);
        todo2.setId(1);

        assertEquals(todo1, todo2, "Two todos with the same id, name, and completion status should be equal.");

    }

    @Test
    void testEqualsShouldReturnFalseForDifferentAttributes() {
        Todo todo1 = new Todo("Test", true);
        todo1.setId(1);

        Todo todo2 = new Todo("Test", false);
        todo2.setId(2);

        assertNotEquals(todo1, todo2, "Two todos with different id, name, and completion status should return false.");

    }

    // Test hash code generation
    @Test
    void testHashCode() {
        Todo todo1 = new Todo("Test", true);
        todo1.setId(1);

        Todo todo2 = new Todo("Test", true);
        todo2.setId(1);

        assertEquals(todo1.hashCode(), todo2.hashCode(), "Two todos with the same attributes should have the same hash code.");
    }
}