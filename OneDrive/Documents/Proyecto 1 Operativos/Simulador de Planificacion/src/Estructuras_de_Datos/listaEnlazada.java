/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;

/**
 *
 * @author salom
 */
public class listaEnlazada {
    private Nodo primero;
    private int tamano;

    public listaEnlazada(Nodo primero, int tamano) {
        this.primero = null;
        this.tamano = 0;
    }
    
    public int tamano(){
        return tamano;
    }
    
    public boolean isEmpty(){
        return primero == null;
    }
}
