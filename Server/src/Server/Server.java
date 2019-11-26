package Server;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe que controla os clientes que entram no servidor, como tambem a manipulacao dos arquivos
 */

public class Server {
    private final ArrayList<Client> clients = new ArrayList<Client>();
    private final ArrayList<FileServer> fileServers = new ArrayList<FileServer>();
    private static String connect;
    private static String directory;
    private static String input;

    /**
     * Retorna um Array com os clientes
     * @return ArrayList de clientes
     */
    public ArrayList<Client> getClientes() {
        return clients;
    }

    /**
     * Retorna uma Array com os arquivos
     * @return ArrayList de fileServer
     */

    public ArrayList<FileServer> getFileServers() {
        return fileServers;
    }

    /**
     * Retorna o Connect
     * @return string connect
     */
    public static String getConnect() {
        return connect;
    }

    /**
     * Seta o connect
     * @param connectIn
     */

    public static void setConnect(String connectIn) {
        connect = connectIn;
    }

    /**
     * Retorna o diretorio
     * @return
     */

    public static String getDirectory() {
        return directory;
    }

    /**
     * Seta o doretorio
     * @param directoryIn
     */

    public static void setDirectory(String directoryIn) {
        directory = directoryIn;
    }

    /**
     * Função auxiliar de mensagem
     * @param text Texto da mensagem
     * @param title Titulo da mensagem
     * @param type Tipo da mensagem
     */

    public static void message(String text, String title, int type){
        JOptionPane optionPane = new JOptionPane(text,type);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    /**
     * Cria um novo arquivo
     * @param name Nome do arquivo
     * @return Retorna -1 caso o arquivo ja exista no diretorio
     * @throws IOException
     */

    public int newFile(String name) throws IOException {
        File file = new File(directory+"/"+name+".txt");
        if(!file.exists()){
            file.createNewFile();
            FileServer help = new FileServer();

            help.setDirectory(directory+"/"+name+".txt");
            help.setSize(0);
            help.setName(name);
            fileServers.add(help);

            return fileServers.lastIndexOf(help);
        }
        else{
            System.out.println("Esse arquivo já existe nesse diretório");
            return -1;
        }
    }

    /**
     * Lista os arquivos de um diretorio e coloca em um Array
     */

    public void filelist(){

        try {
            File f = new File(directory);

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.endsWith(".txt");
                }
            };


            File[] files = f.listFiles(filter);

            for (int i = 0; i < files.length; i++) {
                FileServer help = new FileServer();

                help.setDirectory(files[i].getAbsolutePath());
                help.setSize(files[i].length());
                help.setName(files[i].getName());

                fileServers.add(help);
            }

        } catch (Exception e) {
            message("Erro no diretório "+directory+"!", "Erro", 0);
        }
    }

    /**
     * Salva alteracoes em um arquivo
     * @param FileId id do arquivo
     * @param text Texto para ser salvo
     * @throws IOException
     */

    public void updateFile(int FileId, String text) throws IOException{
        String metDirectory = fileServers.get(FileId).getDirectory();
        File file = new File(metDirectory);
        Writer writer;

        writer = new BufferedWriter(new FileWriter(file, true));
        writer.append(text+"\n");
        writer.close();

    }

    /**
     * Imprime na tela os arquivos de um diretorio
     */

    public void showFiles(){
        int i;
        for (i = 0; i < fileServers.size(); i++) {
            System.out.print("id: "+i+" - Nome do arquivo: "+fileServers.get(i).getName());
            System.out.println("");
        }
    }

    /**
     * Seta o diretorio de edicao e espera a conexao para iniciar a Thread do connect
     * @param args
     * @throws IOException
     */

    public static void main(String []args) throws IOException {
        Server server = new Server();
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("Diretório para os arquivos: ");
            directory = scanner.nextLine();
            if(directory != null){ break; }
            message("Diretório inválido!", "Erro", 0);
        }

        System.out.println("******** Escutando Usuários ********");
        System.out.print("Digite conectar para logar no servidor: ");

        server.filelist(); //lista arquivos da pasta e salva em uma lista



        while(true){
            input = scanner.next();
            if("conectar".compareToIgnoreCase(input) == 0){
                System.out.println("Novo usuário conectado!");
                System.out.println();

                Client user = new Client();

                Runnable runnable = new Connect(user, server);
                Thread t = new Thread(runnable);
                t.start();
                break; // Apenas na parte 2
            }
        }
    }// Fim do método main
}


