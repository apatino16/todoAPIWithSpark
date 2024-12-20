package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {
    void add(Todo todo) throws DaoException;
    void updateTodo(int id, String name, boolean isCompleted) throws Exception;
    void deleteTodo(int id);

    List<Todo> findAll();
    Todo findByTodoId(int id);
}
