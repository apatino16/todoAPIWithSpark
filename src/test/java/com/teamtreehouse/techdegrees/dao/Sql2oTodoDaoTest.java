package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
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
    public void setUp() throws Exception {
        // Setup the database with H2 in memory and create tables
        String connectionString = "jdbc:h2:mem:testing;DB_CLOSE_DELAY=-1;";

        Sql2o sql2o = new Sql2o(connectionString, null, null);
        dao = new Sql2oTodoDao(sql2o);

        // Keep connection open through entire test so that it doesn't get erased
        con = sql2o.beginTransaction();

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS todos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), isCompleted BOOLEAN);";

        con.createQuery(sqlCreateTable).executeUpdate();
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Close the connection after each test
        if (con != null) {
            con.close();
        }
    }

    @Test
    public void addingTodoSetsId() throws DaoException {
        Todo todo = new Todo("Do Homework", false);
        try {
            dao.add(todo);
            System.out.println("Added todo with ID: " + todo.getId());

            assertNotEquals(0, todo.getId(), "ID should be set by database");
        } catch (DaoException e) {
            fail("Should not throw exception during add: " + e.getMessage());
        }
    }
/*

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

 */



}