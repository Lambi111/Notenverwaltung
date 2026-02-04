package de.htwsaar.UserInterface;

import java.util.*;

public abstract class AbstraktCLI implements CI {
    protected final Scanner scanner;

    protected AbstraktCLI(Scanner scanner) {
        this.scanner = scanner;
    }

    protected void header(String title) {
        System.out.println("\n -- " + title + " -- ");
    }

    protected String read(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    protected int readInt(String label) {
        try {
            return Integer.parseInt(read(label));
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Bitte eine Zahl eingeben");
        }
    }
}
