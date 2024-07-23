CREATE TABLE tasks(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    task_priority VARCHAR(6) NOT NULL,
    task_status VARCHAR(7) NOT NULL,
    end_date TIMESTAMP,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);