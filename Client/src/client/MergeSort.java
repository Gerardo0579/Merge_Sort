/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.Arrays;

/**
 *
 * @author gerar
 */
public class MergeSort{


    public static int[] parallelMergeSort(int[] arregloNumeros, int numeroHilos) {
        //Sólo si el número de hilos que le paso es menor a 1, siempre llegará a este punto pues los hilos se dividen a la mitad
        if (numeroHilos <= 1) {
            mergeSort(arregloNumeros);
            return arregloNumeros;
        }

        //Encuentra la mitad del arreglo
        int mid = arregloNumeros.length / 2;

        //Crea las dos sublistas
        int[] left = Arrays.copyOfRange(arregloNumeros, 0, mid);
        int[] right = Arrays.copyOfRange(arregloNumeros, mid, arregloNumeros.length);

        //Acomoda las sublistas en hilos
        Thread arregloIzquierda = mergeSortThread(left, numeroHilos);
        Thread arregloDerecha = mergeSortThread(right, numeroHilos);

        //Corre las sublistas en hilos
        arregloIzquierda.start();
        arregloDerecha.start();

        try {
            //Une los hilos cuando estos han terminado su ejecución
            arregloIzquierda.join();
            arregloDerecha.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Termina de hacer el merge con lo sobrante
        merge(left, right, arregloNumeros);
        
        return arregloNumeros;
    }

    private static Thread mergeSortThread(int[] subLista, int numeroHilos) {
        return new Thread() {
            @Override
            public void run() {
                parallelMergeSort(subLista, numeroHilos / 2);
            }
        };
    }

    public static void mergeSort(int[] arregloNumeros) {
        //por si sólo hay un elemento
        if (arregloNumeros.length <= 1) {
            return;
        }

        int mid = arregloNumeros.length / 2;

        int[] left = Arrays.copyOfRange(arregloNumeros, 0, mid);
        int[] right = Arrays.copyOfRange(arregloNumeros, mid, arregloNumeros.length);

        mergeSort(left);
        mergeSort(right);

        merge(left, right, arregloNumeros);
    }

    private static void merge(int[] a, int[] b, int[] r) {
        int i = 0, j = 0, k = 0;
        //ordena al menor
        while (i < a.length && j < b.length) {
            if (a[i] < b[j]) {
                r[k++] = a[i++];
            } else {
                r[k++] = b[j++];
            }
        }

        //por si sobra la lista izquierda solamente
        while (i < a.length) {
            r[k++] = a[i++];
        }

        //por si sobra la lista derecha solamente
        while (j < b.length) {
            r[k++] = b[j++];
        }
    }

}
