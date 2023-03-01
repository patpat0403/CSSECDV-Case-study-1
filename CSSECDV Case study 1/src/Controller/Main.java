package Controller;


import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;
import View.Frame;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Main {
    
    public SQLite sqlite;
    private static final Pattern password_pattern= Pattern.compile( "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{11,64}$");
    
    public static void main(String[] args) {
        new Main().init();
    }
    
    public void init(){
        // Initialize a driver object
      sqlite = new SQLite();

//        // Create a database
//        sqlite.createNewDatabase();
//        
//        // Drop users table if needed
//        sqlite.dropHistoryTable();
//        sqlite.dropLogsTable();
//        sqlite.dropProductTable();
 //      sqlite.dropUserTable();
//        
//        // Create users table if not exist
//        sqlite.createHistoryTable();
//        sqlite.createLogsTable();
//        sqlite.createProductTable();
 //     sqlite.createUserTable();
//        
//        // Add sample history
//        sqlite.addHistory("admin", "Antivirus", 1, "2019-04-03 14:30:00.000");
//        sqlite.addHistory("manager", "Firewall", 1, "2019-04-03 14:30:01.000");
//        sqlite.addHistory("staff", "Scanner", 1, "2019-04-03 14:30:02.000");
//        
//        // Add sample logs
//        sqlite.addLogs("NOTICE", "admin", "User creation successful", new Timestamp(new Date().getTime()).toString());
//        sqlite.addLogs("NOTICE", "manager", "User creation successful", new Timestamp(new Date().getTime()).toString());
//        sqlite.addLogs("NOTICE", "admin", "User creation successful", new Timestamp(new Date().getTime()).toString());
//        
//        // Add sample product
//        sqlite.addProduct("Antivirus", 5, 500.0);
//        sqlite.addProduct("Firewall", 3, 1000.0);
//        sqlite.addProduct("Scanner", 10, 100.0);
//
//        // Add sample users
 //     sqlite.addUser("admin", this.generateHashedPassword("qwerty1234") , 5);
 //     sqlite.addUser("manager", this.generateHashedPassword("qwerty1234"), 4);
 //       sqlite.addUser("staff", this.generateHashedPassword("qwerty1234"), 3);
 //    sqlite.addUser("client1", this.generateHashedPassword("qwerty1234"), 2);
 //      sqlite.addUser("client2", this.generateHashedPassword("qwerty1234"), 2);
//        
//        
//        // Get users
//        ArrayList<History> histories = sqlite.getHistory();
//        for(int nCtr = 0; nCtr < histories.size(); nCtr++){
//            System.out.println("===== History " + histories.get(nCtr).getId() + " =====");
//            System.out.println(" Username: " + histories.get(nCtr).getUsername());
//            System.out.println(" Name: " + histories.get(nCtr).getName());
//            System.out.println(" Stock: " + histories.get(nCtr).getStock());
//            System.out.println(" Timestamp: " + histories.get(nCtr).getTimestamp());
//        }
//        
//        // Get users
//        ArrayList<Logs> logs = sqlite.getLogs();
//        for(int nCtr = 0; nCtr < logs.size(); nCtr++){
//            System.out.println("===== Logs " + logs.get(nCtr).getId() + " =====");
//            System.out.println(" Username: " + logs.get(nCtr).getEvent());
//            System.out.println(" Password: " + logs.get(nCtr).getUsername());
//            System.out.println(" Role: " + logs.get(nCtr).getDesc());
//            System.out.println(" Timestamp: " + logs.get(nCtr).getTimestamp());
//        }
//        
//        // Get users
//        ArrayList<Product> products = sqlite.getProduct();
//        for(int nCtr = 0; nCtr < products.size(); nCtr++){
//            System.out.println("===== Product " + products.get(nCtr).getId() + " =====");
//            System.out.println(" Name: " + products.get(nCtr).getName());
//            System.out.println(" Stock: " + products.get(nCtr).getStock());
//            System.out.println(" Price: " + products.get(nCtr).getPrice());
//        }
//        // Get users
//        ArrayList<User> users = sqlite.getUsers();
//        for(int nCtr = 0; nCtr < users.size(); nCtr++){
//            System.out.println("===== User " + users.get(nCtr).getId() + " =====");
//            System.out.println(" Username: " + users.get(nCtr).getUsername());
//            System.out.println(" Password: " + users.get(nCtr).getPassword());
//            System.out.println(" Role: " + users.get(nCtr).getRole());
//            System.out.println(" Locked: " + users.get(nCtr).getLocked());
//        }
        
        // Initialize User Interface
        Frame frame = new Frame();
        
        frame.init(this);
    }
    
    public boolean passMatcher(String password)
    {
        Matcher matcher= password_pattern.matcher(password);
        
        return matcher.matches();
    }
    
    public String generateHashedPassword(String password)
    {
        String generatedPassword= null;
        try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(password.getBytes());

                byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

                
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < hashedPassword.length; i++) {
                    s.append(Integer.toString((hashedPassword[i] & 0xff) + 0x100, 16).substring(1));
                }

                generatedPassword = s.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        
        return generatedPassword;
    }
    
    public int verifyRegister(String username, String password, String confpass)
    {
        
        if (password.length()<11)
        {
            System.out.println("Password too short");
            return 1;
        }
        
        else if (!passMatcher(password))//Passord not matching regex for password  complexity
        {
            System.out.println("Password too simple");
            return 1;
        }
        
        else if(!(password.equals(confpass)))
        {
            System.out.println("Password not matching  ");
            return 1;
        }
        
        else
        {
            ArrayList<User> users = sqlite.getUsers();
            //TODO: check database if username exists , check for case insensitivity and if unique
            for(int nCtr = 0; nCtr < users.size(); nCtr++)
            {
                if(username.equalsIgnoreCase(users.get(nCtr).getUsername()))
                     {
                        System.out.println("Username exists");
                        return 1;
                     }
            }
             
            return 0;
        }
            
    }
    
    public int verifyLogin(String username, String password)
    {
         ArrayList<User> users = sqlite.getUsers();
         int invalid = 1;
          for(int nCtr = 0; nCtr < users.size(); nCtr++)
          {
              if (username.equals("")|| password.equals(""))//check for blank text fields
              {
                  System.out.println("Empty field");
                  return invalid= 1;
              }
                  
              else if (username.equalsIgnoreCase(users.get(nCtr).getUsername()))
              {
                  if (users.get(nCtr).getRole()== 1)// check if account is locked
                  {
                      System.out.println("Locked user");
                      return invalid= 1;
                  }
                  //TODO check if hashedpassword is the same    
                  else if(!(this.generateHashedPassword(password).equals(users.get(nCtr).getPassword())))
                  {
                     System.out.println("Wrong password");
                     return invalid =  2;  
                  }
                     
                  else
                  { 
                    

                     return 0;
                  }
              }
            
          }
         
          return invalid;
    }
    
}
