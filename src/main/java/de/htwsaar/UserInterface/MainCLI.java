package de.htwsaar.UserInterface;

import de.htwsaar.UserInterface.AbstraktCLI;
import de.htwsaar.UserInterface.CI;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MainCLI  extends AbstraktCLI {
    private final Map<String,Runnable> actions = new LinkedHashMap<>();

    public MainCLI(Scanner scanner, CI kursMenu, CI studentMenu, CI noteMenu) {
        super(scanner);
        actions.put("1", kursMenu::starten);
        actions.put("2", studentMenu::starten);
        actions.put("3", noteMenu::starten);
        actions.put("0", () -> System.exit(0));
    }

    @Override
    public void starten() {
        while (true) {
            header("Hauptmenü");
            System.out.println("1) Kursverwaltung");
            System.out.println("2) Studentenverwaltung");
            System.out.println("3) Notenverwaltung");
            System.out.println("0) Beenden");

            Runnable action = actions.get(read("Auswahl"));
            if(action != null) {
                action.run();
            } else {
                System.out.println("❌ Ungültige Auswahl");
            }

        }
    }


}