package com.teamtreehouse.techdegrees.api;

import com.teamtreehouse.techdegrees.model.Todo;
import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
public class ApiFunctionalTest {

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
    void testDeleteTodo() throws Exception {
        URL url = new URL("http://localhost:4567/api/v1/todos/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(204, responseCode, "HTTP response code should be 204 for successful deletion.");

        // Verify that the todo cannot be retrieved anymore
        connection = (HttpURLConnection) new URL("http://localhost:4567/api/v1/todos/1").openConnection();
        connection.setRequestMethod("GET");
        assertEquals(404, connection.getResponseCode(), "HTTP response code should be 404 after the todo is deleted.");
    }

}
