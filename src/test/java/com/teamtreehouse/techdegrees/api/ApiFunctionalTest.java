package com.teamtreehouse.techdegrees.api;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
public class ApiFunctionalTest {

    private Connection con;

    @BeforeEach
    public void setUp() {
        // Setup the database with H2 in memory and create tables
        String connectionString = "jdbc:h2:mem:testing;DB_CLOSE_DELAY=-1;";

        Sql2o sql2o = new Sql2o(connectionString, null, null);

        // Keep connection open through entire test so that it doesn't get erased
        con = sql2o.beginTransaction();

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS todos (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), isCompleted BOOLEAN);";

        con.createQuery(sqlCreateTable).executeUpdate();
    }

    @AfterEach
    public void tearDown() {
        // Close the connection after each test
        if (con != null) {
            con.close();
        }
    }

    // Functional test that proves that deletion happens and status code is returned
    @Test
    void testDeleteTodo() throws Exception {

        // Delete the todo
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

    // Functional test that proves that creation happens and status code is returned
    @Test
    void testAddTodo() throws Exception {
        // URL for the POST request
        HttpURLConnection connection = getHttpURLConnection();

        // Assert the response code is 201 (Created)
        assertEquals(201, connection.getResponseCode(), "HTTP response code should be 201 for successful creation");

        // Read the response from the input stream
        Gson gson = new Gson();
        Todo responseTodo;
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            responseTodo = gson.fromJson(response.toString(), Todo.class);
        }

        // Verify the content of the response
        assertEquals("New Todo", responseTodo.getName(), "The name of the returned todo should match the input.");
        assertFalse(responseTodo.isCompleted(), "The completion status of the returned todo should be false.");
    }

    // Helper Function
    private static HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("http://localhost:4567/api/v1/todos");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // create a new todo
        String jsonInputString = "{\"name\": \"New Todo\", \"isCompleted\": false}";

        // Send Json as POST request
        try (OutputStream os = connection.getOutputStream()){
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
}
