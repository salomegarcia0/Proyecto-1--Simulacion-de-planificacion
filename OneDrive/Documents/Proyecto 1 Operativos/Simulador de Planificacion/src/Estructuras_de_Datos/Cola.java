/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;
import Modelado_de_procesos.PCB;
/**
 * Clase para crear colas para organizar los procesos
 * @author salom
 */
public class Cola {
    private Nodo head, tail;
    private int size;
    
    public Cola() {
        this.head = this.tail = null;
        size = 0;
    }

    public Nodo getHead() {
        return head;
    }

    public void setHead(Nodo head) {
        this.head = head;
    }

    public Nodo getTail() {
        return tail;
    }

    public void setTail(Nodo tail) {
        this.tail = tail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    // Funcion para agregar un proceso a la cola
    public void enColar(PCB proceso) {
        Nodo nodo = new Nodo(proceso);
        if (isEmpty()) {
            setHead(nodo);
            setTail(nodo);
        } else {
            getTail().setNext(nodo);
            setTail(nodo);
        }
        size++;
    }

    // Funcion para eliminar un proceso de la cola
    public void desColar() {
        if (isEmpty()) {
            System.out.println("La lista esta vacia");
        } else {
            Nodo pointer = getHead();
            setHead(pointer.getNext());
            pointer.setNext(null);
            size--;
        }
    }
    
    public void organiceCola(PCB proceso){
        if (isEmpty()) {
            enColar(proceso);
        } else {
            System.out.println("CONTINUAR AQUI");
        }
    }

    public boolean isEmpty() {
        return getHead() == null && getTail() == null;
    }
    
    public void print() {
        Nodo pointer = getHead();
        while (pointer != null) {
            System.out.println("[ "+ pointer.getProceso().getProcesoID() + " | " + pointer.getProceso().getProcesoNombre() + " | " + " ]");
            pointer = pointer.getNext();
        }
    }
}
