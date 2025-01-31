
-- Use migrations to create or alter tables that your derivation will use.
-- Each migration is run only once, and new migrations will be applied as needed.
--
-- For example, create the join table below, and then use it across multiple lambdas:
--
-- A first lambda that updates indexed state:
--
--   INSERT INTO my_join_table (id, joined_value) VALUES ($id, $my::value)
--     ON CONFLICT REPLACE;
--
-- A second lambda that reads out and joins over the indexed state:
--
--    SELECT $id, $other$value, j.joined_value FROM my_join_table WHERE id = $id;

CREATE TABLE my_join_table (
    -- A common ID that's joined over.
    id           INTEGER PRIMARY KEY NOT NULL,
    -- A value that's updated by one lambda, and read by another.
    joined_value TEXT NOT NULL
);

