package objects;

import storingOfData.ReadWrite;

import java.io.Serializable;
import java.util.LinkedList;

public abstract class User implements Serializable {
   private final String username;
   private final String hashedPassword;

   public User(String username, String hashedPassword) throws IllegalArgumentException
   {
       LinkedList<User> appUsers = ReadWrite.readUser();

       for (User currentUser : appUsers) {
           if (currentUser.getUsername().equalsIgnoreCase(username)) {
               throw new IllegalArgumentException(
                       "username is already in use");
           }
       }

       this.username = username;
       this.hashedPassword = hashedPassword;
   }

   public String getUsername() {
       return username;
   }

   public String getHashedPassword() {
       return hashedPassword;
   }
}

