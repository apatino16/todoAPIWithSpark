package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.*;

public class Sql2oTodoDaoTest {

    private Sql2oTodoDao dao;
    private Connection con;

    @BeforeEach
    public void setUp() {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";

        Sql2o sql2o = new Sql2o(connectionString, null, null);
        dao = new Sql2oTodoDao(sql2o);

        // Keep connection open through entire test so that it doesn't get erased
        con = sql2o.open();
    }

    @AfterEach
    public void tearDown() {
        con.close();
    }

    @Test
    public void addingTodoSetsId() throws Exception {
        Todo todo = new Todo("Test", true);
        int originalTodoId = todo.getId();

        dao.add(todo);

        assertNotEquals(originalTodoId, todo.getId());
    }

    @Test
    public void addedTodosAreReturnedFromFindAll() throws  Exception{
        // Arrange
        Todo todo1 = new Todo("Test1", true);
        Todo todo2 = new Todo("Test2", false);


        // Act
        dao.add(todo1);
        dao.add(todo2);

        // Assert
        assertEquals(2, dao.findAll().size());
    }

    @Test
    public void noTodosReturnsEmptyList() {
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void existingTodosCanBeFoundById() {

    }

}