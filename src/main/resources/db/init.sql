CREATE TABLE IF NOT EXISTS todos (
    id int PRIMARY KEY auto_increment,
    name VARCHAR(255),
    isCompleted BOOLEAN
);