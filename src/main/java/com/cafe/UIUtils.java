package com.cafe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UIUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printHeader(String title) {
        System.out.println("\n===================================");
        System.out.println("  " + title.toUpperCase());
        System.out.println("===================================");
    }

    public static void printMessage(String message) {
        System.out.println(">> " + message);
    }

    public static void printError(String message) {
        System.err.println("[ERROR] " + message);
    }

    public static String readString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                // Membaca input sebagai String, lalu parsing ke Int
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printError("Input harus berupa angka bulat. Coba lagi.");
            }
        }
    }

    public static int readMenuChoice(int maxOption) {
        int choice;
        while (true) {
            try {
                System.out.print("Pilih menu (" + 1 + "-" + maxOption + "): ");
                String input = scanner.nextLine().trim();
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= maxOption) {
                    return choice;
                } else {
                    printError("Pilihan tidak valid. Masukkan angka antara 1 dan " + maxOption + ".");
                }
            } catch (NumberFormatException e) {
                printError("Input harus berupa angka. Coba lagi.");
            }
        }
    }
}