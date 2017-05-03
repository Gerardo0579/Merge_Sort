package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    int tamanio = 100;
    ArrayList<Integer> valores = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
        ArrayList<Integer> lista = server.generadorNumeros();
        int puerto = 9090;

        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(puerto);
            int conexion = 0;
            ArrayList<String> numerosOrdenadosList = new ArrayList<>();
            while (true) {
                //Esperar conexiones nuevas
                System.out.println("Esperando conexiones");
                //Aceptada la conexion.
                Socket cliente = servidor.accept();
                //Creación de buffers de salida y entrada                
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                
                //Recibo un mensaje para saber si me conecté. Si lo quitas, quizá algo salga mal porque el cliente tardará en ponerse a escuchar
                String mensajeConexion = in.readLine();
                System.out.println(mensajeConexion);
                
                //Envio los números al cliente
                for (int i = 0; i < 100; i++) {
                    out.println(String.valueOf(lista.indexOf(i)));
                }

                //Recibo los número ordenados del cliente
                for (int i = 0; i < 100; i++) {
                    String numerosOrdenados = in.readLine();
                    numerosOrdenadosList.add(numerosOrdenados);
                }
                //Muestro los números de la lista que recibí
                for (String string : numerosOrdenadosList) {
                    System.out.println(string);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                servidor.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static void run(ServerSocket servidor) {
        System.out.println("Esperando conexiones");
        Socket sock = null;
        try {
            sock = servidor.accept();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void slitList() {
        for (Integer valore : valores) {
            System.out.print(valore.toString() + " ");
        }
        System.out.println("");
        ArrayList<Integer> result = Server.merge(valores, valores);
        for (Integer integer : result) {
            System.out.println("Integer: " + integer.toString());
        }
    }

    public ArrayList<Integer> generadorNumeros() {
        for (int i = 0; i < tamanio; i++) {
            Random rand = new Random();
            valores.add(rand.nextInt(tamanio));
        }
        return valores;
    }

    public static <E extends Comparable<? super E>> ArrayList<Integer> merge(ArrayList<Integer> iteradorIzquierdo, ArrayList<Integer> iteradorDerecho) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> it1 = iteradorIzquierdo.iterator();
        Iterator<Integer> it2 = iteradorDerecho.iterator();

        Integer x = it1.next();
        Integer y = it2.next();
        while (true) {
            if (x.compareTo(y) <= 0) {
                result.add(x);
                if (it1.hasNext()) {
                    x = it1.next();
                } else {
                    result.add(y);
                    while (it2.hasNext()) {
                        result.add(it2.next());
                    }
                    break;
                }
            } else {
                result.add(y);
                if (it2.hasNext()) {
                    y = it2.next();
                } else {
                    result.add(x);
                    while (it1.hasNext()) {
                        result.add(it1.next());
                    }
                    break;
                }
            }
        }
        return result;
    }

}
