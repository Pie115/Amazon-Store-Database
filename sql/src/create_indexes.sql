DROP INDEX IF EXISTS idx_users_name;
DROP INDEX IF EXISTS idx_users_type;
DROP INDEX IF EXISTS idx_store_managerID;
DROP INDEX IF EXISTS idx_product_numberOfUnits;
DROP INDEX IF EXISTS idx_orders_customerID;
DROP INDEX IF EXISTS idx_orders_storeID;
DROP INDEX IF EXISTS idx_orders_productName;
DROP INDEX IF EXISTS idx_orders_orderTime;
DROP INDEX IF EXISTS idx_supplyrequests_managerID;
DROP INDEX IF EXISTS idx_supplyrequests_warehouseID;
DROP INDEX IF EXISTS idx_supplyrequests_storeID;
DROP INDEX IF EXISTS idx_productupdates_managerID;

CREATE INDEX idx_users_name ON Users USING BTREE (name);
CREATE INDEX idx_users_type ON Users USING BTREE (type);
CREATE INDEX idx_store_managerID ON Store USING BTREE (managerID);
CREATE INDEX idx_product_numberOfUnits ON Product USING BTREE (numberOfUnits);
CREATE INDEX idx_orders_customerID ON Orders USING BTREE (customerID);
CREATE INDEX idx_orders_storeID ON Orders USING BTREE (storeID);
CREATE INDEX idx_orders_productName ON Orders USING BTREE (productName);
CREATE INDEX idx_orders_orderTime ON Orders USING BTREE (orderTime);
CREATE INDEX idx_supplyrequests_managerID ON ProductSupplyRequests USING BTREE (managerID);
CREATE INDEX idx_supplyrequests_warehouseID ON ProductSupplyRequests USING BTREE (warehouseID);
CREATE INDEX idx_supplyrequests_storeID ON ProductSupplyRequests USING BTREE (storeID);
CREATE INDEX idx_productupdates_managerID ON ProductUpdates USING BTREE (managerID);

