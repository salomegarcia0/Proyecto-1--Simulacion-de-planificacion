/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Politicas_de_Planificacion;
import Estructuras_de_Datos.*;
import Modelado_de_procesos.PCB;
import main.CPU;

/**
 *
 * @author pjroj
 */
public class HRRN {
    
    public void organizarCola(Cola colaListos){
        if (colaListos.isEmpty() || colaListos.getSize() == 1){
            return;
        }
        
        long tiempoActual = CPU.getReloj_global();
        
        Cola colaBackup = new Cola("Backup");
        
        while(!colaListos.isEmpty()){
            PCB procesoMayorRR = encontrarMayorRR(colaListos, tiempoActual);
            if(procesoMayorRR != null){
                colaBackup.enColar(procesoMayorRR);
                
                double rr = calcularRR(procesoMayorRR, tiempoActual);
                long espera = calcularTiempoEspera(procesoMayorRR, tiempoActual);
            }
        }
        
        while(!colaBackup.isEmpty()){
            PCB proceso = colaBackup.desColar();
            colaListos.enColar(proceso);
        }
        
    }
    
    private PCB encontrarMayorRR(Cola cola, long tiempoActual){
        if (cola.isEmpty()){
            return null;
        }
        
        Nodo actual = cola.getHead();
        Nodo anterior = null;
        Nodo nodoMayorRR = null;
        Nodo anteriorMayorRR = null;
        double mayorRR = calcularRR(actual.getProceso(), tiempoActual);
        
        while(actual != null){
            PCB procesoActual = actual.getProceso();
            double rrActual = calcularRR(procesoActual, tiempoActual);
            
            if(rrActual > mayorRR){
                mayorRR = rrActual;
                nodoMayorRR = actual;
                anteriorMayorRR = anterior;
            }
            
            anterior = actual;
            actual = actual.getNext();
        }
        
        return quitarNodo(cola, nodoMayorRR, anteriorMayorRR);
    }
    
    private double calcularRR(PCB proceso, long tiempoActual){
        long tiempoEspera = calcularTiempoEspera(proceso, tiempoActual);
        int tiempoServicio = proceso.getInstruccionesTotal();
        
        if (tiempoServicio == 0) {
            return Double.MAX_VALUE; // evita division por cero
        }
        
        return (double) (tiempoEspera + tiempoServicio) / tiempoServicio;
    }

    private long calcularTiempoEspera(PCB proceso, long tiempoActual) {  
        return tiempoActual - proceso.getTiempoCreacion();
    }
    
    private PCB quitarNodo(Cola cola, Nodo nodoAQuitar, Nodo anterior){
        if (nodoAQuitar == null) {
            return null;
        }
        
        PCB proceso = nodoAQuitar.getProceso();
        
        if (anterior == null) {
            cola.setHead(nodoAQuitar.getNext());
        } else {
            anterior.setNext(nodoAQuitar.getNext());
        }

        if (cola.getTail() == nodoAQuitar) {
            cola.setTail(anterior);
        }

        cola.setSize(cola.getSize() - 1);
        nodoAQuitar.setNext(null);
        
        return proceso;
    }
    
}
