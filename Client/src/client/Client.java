package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        String ip = "localhost";
        int puerto = 9090;
        Socket sock = null;
        try {
            //Inicio el socket y los buffers de entrada y salida
            sock = new Socket(ip, puerto);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

            //Preparo la ruta el envío un mensaje diciéndole que pude conectarme
            String mensajeDeConexion = "Computadora conectándose";
            out.println(mensajeDeConexion);

            //Preparo la variable que ayudará a guardarlos números acomodados
            Scanner renglon = new Scanner(System.in);

            //Preparo la ruta dónde se guardará
            String ruta = "C:\\Users\\gerar\\Desktop\\numerosOrdenados.txt";
            File archivo = new File(ruta);
            BufferedWriter escritor = null;
            try {
                escritor = new BufferedWriter(new FileWriter(archivo));
            } catch (Exception ex) {

            }
            //Sublista dónde guardaré los número que recibo
            ArrayList<Integer> subLista = new ArrayList<>();

            //Me conecto y recibo los número
            for (int i = 0; i < 100; i++) {
                String mensajeServidor = in.readLine();
                //Agrego el número a la lista de integers
                subLista.add(Integer.valueOf(mensajeServidor));
            }
            //El mensaje recibido no viene en ArrayList :/... debo hacerlo ArrayList<Integer> ;)
            //Aquí ocurre la magia ;)
            ArrayList<Integer> numerosOrdenados = new ArrayList<>();

            ArrayList<Integer> listaRes = mergeSort(subLista);
            for (Integer listaRe : listaRes) {
                numerosOrdenados.add(listaRe);
            }

            //Aquí sólo escribo en el documento cada línea ;)
            for (Integer numero : numerosOrdenados) {
                escritor.write(numero.toString());
                escritor.newLine();
                escritor.flush();
            }
            //Cierro el archivo con los números
            escritor.close();

            //Envío de regreso los números al servidor
            for (Integer numero : numerosOrdenados) {
                out.println(String.valueOf(numero));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sock.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Integer> merge(ArrayList<Integer> izquierda, ArrayList<Integer> derecha) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> it1 = izquierda.iterator();
        Iterator<Integer> it2 = derecha.iterator();

        Integer x = it1.next();
        Integer y = it2.next();
        while (true) {
            //Compara para saber cuál es menor
            if (x <= y) {
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

    public static ArrayList<Integer> mergeSort(ArrayList<Integer> subLista) {
        if (subLista.size() <= 1) {
            return subLista;
        }

        int middle = subLista.size() / 2;
        List<Integer> leftList = (List<Integer>) subLista.subList(0, middle);
        List<Integer> rightList = (List<Integer>) subLista.subList(middle, subLista.size());
        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();
        for (Integer integer : leftList) {
            left.add(integer);
        }
        for (Integer integer : rightList) {
            right.add(integer);
        }

        right = mergeSort(right);
        left = mergeSort(left);
        ArrayList<Integer> result = merge(left, right);

        return result;
    }
}
