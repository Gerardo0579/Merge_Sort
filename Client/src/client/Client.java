package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    private static final int NUMERO_HILOS = 16;
    private static int CANTIDAD_NUMEROS;

    public static void main(String[] args){

        String ip = "localhost";
        int puerto = 9090;
        Socket sock = null;
        try{
            //Inicio el socket y los buffers de entrada y salida
            sock = new Socket(ip, puerto);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

            //Preparo la ruta el envío un mensaje diciéndole que pude conectarme
            String mensajeDeConexion = "Computadora conectándose";
            out.println(mensajeDeConexion);
            System.out.println("Esperando a los demás clientes");
            String result = in.readLine();
            int cantidadNumeros = Integer.valueOf(result);
            setCANTIDAD_NUMEROS(cantidadNumeros);
            //Preparo la variable que ayudará a guardarlos números acomodados
            Scanner renglon = new Scanner(System.in);

            //Preparo la ruta dónde se guardará
            String ruta = "C:\\Users\\DARKENSES\\Desktop\\numerosOrdenados.txt";
            File archivo = new File(ruta);
            BufferedWriter escritor = null;
            try{
                escritor = new BufferedWriter(new FileWriter(archivo));
            }catch (Exception ex){

            }
            
            //Subarreglo dónde guardaré los número que recibo
            int[] subArreglo = new int[CANTIDAD_NUMEROS];

            //Me conecto y recibo los número
            for (int i = 0; i < CANTIDAD_NUMEROS; i++){
                String mensajeServidor = in.readLine();
                //Agrego el número al arreglo de números
                subArreglo[i] = Integer.parseInt(mensajeServidor);
            }
            System.out.println("Ordenando Numeros...");
            //Aquí ocurre la magia ;)
            int[] numerosOrdenados = MergeSort.parallelMergeSort(subArreglo, NUMERO_HILOS);

            //Aquí sólo escribo en el documento cada línea ;)
            for (Integer numero : numerosOrdenados){
                escritor.write(numero.toString());
                escritor.newLine();
                escritor.flush();
            }
            //Cierro el archivo con los números
            escritor.close();

            //Envío de regreso los números al servidor
            for (Integer numero : numerosOrdenados){
                out.println(String.valueOf(numero));
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                sock.close();
            }catch (IOException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void setCANTIDAD_NUMEROS(int aCANTIDAD_NUMEROS){
        CANTIDAD_NUMEROS = aCANTIDAD_NUMEROS;
    }

}
