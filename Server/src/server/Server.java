package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server{

    static int tamanio = 500000;
    ArrayList<Integer> valores = new ArrayList<>();
    static int[] listasRecibidas = new int[1];
    static ArrayList<ArrayList<Integer>> listas = new ArrayList<>();

    public static void main(String[] args){
        Server server = new Server();
        ArrayList<Integer> lista = server.generadorNumeros();
        server.guardarArchivo(lista, "numerosGenerados");
        int puerto = 9090;

        ServerSocket servidor = null;
        try{
            servidor = new ServerSocket(puerto);
            int conexion = 0;
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
            int division = (tamanio / 4);
            int[] divisiones = new int[5];
            divisiones[0] = 0;
            for (int j = 1; j < 5; j++){
                divisiones[j] = (division * j) - 1;
            }
            int client = clientes.size();
            ArrayList<Thread> clientesThread = new ArrayList<>();
            for (Socket cliente : clientes){
                ArrayList<Integer> listaEnviar = castListToArray(lista.subList(divisiones[i], (divisiones[i + 1])+1));
                Servidor servicio = new Servidor(cliente, listaEnviar);
                servicio.setServer(server);
                clientesThread.add(0, new Thread(servicio, "Cliente: " + String.valueOf(i)));
                clientesThread.get(0).start();
                i++;
                System.out.println("Cliente: " + i + " enviado");
            }
            //Espera todas las listas
            while (true){
                try{
                    for (Thread thread : clientesThread){
                        thread.join();
                    }
                    break;
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            System.out.println("Listo para comenzar...");
            //A partir de aquí se empieza la mezcla
            ArrayList<Integer> listaPrincipal = new ArrayList<>();
            for (Integer integer : listas.get(0)){
                listaPrincipal.add(integer);
            }
            listas.remove(0);
            //se empiezan a mezclar la principal con las demás
            //aquí pon el merge con hilos
            while (!listas.isEmpty()){
                listaPrincipal = Server.merge(listaPrincipal, listas.get(0));
                listas.remove(0);
            }
            server.guardarArchivo(listaPrincipal, "Nombre");

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

    public static ArrayList castListToArray(List lista){
        ArrayList resultado = new ArrayList<>();
        for (Object object : lista){
            resultado.add(object);
        }
        return resultado;
    }

    public void recibirLista(ArrayList<Integer> lista){
        listas.add(lista);
        listasRecibidas[0] = 1 + listasRecibidas[0];
        System.out.println("Listas Recibidas:" + listasRecibidas[0]);
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
            //Compara para saber cuál es menor
            if (x <= y){
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
