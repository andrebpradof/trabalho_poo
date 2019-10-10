package Editor;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    /**
     * le o caracter digitado
     * @return retorna o caracter lido em um char
     */

    public static char lerCaracterTeclado() {
        Scanner ler = new Scanner(System.in);
        return ler.next().charAt(0);
    }

    /**
     * Le os comandos do usuario e realiza as tarefas de edicoes compativeis com tais comandos
     * @throws IOException tratamento de excessao para o salvamento do texto
     */

    public static void editor() throws IOException {
        Scanner ler = new Scanner(System.in);
        String opcao = "";
        String getTexto;

        while (true) {
            getTexto = ler.nextLine();

            if(getTexto.length() > 1)
                opcao = getTexto.substring(0, 2);

            switch (opcao) {
                case ":z":
                    System.out.print(Texto.desfazer());
                    break;
                case ":y":
                    System.out.print(Texto.refazer());
                    break;
                case ":q":
                    System.out.print("\nDeseja salvar o texto? (s/n): ");
                    if(lerCaracterTeclado() == 's'){
                        Texto.salvar();
                        System.out.println("Editor.Texto salvo!");
                    }
                    return;
                default:
                    Texto.inserir(getTexto);
            }
        }
    }

    /**
     * Exibe as opcoes de navegacao do usuario no programa, le o comando do usuario e realiza/chama a opcao desejada.
     * @throws IOException tratamento de erro para as tarefas envolvendo arquivos.
     * @throws InterruptedException tratamento de erro ao sair-se do programa.
     */

    public static void menu() throws IOException, InterruptedException {
        while (true) {
            System.out.println("*********** EDITOR DE TEXTO ***********");
            System.out.println("MENU");
            System.out.println("1 - Novo texto");
            System.out.println("2 - Carregar texto salvo");
            System.out.println("3 - Continuar edição");
            System.out.println("4 - Salvar");
            System.out.println("5 - Instruções");
            System.out.println("6 - Sair");
            System.out.print("Opção: ");

            switch (lerCaracterTeclado()) {
                case '1':
                    Texto.limparTexto();
                    System.out.println("\n******* EDITOR *******");
                    editor();
                    break;

                case '2':
                    System.out.println("\n******* EDITOR *******");
                    Texto.limparTexto();
                    Texto.lerArquivo();
                    System.out.print(Texto.getTexto());
                    editor();
                    break;

                case '3':
                    System.out.println("\n******* EDITOR *******");
                    System.out.print(Texto.getTexto());
                    editor();

                case '4':
                    Texto.salvar();
                    System.out.println("Texto salvo!");
                    break;

                case '5':
                    System.out.println("    Comandos de edição:");
                    System.out.println("-Desfazer= :z");
                    System.out.println("-Refazer= :y");
                    System.out.println("-Sair= :q");
                    break;

                case '6':
                    System.exit(0);
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        menu();
    }
}