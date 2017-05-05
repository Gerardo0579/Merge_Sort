package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class Servidor implements Runnable{
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<Integer> lista;
    private Socket cliente;
    private Server server;

    public Servidor(Socket cliente, ArrayList<Integer> lista,Server server){
        this.cliente = cliente;
        this.lista = lista;
        this.server = server;
        try{
            this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            this.out = new PrintWriter(cliente.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        //Recibo un mensaje para saber si me conecté. Si lo quitas, quizá algo salga mal porque el cliente tardará en ponerse a escuchar
        String mensajeConexion;
        ArrayList<Integer> numerosOrdenadosList = new ArrayList<>();
        try{
            mensajeConexion = in.readLine();
            System.out.println(mensajeConexion);
            //Envio los números al cliente
            for (int i = 0; i < 100; i++){
                out.println(String.valueOf(lista.get(i)));
            }
            
            //Recibo los número ordenados del cliente
            for (int i = 0; i < 100; i++){
                String numerosOrdenados = in.readLine();
                numerosOrdenadosList.add(Integer.getInteger(numerosOrdenados));
            }
            System.out.println("Todos los datos recibidos...");
            server.recibirLista(numerosOrdenadosList);
        }catch (IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
