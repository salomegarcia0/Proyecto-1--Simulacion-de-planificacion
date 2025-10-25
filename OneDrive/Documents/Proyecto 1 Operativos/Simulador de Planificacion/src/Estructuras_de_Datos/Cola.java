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
    
    public void organiceCola(PCB proceso){
        if (isEmpty()) {
            enColar(proceso);
        } else {
            System.out.println("CONTINUAR AQUI");
        }
    }

    public boolean isEmpty() {
        return getSize() == 0;//getHead() == null && getTail() == null;
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
    }
}
