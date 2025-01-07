package com.teamtreehouse.techdegrees.api;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.Api;

import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import com.teamtreehouse.techdegrees.testing.ApiClient;
import com.teamtreehouse.techdegrees.testing.ApiResponse;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ApiFunctionalTest {

    public static final String PORT = "4567";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private Connection conn;
    private ApiClient client;
    private Gson gson;
    private Sql2oTodoDao todoDao;

    @BeforeAll
    public static void startServer() throws Exception {
        String[] args = {PORT, TEST_DATASOURCE};
        Api.main(args);
    }

    @AfterAll
    public static void stopServer() throws Exception {
        Spark.stop();
    }

    @BeforeEach
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        conn = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
        todoDao = new Sql2oTodoDao(sql2o);
    }

    @AfterEach
    public void tearDown() throws Exception {
        conn.close();
    }

    // Adding a To-do
    @Test
    void addingTodoReturnsCreatedStatus() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test");
        values.put("isCompleted", false);

        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));

        assertEquals(201, res.getStatus());
    }

    @Test
    public void todoCanBeSuccessfullyDeleted () throws Exception{
        Todo todo = new Todo("test", false);
        todoDao.add(todo);
        int id = todo.getId();

        ApiResponse res = client.request("DELETE", "/api/v1/todos/" + todo.getId());

        assertEquals(204, res.getStatus());
        assertNull(todoDao.findByTodoId(id));
    }

    @Test
    public void deletingToDoReturnsNoContentStatus() throws Exception{
        Todo todo = new Todo("test", false);
        todoDao.add(todo);

        ApiResponse res = client.request("DELETE", "/api/v1/todos/" + todo.getId());

        assertEquals(204, res.getStatus());
    }

    @Test
    public void updatingTodoReturnsSuccessfulStatus() throws Exception {
        Todo todo = new Todo("test", false);
        todoDao.add(todo);
        Map<String, Object> values = new HashMap<>();
        values.put("name", "change");
        values.put("isCompleted", true);

        ApiResponse res = client.request("PUT", "/api/v1/todos/" + todo.getId(), gson.toJson(values));

        assertEquals(200, res.getStatus());
    }

    @Test
    public void missingToDoReturns404NotFoundStatus() throws Exception {
        ApiResponse res = client.request("GET", "/api/v1/todos/50");

        assertEquals(404, res.getStatus());
    }
}



