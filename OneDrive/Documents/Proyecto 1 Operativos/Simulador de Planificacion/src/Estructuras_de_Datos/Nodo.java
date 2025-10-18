/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;
import Modelado_de_procesos.PCB;
/**
 * Clase para crear nodos de tipo PCB
 * @author salom
 */
public class Nodo {

    private Nodo next;
    private PCB proceso;

    public Nodo(PCB proceso) {
        this.next = null;
        this.proceso = proceso;
    }

    public Nodo getNext() {
        return next;
    }

    public void setNext(Nodo next) {
        this.next = next;
    }

    public PCB getProceso() {
        return proceso;
    }

    public void setProceso(PCB proceso) {
        this.proceso = proceso;
    }
    
}
