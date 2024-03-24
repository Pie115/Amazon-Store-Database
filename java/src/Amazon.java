import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */



public class Amazon {
   // reference to physical database connection.
   private Connection _connection = null;
   public static String userID;
   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Amazon store
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Amazon(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Amazon

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Amazon.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Amazon esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Amazon object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Amazon (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Stores within 30 miles");
                System.out.println("2. View Product List");
                System.out.println("3. Place a Order");
                System.out.println("4. View 5 recent orders");

                //the following functionalities basically used by managers
                System.out.println("5. Update Product");
                System.out.println("6. View 5 recent Product Updates Info");
                System.out.println("7. View 5 Popular Items");
                System.out.println("8. View 5 Popular Customers");
                System.out.println("9. Place Product Supply Request to Warehouse");
		System.out.println("10. View Admin Options");
                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewStores(esql); break;
                   case 2: viewProducts(esql); break;
                   case 3: placeOrder(esql); break;
                   case 4: viewRecentOrders(esql); break;
                   case 5: updateProduct(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewPopularProducts(esql); break;
                   case 8: viewPopularCustomers(esql); break;
                   case 9: placeProductSupplyRequests(esql); break;
	     	   case 10: viewAdminOptions(esql); break;
                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Amazon esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine();       //enter lat value between [0.0, 100.0]
                
         if (latitude.matches("^-?[0-9]*\\.?[0-9]+$") && Double.parseDouble(latitude) >= 0.0 && Double.parseDouble(latitude) <= 100.0){
		 System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         	 String longitude = in.readLine();

		 if(longitude.matches("^-?[0-9]*\\.?[0-9]+$") && Double.parseDouble(longitude) >= 0.0 && Double.parseDouble(longitude) <= 100.0){

         	 	String type="Customer";

       	  	 	String query = String.format("INSERT INTO USERS (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);
	 	 	esql.executeUpdate(query);
          		System.out.println ("User successfully created!");
		 }
		else{
			System.out.println("Error: Longitude must be a number between 0.0 and 100.0.");
		} 
	 }
	 else{
		 System.out.println("Error: Latitude must be a number between 0.0 and 100.0.");
	 }
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Amazon esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
	 String IdQuery = String.format("SELECT userID FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
         List<List<String>> userIdResults = esql.executeQueryAndReturnResult(IdQuery);
         if (!userIdResults.isEmpty()){
            userID = userIdResults.get(0).get(0); 
	 }

         int userNum = esql.executeQuery(query);
	 if (userNum > 0){
		return name;
	 }
	 else{
		 System.out.println("Invalid login, user does not exist!");
	 }
	 
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   public static void viewStores(Amazon esql) {
	try{
	 String StoreQuery = String.format("SELECT storeID, latitude, longitude FROM Store");
	 String userQuery = String.format("SELECT latitude, longitude FROM Users WHERE userID = '%s'", userID);
         List<List<String>> storeResults = esql.executeQueryAndReturnResult(StoreQuery);
	 List<List<String>> userResults = esql.executeQueryAndReturnResult(userQuery);
	 double tempDistance;
	 int storesize = storeResults.size();
	 for(int i = 0; i < storesize; i++){
		 tempDistance = esql.calculateDistance(Double.parseDouble(userResults.get(0).get(0)), Double.parseDouble(userResults.get(0).get(1)), Double.parseDouble((storeResults.get(i).get(1))), Double.parseDouble((storeResults.get(i).get(2))));
		 if(tempDistance <= 30.0){
                 	System.out.println("Store ID: " + (storeResults.get(i).get(0)) + " Distance: " + tempDistance);
		 }
	 }
	}
	catch(Exception e){
		System.err.println(e.getMessage());
	}
   
   }
   public static void viewProducts(Amazon esql) {
	   try{
		   System.out.print("\tEnter storeID: ");
         	   String tempstoreID = in.readLine();
		   if(!tempstoreID.matches("^-?[0-9]*\\.?[0-9]+$")){
			   tempstoreID = "0";
		   }
		   String ItemQuery = String.format("SELECT productName, numberOfUnits, pricePerUnit FROM Product p, Store s WHERE s.storeID = '%s' AND s.storeID = p.storeID", tempstoreID);
		   List<List<String>> productResults = esql.executeQueryAndReturnResult(ItemQuery);
		   int productsize = productResults.size();
		   if(productsize == 0){
			   System.out.println("Store ID does not exist");
		   }
		   else{
			   for(int i = 0; i < productsize; i++){
				   System.out.println("Product Name: " + productResults.get(i).get(0) + " Number of Units: " + productResults.get(i).get(1) + " Price per Unit: " + productResults.get(i).get(2));
			   }
		   }

	   }
	   catch(Exception e){
		   System.err.println(e.getMessage());
	   } 
   }
   public static void placeOrder(Amazon esql) {
	   try{
		   System.out.print("\tEnter storeID: ");
		   String tempstoreID = in.readLine();
		   System.out.print("\tEnter Product Name: ");
		   String tempProductName = in.readLine();
		   System.out.print("\tEnter Number of Units Needed: ");
		   String tempUnits = in.readLine();

		   if(!tempstoreID.matches("^-?[0-9]*\\.?[0-9]+$")){
			   tempstoreID = "0";
		   }
		   String StoreLocationQuery = String.format("SELECT latitude, longitude FROM Store WHERE storeID = '%s'", tempstoreID);
         	   String userLocationQuery = String.format("SELECT latitude, longitude FROM Users WHERE userID = '%s'", userID);
		   
		   String stockQuery = String.format("SELECT numberOfUnits FROM Product p, Store s WHERE p.storeID = s.storeID AND productName = '%s' AND s.storeID = '%s'", tempProductName, tempstoreID);
		   
		   String updateItemQuery = String.format("UPDATE PRODUCT SET numberOfUnits = numberOfUnits - %s WHERE storeID = '%s' AND productName = '%s'", tempUnits, tempstoreID, tempProductName);
		   
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   		   LocalDateTime now = LocalDateTime.now();
		   String date = dtf.format(now);

		   String OrderNumber = String.format("SELECT orderNumber FROM Orders ORDER BY orderNumber DESC LIMIT 1;");
                   List<List<String>> mostRecentOrderNumber = esql.executeQueryAndReturnResult(OrderNumber);
		   int thisOrder = Integer.parseInt(mostRecentOrderNumber.get(0).get(0)) + 1;
		   String addOrder = String.format("INSERT INTO ORDERS (orderNumber, customerID, storeID, ProductName, unitsOrdered, orderTime) VALUES ('%s','%s', '%s', '%s','%s','%s')", thisOrder, userID, tempstoreID, tempProductName, tempUnits, date); 

		   List<List<String>> StoreLocation = esql.executeQueryAndReturnResult(StoreLocationQuery);
         	   List<List<String>> userLocation = esql.executeQueryAndReturnResult(userLocationQuery);
                   List<List<String>> itemStock = esql.executeQueryAndReturnResult(stockQuery);

		    if(itemStock.size() == 0){
                           System.out.println("Item does not exist at this location");
                   }
		   if(itemStock.size() > 0){
		   	int tempstock = Integer.parseInt(itemStock.get(0).get(0));
		   	double tempDistance;

		   	tempDistance = esql.calculateDistance(Double.parseDouble(userLocation.get(0).get(0)), Double.parseDouble(userLocation.get(0).get(1)), Double.parseDouble((StoreLocation.get(0).get(0))), Double.parseDouble((StoreLocation.get(0).get(1))));

		   	if(tempDistance <= 30){
			   	System.out.println("Valid Store");
			   	if(tempstock >= Integer.parseInt(tempUnits)){
				   esql.executeUpdate(addOrder);
				   esql.executeUpdate(updateItemQuery);
				   System.out.println("Order successfully placed!");

			   	}
			   	else{
				   	System.out.println("Invalid Order, not enough units available");
			   	}

		   	}
		   	
		   else{
			   System.out.println("Invalid store, store location too far");
		   }
		   }
	   }
	   catch(Exception e){
		   System.err.println(e.getMessage());
	   }
   }
   public static void viewRecentOrders(Amazon esql) {

	   try{
		String checkManager = String.format("SELECT managerID FROM Store s WHERE managerID = '%s'", userID);
	   	String CustomerOrders = String.format("SELECT storeID, productName, unitsOrdered, orderTime FROM Orders WHERE customerID = '%s' ORDER BY orderNumber DESC LIMIT 5;", userID);
		String managerCustomerOrders = String.format("SELECT o.orderNumber, u.name, o.storeID, o.productName, o.orderTime FROM Orders o, Users u, Store s WHERE s.storeID = o.storeID AND o.customerID = u.userID AND s.managerID = '%s'", userID);
		List<List<String>> managerExists = esql.executeQueryAndReturnResult(checkManager);
		
		if(managerExists.size() > 0){
			System.out.println("Orders throughout manager's stores");
			esql.executeQueryAndPrintResult(managerCustomerOrders);
			System.out.println("Manager's Orders: ");	
		}
   	   	esql.executeQueryAndPrintResult(CustomerOrders);
	   }

	   catch(Exception e){
		   System.err.println(e.getMessage());
	   }
   }
   public static void updateProduct(Amazon esql) {
	  try{
		  String checkManager = String.format("SELECT managerID FROM Store s WHERE managerID = '%s'", userID);
		  List<List<String>> managerExists = esql.executeQueryAndReturnResult(checkManager);

		  if(managerExists.size() > 0){
			  System.out.println("Manager Valid");
                   	  System.out.print("\tEnter storeID for product update: ");
                   	  String tempstoreID = in.readLine();
			  if(!tempstoreID.matches("^-?[0-9]*\\.?[0-9]+$")){
				  tempstoreID = "0";
			  }
			  String validStore = String.format("SELECT storeID FROM Store WHERE managerID = %s AND storeID = %s", userID, tempstoreID);

			  List<List<String>> validStores = esql.executeQueryAndReturnResult(validStore);

			  if(validStores.size() > 0){
				  System.out.println(validStores.get(0).get(0));
				  System.out.println("Valid Store Selected");

				  System.out.print("\tEnter product name: ");
				  String tempProductName = in.readLine();
                          	  System.out.print("\tEnter new amount of units: ");
                          	  String units = in.readLine();
				  System.out.print("\tEnter new price per unit: ");
                          	  String price = in.readLine();

				  String stockQuery = String.format("SELECT numberOfUnits FROM Product p, Store s WHERE p.storeID = s.storeID AND productName = '%s' AND s.storeID = '%s'", tempProductName, tempstoreID);
				  List<List<String>> itemStock = esql.executeQueryAndReturnResult(stockQuery);

                    		  if(itemStock.size() == 0){
					  System.out.println("Item does not exist at this location");
                   		  }

				  else if(Integer.parseInt(units) >= 0 && Double.parseDouble(price) >= 0){
					  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                   			  LocalDateTime now = LocalDateTime.now();
                   			  String date = dtf.format(now);
					  System.out.println("Valid input");
					  String updateItemQuery = String.format("UPDATE PRODUCT SET numberOfUnits = %s, pricePerUnit = %s WHERE storeID = '%s' AND productName = '%s'", units, price, tempstoreID, tempProductName);
					  String updateNumber = String.format("SELECT updateNumber FROM ProductUpdates ORDER BY updateNumber DESC LIMIT 1;");
                   			  List<List<String>> mostRecentUpdateNumber = esql.executeQueryAndReturnResult(updateNumber);
                   			  int thisUpdate = Integer.parseInt(mostRecentUpdateNumber.get(0).get(0)) + 1; 
					  System.out.println(thisUpdate);
					  String addProductUpdate = String.format ("INSERT INTO ProductUpdates (updateNumber, managerID, storeID, productName, updatedOn) VALUES ('%s','%s', '%s', '%s','%s')", thisUpdate, userID, tempstoreID, tempProductName, date);
					  esql.executeUpdate(updateItemQuery);
					  esql.executeUpdate(addProductUpdate);

				  }
				  else{
					  System.out.println("Invalid Input");
				  }
				 
			  }
			  else{
				  System.out.println("Store selection invalid");
			  }
		  	}

		
		  else{
			  System.out.println("Invalid, this option only available to managers!");
		  }
	  }

	 catch(Exception e){
		 System.err.println(e.getMessage());
	 } 
   
   }
   public static void viewRecentUpdates(Amazon esql) {
	   try{
		  String checkManager = String.format("SELECT managerID FROM Store s WHERE managerID = '%s'", userID);
                  List<List<String>> managerExists = esql.executeQueryAndReturnResult(checkManager);
                  if(managerExists.size() > 0){
			  String Updates = String.format("SELECT *  FROM ProductUpdates ORDER BY updateNumber DESC LIMIT 5;");
                	  esql.executeQueryAndPrintResult(Updates);

		  }
		  else{
			  System.out.println("Invalid, this option only available to managers!");
		  }
	   }

	   catch(Exception e){
		   System.err.println(e.getMessage());
	   }
   }
   public static void viewPopularProducts(Amazon esql) {
	   try{
		   String checkManager = String.format("SELECT managerID FROM Store s WHERE managerID = '%s'", userID);
                   List<List<String>> managerExists = esql.executeQueryAndReturnResult(checkManager);
                   if(managerExists.size() > 0){

		   	System.out.print("\tEnter storeID for popular products: ");
                   	String tempstoreID = in.readLine();
			if(!tempstoreID.matches("^-?[0-9]*\\.?[0-9]+$")){
				tempstoreID = "0";
			}
                   	String validStore = String.format("SELECT storeID FROM Store WHERE managerID = %s AND storeID = %s", userID, tempstoreID);

                  	List<List<String>> validStores = esql.executeQueryAndReturnResult(validStore);

                   	if(validStores.size() > 0){
			   	String popularItems = String.format("Select productName, storeID, numberOfUnits FROM Product WHERE storeID = %s ORDER BY numberOfUnits ASC LIMIT 5;", tempstoreID);
                           	esql.executeQueryAndPrintResult(popularItems);

		   	}

		   	else{
				System.out.println("Store selection invalid");
		   	}
		   }
		   else{
			  System.out.println("Invalid, this option only available to managers!");
		   }
	   }

	   catch(Exception e){
		   System.err.println(e.getMessage());
	   }
   
   }
   public static void viewPopularCustomers(Amazon esql) {
   
           try{
                   String checkManager = String.format("SELECT managerID FROM Store s WHERE managerID = '%s'", userID);
                   List<List<String>> managerExists = esql.executeQueryAndReturnResult(checkManager);
                   if(managerExists.size() > 0){

                        System.out.print("\tEnter storeID for popular customers: ");
                        String tempstoreID = in.readLine();
			if(!tempstoreID.matches("^-?[0-9]*\\.?[0-9]+$")){
				tempstoreID = "0";
			}
                        String validStore = String.format("SELECT storeID FROM Store WHERE managerID = %s AND storeID = %s", userID, tempstoreID);

                        List<List<String>> validStores = esql.executeQueryAndReturnResult(validStore);

                        if(validStores.size() > 0){
                                String popularCustomers = String.format("SELECT u.name, COUNT(o.customerID) FROM Orders o, Users u WHERE o.customerID = u.userID AND storeID = %s GROUP BY(u.name) ORDER BY COUNT(o.customerID) DESC LIMIT 5;", tempstoreID);
                                esql.executeQueryAndPrintResult(popularCustomers);

                        }

                        else{
                                System.out.println("Store selection invalid");
                        }
                   }
                   else{
                          System.out.println("Invalid, this option only available to managers!");
                   }
           }

           catch(Exception e){
                   System.err.println(e.getMessage());
           }
   
   
   
   }
   public static void placeProductSupplyRequests(Amazon esql) {
	   try{
		   String checkManager = String.format("SELECT managerID FROM Store s WHERE managerID = '%s'", userID);
                   List<List<String>> managerExists = esql.executeQueryAndReturnResult(checkManager);

                  if(managerExists.size() > 0){
			  System.out.print("\tEnter storeID: ");
			  String tempstoreID = in.readLine();
			  if(!tempstoreID.matches("^-?[0-9]*\\.?[0-9]+$")){

				  tempstoreID = "0";
			  }

                          String validStore = String.format("SELECT storeID FROM Store WHERE managerID = %s AND storeID = %s", userID, tempstoreID);

                          List<List<String>> validStores = esql.executeQueryAndReturnResult(validStore);

                          if(validStores.size() > 0){
			  	System.out.print("\tEnter Product Name: ");
			  	String tempProductName = in.readLine();
                                String stockQuery = String.format("SELECT numberOfUnits FROM Product p, Store s WHERE p.storeID = s.storeID AND productName = '%s' AND s.storeID = '%s'", tempProductName, tempstoreID);
                                List<List<String>> itemStock = esql.executeQueryAndReturnResult(stockQuery);

                                if(itemStock.size() > 0){        
                                
			  		System.out.print("\tEnter Number of Units Needed: ");
			  		String tempUnits = in.readLine();
					if(Integer.parseInt(tempUnits) > 0){
			  			System.out.print("\tEnter warehouseID: ");
			  			String tempWarehouse = in.readLine();

						String validWarehouse = String.format("SELECT WarehouseID FROM Warehouse WHERE WarehouseID = %s", tempWarehouse);

						List<List<String>> validWarehouses = esql.executeQueryAndReturnResult(validWarehouse);

						if(validWarehouses.size() > 0){
                                          		String requestNumber = String.format("SELECT requestNumber FROM ProductSupplyRequests ORDER BY requestNumber DESC LIMIT 1;");
							List<List<String>> mostRecentRequestNumber = esql.executeQueryAndReturnResult(requestNumber);
                                          		int thisRequest = Integer.parseInt(mostRecentRequestNumber.get(0).get(0)) + 1;
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        	  	LocalDateTime now = LocalDateTime.now();
                                          		String date = dtf.format(now);

							String addSupplyRequests = String.format ("INSERT INTO ProductSupplyRequests (requestNumber, managerID, warehouseID, storeID, productName, unitsRequested) VALUES ('%s', '%s','%s', '%s', '%s','%s')", thisRequest, userID, tempWarehouse, tempstoreID, tempProductName, tempUnits);

                                          		String updateNumber = String.format("SELECT updateNumber FROM ProductUpdates ORDER BY updateNumber DESC LIMIT 1;");
                                          		List<List<String>> mostRecentUpdateNumber = esql.executeQueryAndReturnResult(updateNumber);
                                          		int thisUpdate = Integer.parseInt(mostRecentUpdateNumber.get(0).get(0)) + 1;
                                          
                                          		String addProductUpdate = String.format ("INSERT INTO ProductUpdates (updateNumber, managerID, storeID, productName, updatedOn) VALUES ('%s','%s', '%s', '%s','%s')", thisUpdate, userID, tempstoreID, tempProductName, date);

							String updateItemQuery = String.format("UPDATE PRODUCT SET numberOfUnits = numberOfUnits + %s WHERE storeID = '%s' AND productName = '%s'", tempUnits, tempstoreID, tempProductName);

							esql.executeUpdate(addSupplyRequests);
                                          		esql.executeUpdate(addProductUpdate);
                                          		esql.executeUpdate(updateItemQuery);


						}
						else{
							System.out.println("Invalid warehouse selected");
						}

					}
					else{
						System.out.println("Unit amount must be greater than 0");
					}
				}
				else{
                                        System.out.println("Item does not exist at this location");
				}
			  }
			  else{
                                System.out.println("Store selection invalid");
			  }
		  }
		  else{
			  System.out.println("Invalid, this option only available to managers!");
		  }


	   }
	   catch(Exception e){
		   System.err.println(e.getMessage());
	   }
   
   }
   public static void viewAdminOptions(Amazon esql) {
	   try{
	   	String checkAdmin = String.format("SELECT userID FROM Users s WHERE type = 'admin' AND userID = '%s'", userID);
	   	List<List<String>> AdminResults = esql.executeQueryAndReturnResult(checkAdmin);
	   
	   	if(AdminResults.size() > 0){
	   		System.out.println("Hello Admin, what would you like to do?");
	   		System.out.println("1. View All User Information");
			System.out.println("2. View All Product Information");
			System.out.println("3. Change User Information");
			System.out.println("4. Change Product Information");
			System.out.println("5. Exit");
			System.out.print("Please make your choice: ");
			String choice = in.readLine();

			if(Integer.parseInt(choice) == 1){
				String showUsers = String.format("SELECT * FROM Users");
				esql.executeQueryAndPrintResult(showUsers);
			}
			else if(Integer.parseInt(choice) == 2){
				String showItems = String.format("SELECT * FROM Product");
				esql.executeQueryAndPrintResult(showItems);
			}
			else if (Integer.parseInt(choice) == 3){
				System.out.print("Enter the User ID for user you would like to change: ");
				String tempuserID = in.readLine();

				String checkUser = String.format("SELECT name FROM Users s WHERE userID = '%s'",tempuserID);
				List<List<String>> userResult = esql.executeQueryAndReturnResult(checkUser);
				if(!userResult.isEmpty()){
					System.out.println("Would you like to change user name, password, latitude, longitude, or all?");
                        		System.out.println("1. Change User Name");
                        		System.out.println("2. Change Password");
                        		System.out.println("3. Change Latitude");
                        		System.out.println("4. Change Longitude");
                        		System.out.println("5. Change All");
					System.out.println("6. Exit");
					System.out.print("Please make you choice: ");
					String userChoice = in.readLine();

					if(Integer.parseInt(userChoice) == 1){
						System.out.print("Enter the new User Name you would like to use: ");
						String newUserName = in.readLine();
						String updateNameQuery = String.format("UPDATE Users SET name = '%s' WHERE userID = '%s'", newUserName, tempuserID);
						esql.executeUpdate(updateNameQuery);
						System.out.println("Change successful!");
					}
					else if(Integer.parseInt(userChoice) == 2){
						System.out.print("Enter the new Password you would like to use: ");
                                                String newPassword = in.readLine();
                                                String updateNameQuery = String.format("UPDATE Users SET password = '%s' WHERE userID = '%s'", newPassword, tempuserID);
                                                esql.executeUpdate(updateNameQuery);
                                                System.out.println("Change successful!");

					}
					else if(Integer.parseInt(userChoice) == 3){
						System.out.print("Enter the new latitude you would like to use: ");
						String newLatitude = in.readLine();

						if(newLatitude.matches("^-?[0-9]*\\.?[0-9]+$") && Double.parseDouble(newLatitude) >= 0.0 && Double.parseDouble(newLatitude) <= 100.0){
							String updateLatitudeQuery = String.format("UPDATE Users SET latitude = '%s' WHERE userID = '%s'", newLatitude, tempuserID);
							esql.executeUpdate(updateLatitudeQuery);
							System.out.println("Change successful!");
						}
						else{
							System.out.println("Error: Latitude must be a number between 0.0 and 100.0.");
						}
					}
					else if(Integer.parseInt(userChoice) == 4){
						System.out.print("Enter the new longitude you would like to use: ");
                                                String newLongitude = in.readLine();

                                                if(newLongitude.matches("^-?[0-9]*\\.?[0-9]+$") && Double.parseDouble(newLongitude) >= 0.0 && Double.parseDouble(newLongitude) <= 100.0){
                                                        String updateLongitudeQuery = String.format("UPDATE Users SET longitude = '%s' WHERE userID = '%s'", newLongitude, tempuserID);
                                                        esql.executeUpdate(updateLongitudeQuery);
							System.out.println("Change successful!");
                                                }
						else{
							System.out.println("Error: Longitude must be a number between 0.0 and 100.0.");
						}
					}
					else if(Integer.parseInt(userChoice) == 5){
                                                System.out.print("Enter the new User Name you would like to use: ");
                                                String newUserName = in.readLine();

						System.out.print("Enter the new Password you would like to use: ");
                                                String newPassword = in.readLine();

                                                System.out.print("Enter the new latitude you would like to use: ");
                                                String newLatitude = in.readLine();

						if(newLatitude.matches("^-?[0-9]*\\.?[0-9]+$") && Double.parseDouble(newLatitude) >= 0.0 && Double.parseDouble(newLatitude) <= 100.0){
							System.out.print("Enter the new longitude you would like to use: ");
                                                	String newLongitude = in.readLine();
							if(newLongitude.matches("^-?[0-9]*\\.?[0-9]+$") && Double.parseDouble(newLongitude) >= 0.0 && Double.parseDouble(newLongitude) <= 100.0){
								String updateEverythingQuery = String.format("UPDATE Users SET name = '%s', password = '%s', latitude = '%s', longitude = '%s'  WHERE userID = '%s'", newUserName, newPassword, newLatitude, newLongitude, tempuserID);
								esql.executeUpdate(updateEverythingQuery);
								System.out.println("Change successful!");
							}
							else{
                                                        	System.out.println("Error: Longitude must be a number between 0.0 and 100.0.");
							}
						}
						else{
                                                        System.out.println("Error: Latitude must be a number between 0.0 and 100.0.");
						}
					}
					else if(Integer.parseInt(userChoice) == 6){
						System.out.println("Goodbye!");

					}
					else{
						System.out.println("Invalid Choice");
					}

				}
				else{
					System.out.println("Invalid userID, user does not exist");
				}
			}
			else if (Integer.parseInt(choice) == 4){
				System.out.print("Enter the Store ID for product you would like to change: ");
                                String tempstoreID = in.readLine();
				if(!tempstoreID.matches("^-?[0-9]*\\.?[0-9]+$")){
					tempstoreID = "0";
				}
				System.out.print("Enter the product you would like to change: ");
				String tempproductName = in.readLine();
				String checkStore = String.format("SELECT storeID, productName FROM Product s WHERE storeID = '%s' AND productName = '%s'",tempstoreID, tempproductName);
                                List<List<String>> storeResult = esql.executeQueryAndReturnResult(checkStore);

				if(!storeResult.isEmpty()){
					System.out.println("Valid store and product combination");
                                        System.out.println("Would you like to change number of units, price per unit, or all?");
                                        System.out.println("1. Change Number of Units");
                                        System.out.println("2. Change Price Per Unit");
                                        System.out.println("3. Change All");
                                        System.out.println("4. Exit");
                                        System.out.print("Please make you choice: ");

                                        String userChoice = in.readLine();

					if(Integer.parseInt(userChoice) == 1){
						System.out.print("Enter the new number of units: ");
						String newUnits = in.readLine();
						if(newUnits.matches("^-?[0-9]*\\.?[0-9]+$") && Integer.parseInt(newUnits) >= 0){
                                                	String updateProductNameQuery = String.format("UPDATE Product SET numberOfUnits = '%s' WHERE storeID = '%s' AND productName = '%s'", newUnits, tempstoreID, tempproductName);
                                                	esql.executeUpdate(updateProductNameQuery);
                                                	System.out.println("Change successful!");
						}
						else{
							System.out.println("Error, new number of units must be a number greater than or equal to 0");
						}
					}
				
					else if(Integer.parseInt(userChoice) == 2){
                                                System.out.print("Enter the new price per unit: ");
                                                String newPrice = in.readLine();
                                                if(newPrice.matches("^-?[0-9]*\\.?[0-9]+$") && Integer.parseInt(newPrice) >= 0){
                                                        String updateProductPriceQuery = String.format("UPDATE Product SET pricePerUnit = '%s' WHERE storeID = '%s' AND productName = '%s'", newPrice, tempstoreID, tempproductName);
                                                        esql.executeUpdate(updateProductPriceQuery);
                                                        System.out.println("Change successful!");
                                                }
                                                else{
                                                        System.out.println("Error, new price of units must be a number greater than or equal to 0");
                                                }
 
                                        }

                                   if(Integer.parseInt(userChoice) == 3){
                                                System.out.print("Enter the new number of units: ");
                                                String newUnits = in.readLine();
                                                if(newUnits.matches("^-?[0-9]*\\.?[0-9]+$") && Integer.parseInt(newUnits) >= 0){
                                                	System.out.print("Enter the new price per unit: ");
                                                	String newPrice = in.readLine();
                                                	if(newPrice.matches("^-?[0-9]*\\.?[0-9]+$") && Integer.parseInt(newPrice) >= 0){
                                                        	String updateProductPriceQuery = String.format("UPDATE Product SET numberOfUnits = '%s', pricePerUnit = '%s' WHERE storeID = '%s' AND productName = '%s'", newUnits, newPrice, tempstoreID, tempproductName);                               
                                                        	esql.executeUpdate(updateProductPriceQuery);
                                                        	System.out.println("Change successful!");
                                                }
                                                	else{   
                                                        	System.out.println("Error, new price of units must be a number greater than or equal to 0");
                                                	}
                                                }
                                                else{
                                                        System.out.println("Error, new number of units must be a number greater than or equal to 0");
                                                }
                                        }
				   else if(Integer.parseInt(userChoice) == 4){
					   System.out.println("Goodbye!");
				   }
				   else{
					   System.out.println("Invalid choice");
				   }

        
				}

				else{
					System.out.println("Invalid storeID or product name, that combination of storeID and product name does not exist!");
				}

			}
			else if (Integer.parseInt(choice) == 5){
				System.out.println("Goodbye!");

			}

			else{
				System.out.println("Invalid choice");
			}

	   	}
	   	else{
			System.out.println("Invalid, this option is only availiable to Administrators!");
	   	}
   	    }
	   catch(Exception e){
		   System.err.println(e.getMessage());
	   }
   }

}//end Amazon

