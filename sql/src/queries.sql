SELECT userID, name, storeID
FROM Users u, Store s
WHERE u.latitude < s.latitude AND u.longitude < s.longitude AND u.userID = 101;

SELECT productName, numberOfUnits
FROM Product p, Store s
WHERE s.storeID = 2 AND s.storeID = p.storeID;

SELECT *
FROM Orders 
ORDER BY orderNumber DESC 
LIMIT 5;

SELECT *
FROM ProductUpdates;

Select productName, storeID, numberOfUnits
FROM Product
WHERE storeID = 3 OR storeID = 7 OR storeID = 12 OR storeID = 13
ORDER BY numberOfUnits ASC
LIMIT 5;

SELECT U.name, U.userID,  COUNT(O.customerID)
FROM Orders O, Users U
WHERE O.customerID = U.userID AND storeID = 3
GROUP BY(U.name)
ORDER BY COUNT(O.customerID) DESC
LIMIT 5;

SELECT *
FROM ProductSupplyRequests;
