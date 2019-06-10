package ui;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class made to represent a console app menu
 */
public class Menu {
    private int numOfItems = 0;
    private int counter;
    private ArrayList<String> menu = new ArrayList<>();
    private ArrayList<String> completeMenu = new ArrayList<>();

    private String title;


    // Visual specs
    private String cornerChar = "+";
    private char horizontalChar = '-';
    private char verticalChar = '|';


    public Menu(String title, String header) {
        menu.add("<" + title + ">");
        menu.add(verticalChar + " " + header);

        this.title = title;

    }


    public void addMenuItem(String title) {
        numOfItems++;
        menu.add(verticalChar + " " + numOfItems + ". " + title);
    }

    private void format() {
        // longest menuItem
        counter = menu.get(1).length();

        // find longest menuItem
        for (int i = 2; i < menu.size(); i++) {
            if (menu.get(i).length() > counter) {
                counter = menu.get(i).length();
            }
        }

        // format spaces and characters to fit longest menuItem's length
        for (int i = 1; i < menu.size(); i++) {
            if (menu.get(i).length() <= counter) {
                menu.set(i, menu.get(i) + getSpace(counter - menu.get(i).length()) + " " + verticalChar);
            }
        }

        // create new list with dashes
        completeMenu.add(menu.get(0));
        completeMenu.add(cornerChar + getChar(counter) + cornerChar);
        completeMenu.add(menu.get(1));
        completeMenu.add(cornerChar + getChar(counter) + cornerChar);

        for (int i = 2; i < menu.size(); i++) {
            completeMenu.add(menu.get(i));
        }

        completeMenu.add(cornerChar + getChar(counter) + cornerChar);
        completeMenu.add("<>");

    }


    public void showMenu() {
      //  System.out.print("\n" + menuString);
        format();
        System.out.print("\n");
        for (int i = 0; i < completeMenu.size(); i++) {
            System.out.printf("%s%s", completeMenu.get(i), i < completeMenu.size() - 1 ? "\n" : "");
        }
    }



    private String getSpace(int length) {
        int i = length;
        String someNum = "";

        char[] zeroes1 = new char[i];
        Arrays.fill(zeroes1, ' ');
        String newNum1 = someNum + new String(zeroes1);

        return newNum1;
    }

    private String getChar(int length) {
        int i = length;
        String someNum = "";

        char[] zeroes1 = new char[i];
        Arrays.fill(zeroes1, horizontalChar);
        String newNum1 = someNum + new String(zeroes1);

        return newNum1;
    }


    public String getTitle() {
        return title;
    }
}
