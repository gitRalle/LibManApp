package storingOfData;

import objects.User;

import java.io.*;
import java.util.LinkedList;

public class ReadWrite {
    private static String path = "data/user.data";
    private static File file = new File(path);

    public static void writeUser(LinkedList<User> myUsers) {

        try (ObjectOutputStream oOut = new ObjectOutputStream(new FileOutputStream(file)))
        {
            for (User currentUser : myUsers)
                oOut.writeObject(currentUser);
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static LinkedList<User> readUser() {
        boolean cont = true;
        Object objectFromFile;
        User currentUser;
        LinkedList<User> myUsers = new LinkedList<>();

        try (ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(file)))
        {
            while(cont) {
                objectFromFile = oIn.readObject();

                if (objectFromFile != null) {
                    currentUser = (User) objectFromFile;
                    myUsers.add(currentUser);
                }

                else
                    cont = false;
            }

        }

        catch (EOFException | ClassNotFoundException ex) {
            // end of stream
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }

        return myUsers;
    }

    /**
     * Cannot be used to create a new file - only append an existing one
     *
     * @param outUser
     */
    public static void appendUser(User outUser) {
        class AppendableObjectOutputStream extends ObjectOutputStream {

            private AppendableObjectOutputStream(OutputStream out) throws IOException {
                super(out);
            }

            @Override
            protected void writeStreamHeader() throws IOException {
                /*
                do not write a header but reset:
                this line added after another question
                showed a problem with the original
                 */
                reset();
            }
        }

        if (!file.exists()) {
            writeUser(new LinkedList<User>());
        }

        try (AppendableObjectOutputStream aOin = new AppendableObjectOutputStream(new FileOutputStream(file)))
        {
            aOin.writeObject(outUser);
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }



    }
}
