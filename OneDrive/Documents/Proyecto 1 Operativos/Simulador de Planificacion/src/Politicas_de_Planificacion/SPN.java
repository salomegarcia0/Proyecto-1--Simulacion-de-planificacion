/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Politicas_de_Planificacion;

import Estructuras_de_Datos.Cola;
import Estructuras_de_Datos.Nodo;
import Modelado_de_procesos.PCB;
import main.CPU;

/**
 *
 * @author salom
 */
public class SPN {
    


    public void organizarCola(Cola colaListos) {
        if (colaListos.isEmpty() || colaListos.getSize() == 1) {
            System.out.println("Cola vacia o con un solo elemento, no hay nada para organizar ");
            return;
        }

        colaListos.print2(); // mostrar cola de listos desordenada
        Cola colaAuxiliar = new Cola("Auxiliar SPN"); // crear cola auxiliar

        while (!colaListos.isEmpty()) {    // mientras haya procesos en la cola original

            PCB procesoMasCorto = encontrarYRemoverMasCorto(colaListos); // encontrar el proceso mas corto 
            if(procesoMasCorto != null){
                colaAuxiliar.enColar(procesoMasCorto);// agregarlo a la cola auxiliar
            } else {
                break;
            }
        }

        while (!colaAuxiliar.isEmpty()) {// transfiere todos los procesos ordenado ala cola listos
            PCB proceso = colaAuxiliar.desColar();
            colaListos.enColar(proceso);
        }
        CPU.setColaNuevos(colaListos);
        System.out.println("SPN, cola organizada exitosamente");
        colaListos.print2();

    }

    private PCB encontrarYRemoverMasCorto(Cola cola) {
        if (cola.isEmpty()) {
            return null;
        }

        Nodo actual = cola.getHead();
        Nodo anterior = null;
        Nodo nodoMasCorto = actual;
        Nodo anteriorMasCorto = null;
        int minInstrucciones = actual.getProceso().getInstruccionesTotal();

        while (actual != null) {// busca el proceso con menos instrucciones
            PCB procesoActual = actual.getProceso();
            int instruccionesActual = procesoActual.getInstruccionesTotal();

            if (instruccionesActual < minInstrucciones) {
                minInstrucciones = instruccionesActual;
                nodoMasCorto = actual;
                anteriorMasCorto = anterior;
            }

            anterior = actual;
            actual = actual.getNext();
        }

        return removerNodo(cola, nodoMasCorto, anteriorMasCorto);// remover y devolver el proceso mas corto
    }

    private PCB removerNodo(Cola cola, Nodo nodoARemover, Nodo anterior) {
        if (nodoARemover == null) {
            return null;
        }

        PCB proceso = nodoARemover.getProceso();

        if (anterior == null) {
            cola.setHead(nodoARemover.getNext());// Es el primer nodo (cabeza)
        } else {
            anterior.setNext(nodoARemover.getNext());
        }

        if (cola.getTail() == nodoARemover) {    // actualiza tail 
            cola.setTail(anterior);
        }

        cola.setSize(cola.getSize() - 1);// actualizar tamaño

        nodoARemover.setNext(null);// limpiar 

        return proceso;
    }
}
