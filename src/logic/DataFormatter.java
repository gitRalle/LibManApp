package logic;

import java.util.ArrayList;
import java.util.Arrays;

public class DataFormatter {

    private ArrayList<String> formatted = new ArrayList<>();
    private int[] max;
    private int longestArg;
    private final char headerChar;
    private final char dividerChar;
    private final char sideChar;
    private String title;
    private String cursor = "<>";

    public DataFormatter() {
        headerChar = '-';
        dividerChar = '-';
        sideChar = '|';
    }

    public DataFormatter(String title) {
        this.title = title;
        headerChar = '-';
        dividerChar = '-';
        sideChar = '|';

    }


    public DataFormatter(char headerCh, char dividerCh, char sideCh) {
        headerChar = headerCh;
        dividerChar = dividerCh;
        sideChar = sideCh;
    }

    public DataFormatter(String title, char headerCh, char dividerCh, char sideCh) {
        this.title = title;
        headerChar = headerCh;
        dividerChar = dividerCh;
        sideChar = sideCh;
    }



    public void formatData(String[] data) {
        int longest = 0;

        // find longest String
        for (String currentData : data) {
            if (currentData.length() > longest) {
                longest = currentData.length();
            }
        }
        this.longestArg = longest;

        // create new list
        int currentArg = 0;
        // add header
        formatted.add(getChar(headerChar, longestArg + 1));
        // loop through data
        for (int i = 0; i < data.length; i++) {
            currentArg = data[i].length();
            // add data entry
            formatted.add(data[i] + " " + getChar(' ', longestArg - currentArg) + getChar(sideChar, 1));

            // add divider
            if (i < data.length - 1) {
                formatted.add(getChar(dividerChar, longestArg + 1) + sideChar);
            }

            // add footer
            else {
                formatted.add(getChar(headerChar, longestArg + 1));
            }
        }

    }

    public void formatData(String[][] data) {
        int longest = 0;
        int longestRow = 0;

        // find longest String
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                longestRow += data[row][col].length();
            }

            if (longestRow > longest) {
                longest = longestRow;
            }
            longestRow = 0;
        }

        this.longestArg = longest;


        // find longest substrings
        this.max = new int[data[0].length];
        int tempLongest = 0;
        int longestArg = 0;


        for (int col = 0; col < data[0].length; col++) {
            for (int row = 0; row < data.length; row++) {
                tempLongest = data[row][col].length();
                if (tempLongest > longestArg) {
                    longestArg = tempLongest;
                }
            }
            if (longestArg > max[col]) {
                max[col] = longestArg;
            }
            longestArg = 0;
        }

        // FORMAT ->
        formatted.add(getChar(headerChar, this.longestArg + 1));
        String str = "";
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                str += String.format("%s%s", data[row][col], getChar(' ', max[col] - data[row][col].length()));
            }

            // add string entry
            formatted.add(str +  getChar(sideChar, 1));

            // add divider
            if (row < data.length - 1) {
                formatted.add(getChar(dividerChar, this.longestArg + 1) + sideChar);
            }

            // add footer
            else {
                formatted.add(getChar(headerChar, this.longestArg + 1));
            }

            str = "";

        }

    }


    public void showData() {
        System.out.print("\n");
        if (title != null) {
            System.out.println("<" + getTitle() + ">");
        }

        for (String currentLine : formatted) {
            System.out.println(currentLine);
        }
        System.out.print(cursor);
    }


    private String getChar(char ch, int length) {
        String someNum = "";
        char[] zeroes1 = new char[length];
        Arrays.fill(zeroes1, ch);

        return someNum + new String(zeroes1);
    }

    public String getTitle() {
        return title;
    }


    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
