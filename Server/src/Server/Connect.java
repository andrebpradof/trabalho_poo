package Server;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connect implements Runnable{
    private Client client;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;


    public Connect(Client client, Socket socket, ObjectInputStream input, ObjectOutputStream output) {
        this.client = client;
        this.socket = socket;
        objectInputStream = input;
        objectOutputStream = output;
    }


    @Override
    public void run() {
        while (true){
            String input = null;
            try {
                input = objectInputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (input){
                case "novo":
                    try {
                        String nome = objectInputStream.readUTF();
                        objectOutputStream.writeInt(ServerControl.newFile(nome,client));
                        objectOutputStream.flush();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null,e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);
                    }
                break;
                case "upload":
                    try {
                            String nome = objectInputStream.readUTF();
                            if(ServerControl.verificaArquivo(nome) == -1) {
                                objectOutputStream.writeInt(-1);
                                objectOutputStream.flush();
                                break;
                            }
                            objectOutputStream.writeInt(1);
                            objectOutputStream.flush();
                            objectOutputStream.flush();
                            String texto = objectInputStream.readUTF();
                            objectOutputStream.write(ServerControl.upload(nome,texto,client));
                            objectOutputStream.flush();

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null,e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);
                    }
                break;
                case "abrir":
                    try {
                        objectOutputStream.writeUTF(ServerControl.listaArquivos());
                        objectOutputStream.flush();
                        objectOutputStream.writeUTF(ServerControl.abrir(objectInputStream.read(),client));
                        objectOutputStream.flush();

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null,e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);
                    }
                break;
                case "texto":
                    try {
                        String texto = objectInputStream.readUTF();
                        objectOutputStream.writeInt(ServerControl.recebeTexto(texto,client));
                        objectOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
