CREATE TABLE reservations (
    id UUID PRIMARY KEY UNIQUE NOT NULL,
    reserved_by VARCHAR(30),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);