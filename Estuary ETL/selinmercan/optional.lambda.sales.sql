
-- Example statement which passes-through source selinmercan/takehome/sales documents without modification.
-- Use a WHERE clause to filter, for example: WHERE $my$column = 1234
SELECT JSON($flow_document);

-- Example statement demonstrating how to SELECT specific locations from documents of selinmercan/takehome/sales.
-- This statement is effectively disabled by its WHERE FALSE clause and does not emit any documents.
--
-- You can rename a location by using the SQL "AS" syntax, for example:
--   SELECT $some$column AS "my_new_column_name;
--
-- You can also filter by using locations in a WHERE clause, for example:
--   SELECT $some$column WHERE $other$column = 1234;
SELECT
    -- Field _meta at /_meta
    $_meta,
    -- Field _meta/before at /_meta/before; Record state immediately before this change was applied.
    $_meta$before,
    -- Field _meta/before/product_id at /_meta/before/product_id; (source type: int4)
    $_meta$before$product_id,
    -- Field _meta/before/quantity at /_meta/before/quantity; (source type: int4)
    $_meta$before$quantity,
    -- Field _meta/before/sale_amount at /_meta/before/sale_amount; (source type: int4)
    $_meta$before$sale_amount,
    -- Field _meta/before/sale_id at /_meta/before/sale_id; (source type: non-nullable int4)
    $_meta$before$sale_id,
    -- Field _meta/flow_truncated at /_meta/flow_truncated; Flow truncation indicator; Indicates whether any of the materialized values for this row have been truncated to make them fit inside the limitations of the destination system.
    $_meta$flow_truncated,
    -- Field _meta/op at /_meta/op; Change operation type: 'c' Create/Insert, 'u' Update, 'd' Delete.
    $_meta$op,
    -- Field _meta/source at /_meta/source
    $_meta$source,
    -- Field _meta/source/loc at /_meta/source/loc; Location of this WAL event as [last Commit.EndLSN; event LSN; current Begin.FinalLSN]. See https://www.postgresql.org/docs/current/protocol-logicalrep-message-formats.html
    $_meta$source$loc,
    -- Field _meta/source/schema at /_meta/source/schema; Database schema (namespace) of the event.
    $_meta$source$schema,
    -- Field _meta/source/snapshot at /_meta/source/snapshot; Snapshot is true if the record was produced from an initial table backfill and unset if produced from the replication log.
    $_meta$source$snapshot,
    -- Field _meta/source/table at /_meta/source/table; Database table of the event.
    $_meta$source$table,
    -- Field _meta/source/ts_ms at /_meta/source/ts_ms; Unix timestamp (in millis) at which this event was recorded by the database.
    $_meta$source$ts_ms,
    -- Field _meta/source/txid at /_meta/source/txid; The 32-bit transaction ID assigned by Postgres to the commit which produced this change.
    $_meta$source$txid,
    -- Field flow_published_at at /_meta/uuid; Flow Publication Time; Flow publication date-time of this document
    $flow_published_at,
    -- Field product_id at /product_id; (source type: int4)
    $product_id,
    -- Field quantity at /quantity; (source type: int4)
    $quantity,
    -- Field sale_amount at /sale_amount; (source type: int4)
    $sale_amount,
    -- Key sale_id at /sale_id; (source type: non-nullable int4)
    $sale_id
-- Disable this statement, so that it emits no documents.
WHERE FALSE;
