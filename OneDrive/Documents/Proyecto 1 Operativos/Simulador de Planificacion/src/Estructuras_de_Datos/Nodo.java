/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;

/**
 *
 * @author salom
 */
public class Nodo {

    private Nodo next;
    private Object elemento;

    public Nodo(Object elemento) {
        this.next = null;
        this.elemento = elemento;
    }

    public Nodo getNext() {
        return next;
    }

    public void setNext(Nodo next) {
        this.next = next;
    }

    public Object getElemento() {
        return elemento;
    }

    public void setElemento(Object elemento) {
        this.elemento = elemento;
    }
    
}
