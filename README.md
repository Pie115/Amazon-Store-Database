# Amazon Store Database 

## Overview 
This project involved building a Java-based user interface and a SQL database structure. Here, I connected to a PostgreSQL database and ran SQL queries, as well as load supplied data records into my database. The application allows functions like user registration, login/logout, product browsing and ordering, and product information updating in order to serve non-SQL users, notably customers and managers of the Amazon Store.

## ER Diagram
<img width="1065" alt="ER Diagram" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/209b5d05-fadc-4d0f-91d1-db336c46de40">

## Functionality 
### Users 

• New User Registration: I implemented a feature allowing new users to create an account via the interface by supplying the required information. 

• User Login/Logout: Users could log in with their credentials, initiating a session maintained until they logged out, supported by an example in the Java source code. 

• Browse Stores: This functionality enabled users to view stores within a 30-mile radius of their location, utilizing a simplified method for distance calculation we developed for ease of use. 

• Browse Products: Users could enter a storeID to list available products in a store, including details like product name, available units, and unit price. 

• Order Products: I enabled users to order products from stores within a 30-mile radius, requiring inputs like storeID, productName, and number of units. The system then updated the Orders table and the relevant Product tables accordingly. 

• Update Product Information (For Managers): Managers were granted the ability to update product details for their stores, with changes reflected in the Product and ProductUpdates tables. They could also access the last five updates made in their stores. 

• Admin Capabilities: Admin users were provided with comprehensive access to view and modify all user and product information within the database.
Orders 

• Browse Orders List: Customers could view their five most recent orders, including specific details like storeID, product name, units ordered, and the order date, ensuring privacy from other customers' orders. 

• Manager Access: Managers had access to all orders within their stores, including order IDs, customer names, and detailed order information. 

• Popular Products and Customers: Managers could identify the top five most popular products and the top five customers based on the volume of orders, enhancing targeted management strategies. 

### ProductRequests 

• Put Supply Request: Managers could request additional supplies for any product in their stores by specifying details like storeID, productName, units needed, and the supplying warehouseID. Assumptions were made that warehouses could fulfill these requests, with appropriate updates made to the Product and ProductRequests tables.

## Screenshots of Example Usage 

### Creating User 
<img width="1065" alt="ER Diagram" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/347a70e4-3476-46b8-9574-e4746b193386">

### Logging in as Customer 
<img width="1065" alt="Login" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/2f70301a-557f-461b-8911-3df0e8ba376a">

### View Stores 
<img width="1065" alt="View Stores" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/2a778b74-2534-47ee-966c-8a7a9563327c">

### View Products 
<img width="1065" alt="View Products" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/c0117ce0-c3b1-422f-b215-d92369c3778c">

### Placing Order 
<img width="1065" alt="Place Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/c60e502e-39b1-4651-b47f-7673d9c4749d">

### View Recent Orders 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/7cc1aac0-a4bd-4093-9631-ff53badf0801">

### Logging Out and Logging in as Manager 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/2be4a038-0779-4f09-9fc2-9c962861d133"> 

### View Recent Orders as Manager 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/01f9327f-767b-4d5e-a91b-8addddbe3e8c"> 

### Update Product as Manager 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/c7229b34-f9e8-43a2-82d7-72542fb96635"> 

### View Recent Product Updates 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/dc959c33-1b85-4d77-ae6c-8295bcdf969c"> 

### View Popular Products 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/6603b714-d640-4f9d-afde-8e60ffaa9717"> 

### Place Supply Request 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/e6ebb211-b229-4e8a-9462-a32603093106"> 

### Logging in as Admin 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/414fc17b-7fac-41c5-8790-44a6d84bffd2"> 

### Admin Options 
<img width="1065" alt="View Recent Orders" src="https://github.com/Pie115/Amazon-Store-Database/assets/6378028/5e693371-e55e-43ec-a822-aebe1a838e1b"> 



