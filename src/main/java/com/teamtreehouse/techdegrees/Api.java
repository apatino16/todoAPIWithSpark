package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exc.ErrorMessage;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class Api {

    public static void main(String[] args) {

        port(4567);
        staticFileLocation("/public");
        Sql2o sql2o = new Sql2o("jdbc:h2:~/todos.db;INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "" );

        // JSON transformation
        Gson gson = new Gson();

        // Initialize DAO
        TodoDao todoDao = new Sql2oTodoDao(sql2o);

        // Default response type is JSON
        before("/api/v1/*", (req, res) -> res.type("application/json"));

        // Setup Routes
        routes(todoDao, gson);
    }

    private static void routes(TodoDao todoDao, Gson gson) {
        // Route that fetches all todos from the database
        get("/api/v1/todos", "application/json", (req, res) -> {
            try {
                return gson.toJson(todoDao.findAll());
            } catch (Exception e) {
                res.status(500); // Internal Server Error
                res.type("application/json");
                return gson.toJson(new ErrorMessage("Failed to fetch todos: " + e.getMessage()));
            }
        }, gson::toJson);

        // Route to add new todo to the database
        post("/api/v1/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.add(todo);
            res.status(201); // Created
            return todo;
        }, gson::toJson); // method reference

        after((req, res) -> res.type("application/json"));

        // Updating existing todo
        put("/api/v1/todos/:id", "application/json", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Todo updatedTodo = gson.fromJson(req.body(), Todo.class);

                // fallback
                Todo existingTodo = todoDao.findByTodoId(id);

                if (existingTodo == null) {
                    res.status(404); // Not Found
                    return gson.toJson("Todo not found");
                }

                // Update todo with new values
                if (updatedTodo.getName() != null) existingTodo.setName(updatedTodo.getName());
                if (updatedTodo.isCompleted() != existingTodo.isCompleted())
                    existingTodo.setCompleted(updatedTodo.isCompleted());

                res.status(200); // ok
                return "Todo updated successfully";
            } catch (Exception e) {
                res.status(500);
                res.type("application/json");
                return gson.toJson(new ErrorMessage("An error occurred: " + e.getMessage()));
            }
        }, gson::toJson);

        // Route to delete a todo from the database
        delete("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Todo todo = todoDao.findByTodoId(id);

            if (todo != null) {
                todoDao.deleteTodo(id);
                res.status(204); // No Content
                return "";
            } else {
                res.status(404); // Not Found
                return gson.toJson("Todo not found");
            }
        });
    }
}
