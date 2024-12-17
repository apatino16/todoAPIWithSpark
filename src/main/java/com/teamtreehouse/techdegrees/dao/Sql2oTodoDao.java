package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao {

    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Todo todo) throws DaoException {
        // SQL query for inserting a new todo into the database
        String sql = "INSERT INTO todos(name, isCompleted) VALUES (:name, :isCompleted)";

        // Try-with-resources: open a new database connection and automatically closes the connection after execution
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(todo) // streamline the process of binding data to SQL query parameters
                    .executeUpdate()
                    .getKey();
            todo.setId(id); // syncs Java object with database
        } catch (Sql2oException e) {
            throw new DaoException(e, "Problem adding todo");
        }
    }

    // Update a todo
    @Override
    public void updateTodo(int id, String name, boolean isCompleted) {
        String sql = "UPDATE todos SET name = :name, isCompleted = :isCompleted WHERE id = :id";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("isCompleted", isCompleted)
                    .executeUpdate();
        }
    }

    // Delete a todo
    @Override
    public void deleteTodo(int id) {
        String sql = "DELETE FROM todos WHERE id = :id";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    // Find all todos
    @Override
    public List<Todo> findAll() {
        String sql = "SELECT * FROM todos";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Todo.class);
        }
    }

    // Find a todo by ID
    @Override
    public Todo findByTodoId(int id) {
        String sql = "SELECT * FROM todos WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Todo.class);
        }
    }
}
