# Amazon Store Database 

## Overview 
This project involved building a Java-based user interface and a SQL database structure. Here, I connected to a PostgreSQL database and ran SQL queries, as well as load supplied data records into my database. The application allows functions like user registration, login/logout, product browsing and ordering, and product information updating in order to serve non-SQL users, notably customers and managers of the Amazon Store.

## ER Diagram
<img width="1065" alt="ER Diagram" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/209b5d05-fadc-4d0f-91d1-db336c46de40">

## Functionality 
### Users 

New User Registration: I implemented a feature allowing new users to create an account via the interface by supplying the required information. 

User Login/Logout: Users could log in with their credentials, initiating a session maintained until they logged out, supported by an example in the Java source code. 

Browse Stores: This functionality enabled users to view stores within a 30-mile radius of their location, utilizing a simplified method for distance calculation we developed for ease of use. 

Browse Products: Users could enter a storeID to list available products in a store, including details like product name, available units, and unit price. 

Order Products: I enabled users to order products from stores within a 30-mile radius, requiring inputs like storeID, productName, and number of units. The system then updated the Orders table and the relevant Product tables accordingly. 

Update Product Information (For Managers): Managers were granted the ability to update product details for their stores, with changes reflected in the Product and ProductUpdates tables. They could also access the last five updates made in their stores. 

Admin Capabilities: Admin users were provided with comprehensive access to view and modify all user and product information within the database.
Orders 

Browse Orders List: Customers could view their five most recent orders, including specific details like storeID, product name, units ordered, and the order date, ensuring privacy from other customers' orders. 

Manager Access: Managers had access to all orders within their stores, including order IDs, customer names, and detailed order information. 

Popular Products and Customers: Managers could identify the top five most popular products and the top five customers based on the volume of orders, enhancing targeted management strategies. 

### ProductRequests 

Put Supply Request: Managers could request additional supplies for any product in their stores by specifying details like storeID, productName, units needed, and the supplying warehouseID. Assumptions were made that warehouses could fulfill these requests, with appropriate updates made to the Product and ProductRequests tables.


## Screenshots of Example Usage 

### Creating New User
