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
        String datasource = "jdbc:h2:~/todos.db";
        if (args.length > 0) {
            if (args.length != 2 ) {
                System.out.println("java API <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        }

        port(4567);
        staticFileLocation("/public");
        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource)
                ,"", "" );

        // Initialize DAO
        TodoDao todoDao = new Sql2oTodoDao(sql2o);

        // JSON transformation
        Gson gson = new Gson();

        // Default response type is JSON
        before("/api/v1/*", (req, res) -> res.type("application/json"));

        // Setup Routes
        routes(todoDao, gson);

    }

    private static void routes(TodoDao todoDao, Gson gson) {
        // Route that fetches all to-dos from the database
        get("/api/v1/todos", "application/json", (req, res) -> {
            try {
                return todoDao.findAll();
            } catch (Exception e) {
                res.status(500); // Internal Server Error
                res.type("application/json");
                return gson.toJson(new ErrorMessage("Failed to fetch to-dos: " + e.getMessage()));
            }
        }, gson::toJson);

        // Route to add new to-do to the database
        post("/api/v1/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.add(todo);
            res.status(201); // Created
            return todo;
        }, gson::toJson);

        after((req, res) -> res.type("application/json"));

        // Updating existing to-do
        put("/api/v1/todos/:id", "application/json", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Todo updatedTodo = gson.fromJson(req.body(), Todo.class);

                // fallback
                Todo existingTodo = todoDao.findByTodoId(id);

                if (existingTodo == null) {
                    res.status(404); // Not Found
                    return gson.toJson("To-do not found");
                }

                // Update to-do with new values
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

        // Route to delete a to-do from the database
        delete("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Todo todo = todoDao.findByTodoId(id);

            if (todo != null) {
                todoDao.deleteTodo(id);
                res.status(204); // No Content
                return "";
            } else {
                res.status(404); // Not Found
                return gson.toJson("To-do not found");
            }
        });
    }
}
