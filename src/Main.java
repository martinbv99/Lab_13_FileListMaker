import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<String> userlist = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static Scanner in = new Scanner(System.in);
    private static String currentFile = null; // Track the file we're working with

    public static void main(String[] args) {
        boolean quit = false;
        do {
            displayMenu();
            String choice = SafeInput.getRegExString(in, "Enter a choice", "[AaDdIiPpQqMmOoSsCcVv]"); // Updated regex for menu options
            switch (choice.toUpperCase()) {
                case "A":
                    addItem();
                    break;
                case "D":
                    deleteItem();
                    break;
                case "I":
                    insertItem();
                    break;

                case "M":
                    moveItem();
                    break;
                case "O":
                    openList();
                    break;
                case "S":
                    saveList();
                    break;
                case "C":
                    clearList();
                    break;
                case "V":
                    viewList();
                    break;
                case "Q":
                    quit = quitProgram();
                    break;
            }
        } while (!quit);
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from list");
        System.out.println("I - Insert an item into the list");
        System.out.println("M - Move an item within the list");
        System.out.println("O - Open a list from files");
        System.out.println("S - Save the current list to file");
        System.out.println("C - Clear the list");
        System.out.println("V - View the list");
        System.out.println("Q - Quit");
    }

    private static void addItem() {
        String item = SafeInput.getNonZeroLenString(in, "Enter an item to add");
        userlist.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem() {
        int itemNumber = SafeInput.getRangedInt(in, "Enter the item number to delete", 1, userlist.size());
        userlist.remove(itemNumber - 1);
        needsToBeSaved = true;
    }

    private static void insertItem() {
        int itemNumber = SafeInput.getRangedInt(in, "Enter the position to insert the item", 1, userlist.size() + 1);
        String item = SafeInput.getNonZeroLenString(in, "Enter an item to insert");
        userlist.add(itemNumber - 1, item);
        needsToBeSaved = true;
    }

    private static void moveItem() {
        int fromIndex = SafeInput.getRangedInt(in, "Enter the item number to move", 1, userlist.size()) - 1;
        int toIndex = SafeInput.getRangedInt(in, "Enter the new position for the item", 1, userlist.size());
        String item = userlist.remove(fromIndex);
        userlist.add(toIndex - 1, item);
        needsToBeSaved = true;
    }

    private static void viewList() {
        if (userlist.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            for (int i = 0; i < userlist.size(); i++) {
                System.out.println((i + 1) + ": " + userlist.get(i));
            }
        }
    }

    private static void openList() {
        if (needsToBeSaved) {
            boolean saveFirst = SafeInput.getYNConfirm(in, "Do you want to save before opening?");
            if (saveFirst) {
                saveList();
            }
        }

        String filename = SafeInput.getNonZeroLenString(in, "Enter the name");
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                userlist.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    userlist.add(line);
                }
                currentFile = filename;
                needsToBeSaved = false;
                System.out.println("List loaded from " + filename);
            } catch (IOException e) {
                System.out.println("Error opening file: " + e.getMessage());
            }
        } else {
            System.out.println("File not found!");
        }
    }

    private static void saveList() {
        if (currentFile == null) {
            currentFile = SafeInput.getNonZeroLenString(in, "Enter a file name");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            for (String item : userlist) {
                writer.write(item);
                writer.newLine();
            }
            needsToBeSaved = false;
            System.out.println("List saved to " + currentFile);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void clearList() {
        userlist.clear();
        needsToBeSaved = true;
    }

    private static boolean quitProgram() {
        if (needsToBeSaved) {
            boolean saveFirst = SafeInput.getYNConfirm(in, "Do you want to save before quitting?");
            if (saveFirst) {
                saveList();
            }
        }
        return true;
    }
}