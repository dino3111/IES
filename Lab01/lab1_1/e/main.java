package e;

import java.util.Scanner;
import java.util.logging.*;

public class main {
    private static final Logger log = Logger.getLogger("App");

    public static void main(String[] args) {
        log.info("Aplicação iniciada");

        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Introduza um número: ");
            int x = sc.nextInt();
            log.info("Número introduzido: " + x);
        } catch (Exception e) {
            log.severe("Erro ao ler número");
        }

        log.info("Aplicação terminada");
    }
}
