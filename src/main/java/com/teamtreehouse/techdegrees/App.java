package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
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

    private static void routes(TodoDao todoDao, Gson gson){
        
    }

}
