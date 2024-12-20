package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class Api {

    public static void main(String[] args) {
        Sql2o sql2o = new Sql2o("jdbc:h2:~/todo.db;INIT=RUNSCRIPT from 'classpath:db/init.sql");
        staticFileLocation("/public");

        // JSON transformation
        Gson gson = new Gson();

        // Default response type is JSON
        before("/api/v1/*", (req, res) -> res.type("application/json"));

        // Initialize DAO
        TodoDao todoDao = new Sql2oTodoDao(sql2o);

        // Setup Routes
        routes(todoDao, gson);
    }

    private static void routes(TodoDao todoDao, Gson gson) {
        // Route that fetches all todos from the database
        get("/api/v1/todos", "application/json", (req, res) -> todoDao.findAll(), gson::toJson);

        // Route to add new todo to the database
        post("/api/v1/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.add(todo);
            res.status(201); // Created
            return todo;
        }, gson::toJson); // method reference

        after((req, res) -> res.type("application.json"));

        // Updating existing todo
        put("/api/v1/todos/:id","application/json",(req, res) -> {

                int id = Integer.parseInt(req.params(":id"));
                Todo updatedTodo = gson.fromJson(req.body(), Todo.class);

                // fallback
                Todo existingTodo = todoDao.findByTodoId(id);

                if (existingTodo == null) {
                    res.status(400); // Not Found
                    return "Todo not found";
                }

                // Update todo with new values
                if (updatedTodo.getName() != null) existingTodo.setName(updatedTodo.getName());
                if (updatedTodo.isCompleted() != existingTodo.isCompleted()) existingTodo.setCompleted(updatedTodo.isCompleted());

                res.status(200); // ok
                return "Todo updated successfully";
            }, gson::toJson);
        }
}
