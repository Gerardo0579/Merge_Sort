package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server{

    int tamanio = 100;
    ArrayList<Integer> valores = new ArrayList<>();
    int listasRecibidas = 0;
    ArrayList<ArrayList<Integer>> listas = new ArrayList<>();

    public static void main(String[] args){
        Server server = new Server();
        ArrayList<Integer> lista = server.generadorNumeros();
        server.guardarArchivo(lista, "numerosGenerados");
        int puerto = 9090;

        ServerSocket servidor = null;
        try{
            servidor = new ServerSocket(puerto);
            int conexion = 0;
            ArrayList<String> numerosOrdenadosList = new ArrayList<>();
            ArrayList<Socket> clientes = new ArrayList<>();
            while (clientes.size() < 4){
                //Esperar conexiones nuevas
                System.out.println("Esperando conexiones");
                //Aceptada la conexion.
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.toString());
                clientes.add(cliente);
            }
            int i = 0;
            for (Socket cliente : clientes){
                Servidor servicio = new Servidor(cliente, lista,this);
                new Thread(servicio, "Cliente: " + String.valueOf(i)).start();
            }
            while (listas.size() != 4){
            }
            
            while(listas.size() != 2){
                listas.get(0) = Server.merge(listas.get(0),listas.get(1));
                listas.remove(1);
            }
            listas.get(0) = Server.merge(listas.get(0),listas.get(1));
            server.guardarArchivo(listas.get(i), nombre);
        }catch (IOException ex){
            ex.printStackTrace();
        }finally{
            try{
                servidor.close();
            }catch (IOException ex){
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void recibirLista(ArrayList<Integer> lista){
        listas.add(lista);
        listasRecibidas++;
    }

    private ArrayList<Integer> generadorNumeros(){
        for (int i = 0; i < tamanio; i++){
            Random rand = new Random();
            valores.add(rand.nextInt(tamanio));
        }
        return valores;
    }

    public void guardarArchivo(ArrayList<Integer> numerosOrdenados, String nombre){
        String ruta = "C:\\Users\\DARKENSES\\Desktop\\" + nombre + ".txt";
        File archivo = new File(ruta);
        BufferedWriter escritor = null;
        try{
            escritor = new BufferedWriter(new FileWriter(archivo));
        }catch (Exception ex){

        }
        for (Integer numero : numerosOrdenados){
            try{
                escritor.write(numero.toString());
                escritor.newLine();
                escritor.flush();
            }catch (IOException e){

            }
        }
    }

    public static ArrayList<Integer> merge(ArrayList<Integer> listaPrincipal, ArrayList<Integer> listaSecundaria){
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> it1 = listaPrincipal.iterator();
        Iterator<Integer> it2 = listaSecundaria.iterator();

        Integer x = it1.next();
        Integer y = it2.next();
        while (true){
            if (x.compareTo(y) <= 0){
                result.add(x);
                if (it1.hasNext()){
                    x = it1.next();
                }else{
                    result.add(y);
                    while (it2.hasNext()){
                        result.add(it2.next());
                    }
                    break;
                }
            }else{
                result.add(y);
                if (it2.hasNext()){
                    y = it2.next();
                }else{
                    result.add(x);
                    while (it1.hasNext()){
                        result.add(it1.next());
                    }
                    break;
                }
            }
        }
        return result;
    }

}
