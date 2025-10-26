/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;
import Modelado_de_procesos.*;
/**
 * Clase para crear colas para organizar los procesos
 * @author salom
 */
public class Cola {
    private Nodo head, tail;
    private int size;
    private String nombre;
    
    public Cola(){
        this.head = this.tail = null;
        size = 0;
    }
    
    public Cola(String nombre) {
        this.head = this.tail = null;
        size = 0;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    public PCB desColar() {
        if (isEmpty()) {
            System.out.println("La lista esta vacia");
            return null;
        } else {
            Nodo pointer = getHead();
            PCB proceso = pointer.getProceso();
            if(pointer != getTail()){
                setHead(pointer.getNext());
                pointer.setNext(null);
                size--;
            }else {
                setHead(null);
                pointer.setNext(null);
                setTail(null);
                size--;
            }
            
            return proceso;
        }
    }
    
    // Encolar en orden según tiempo de espera
    public void enColarOrdenadoSRN(PCB proceso) {
        Nodo nuevo = new Nodo(proceso);

        // Caso 1: cola vacía
        if (isEmpty()) {
            setHead(nuevo);
            setTail(nuevo);
        } 
        // Caso 2: insertar al inicio (si es menor que el head actual)
        else if (proceso.getTiempoRestante() < head.getProceso().getTiempoRestante()) {
            nuevo.setNext(head);
            setHead(nuevo);
        } 
        // Caso 3: buscar la posición correcta en medio o al final
        else {
            Nodo actual = head;
            while (actual.getNext() != null &&
                   actual.getNext().getProceso().getTiempoRestante() <= proceso.getTiempoRestante()) {
                actual = actual.getNext();
            }

            nuevo.setNext(actual.getNext());
            actual.setNext(nuevo);

            // Si lo insertamos al final, actualizar tail
            if (nuevo.getNext() == null) {
                setTail(nuevo);
            }
        }

        size++;
    }    


    public boolean isEmpty() {
        return getHead() == null && getTail() == null;
    }
    
    public void print() {
        Nodo pointer = getHead();
        while (pointer != null) {
            String tproceso;
            TipoProceso tipo = pointer.getProceso().getTipo();
            if (tipo == TipoProceso.IO_BOUND){
                tproceso = "IO_BOUND";
            }else{
                tproceso = "CPU_BOUND";
            } 
            System.out.println("[ Id: "+ pointer.getProceso().getProcesoID() + " | Nombre: " + pointer.getProceso().getProcesoNombre() + " | #Instrucciones: " + pointer.getProceso().getInstruccionesTotal() + 
                    " | TipoProceso: " + tproceso + " | TiempoLlegada: " + pointer.getProceso().getTiempoCreacion() + " | TiempoServicio: " + pointer.getProceso().getTiempoServicio() + 
                    " | MemoriaRequerida: " + pointer.getProceso().getMemoria() +" ]");
            pointer = pointer.getNext();
        }
        if(isEmpty()){
            System.out.println("vacio");
        }
    }
    
    public void print2() {
        Nodo pointer = getHead();
        while (pointer != null) {
            String tproceso;
            TipoProceso tipo = pointer.getProceso().getTipo();
            if (tipo == TipoProceso.IO_BOUND){
                tproceso = "IO_BOUND";
            }else{
                tproceso = "CPU_BOUND";
            } 
            System.out.println("[ Id: "+ pointer.getProceso().getProcesoID() + " | Nombre: " + pointer.getProceso().getProcesoNombre()); 
            pointer = pointer.getNext();
        }
        if(isEmpty()){
            System.out.println("vacio");
        }
    }
    
    public void printFCFS() {
        Nodo pointer = getHead();
        while (pointer != null) {
            String tproceso;
            TipoProceso tipo = pointer.getProceso().getTipo();
            if (tipo == TipoProceso.IO_BOUND){
                tproceso = "IO_BOUND";
            }else{
                tproceso = "CPU_BOUND";
            } 
            System.out.println("[ Id: "+ pointer.getProceso().getProcesoID() + " | Nombre: " + pointer.getProceso().getProcesoNombre() +
                " | TiempoLlegada: " + pointer.getProceso().getTiempoCreacion() + "| TiempoServicio: " + pointer.getProceso().getTiempoServicio() +
                " | TiempoComienzo: " + pointer.getProceso().getTiempoInicioEjecucion() + "| TiempoFinalizacion: " + pointer.getProceso().getTiempoFinalizacion() +
                " | TiempoEstancia: " + pointer.getProceso().getTAT() + " | TiempoEstanciaNomalizada: " + pointer.getProceso().getTiempoEstanciaNormalizado() + " ]");
            pointer = pointer.getNext();
        }
        if(isEmpty()){
            System.out.println("vacio");
        }
    }
}
