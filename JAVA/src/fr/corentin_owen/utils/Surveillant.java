package fr.corentin_owen.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe abstraite utilitaire à adapter pour intéragir avec un objet fourni au constructeur.<br/>
 * L'idée est que l'objet surveillé peut continuer à fonctionner pendant la surveillance.
 *
 * @author Jean-Charles BOISSON 11/2021
 * @version 1.0
 */
public abstract class Surveillant implements Runnable {

    /**
     * ArrayList de String pour le détail des possibilités.<br/>
     * Variable mise en "protected" pour que les classes filles puisse l'utiliser
     */
    protected List<String> menu;

    /**
     * Constructeur juste là pour initialiser menu
     * et y mettre le premier choix du menu qui est celui de sortie
     */
    public Surveillant() {
        this.menu = new ArrayList<>();
        this.menu.add("Stop");
    }

    /**
     * Permet d'agir en fonction du choix de l'utilisateur
     * Cette méthode est dépendante du contexte
     *
     * @param choix Le choix de l'utilisateur
     */
    public abstract void appliqueChoix(int choix);

    /**
     * Implémentation de la méthode run qui est utilisée lors du démarrage du Thread.<br/>
     * Cette méthode ne doit pas être appelée directement,<br/>
     * c'est start (de Thread) qui l'utilise.
     */
    public void run() {

        var lecteur = new Scanner(System.in);

        int choix = -1;

        while (choix != 0) {

            System.out.println("\nChoose an action: ");
            for (int i = 0; i < menu.size(); i++) {
                System.out.println("\t" + i + " = " + menu.get(i));
            }

            System.out.print("\nYour choice ? (0-" + (menu.size() - 1) + "): ");

            choix = Integer.parseInt(lecteur.nextLine());
            appliqueChoix(choix);
        }
        lecteur.close();
    }
}