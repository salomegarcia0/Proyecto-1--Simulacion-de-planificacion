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
public class HRRN {
    

    
    private double calcular(PCB proceso, long tiempoActual){
        long tiempoEspera = calcularTiempoEspera(proceso, tiempoActual);// calcula Response Ratio = (TiempoEspera + TiempoServicio) / TiempoServicio
        int tiempoServicio = proceso.getInstruccionesTotal();
        
        if (tiempoServicio == 0) {
            return Double.MAX_VALUE; // Evitar división por cero
        }
        
        return (double) (tiempoEspera + tiempoServicio) / tiempoServicio;
    }
    
    private long calcularTiempoEspera(PCB proceso, long tiempoActual) {
        return tiempoActual - proceso.getTiempoCreacion();
    }
    
    
    private PCB encontrarProcesoConMasTiempo(Cola cola, long tiempoActual){
        if (cola.isEmpty()){
            return null;
        }
        
        Nodo actual = cola.getHead();
        Nodo anterior = null;
        Nodo nodoConMasTiempo = actual;
        Nodo anteriorConMasTiempo = null;
        double procesoConMasTiempo = calcular(actual.getProceso(), tiempoActual);
        
        while (actual != null){
            PCB procesoActual = actual.getProceso();
            double hrrnActual = calcular(procesoActual, tiempoActual);
            
            if (hrrnActual > procesoConMasTiempo){
                procesoConMasTiempo= hrrnActual;
                nodoConMasTiempo = actual;
                anteriorConMasTiempo = anterior;
            }
            anterior = actual;
            actual = actual.getNext();
        }
        return null;
    }
    
    public void organizarCola(Cola colaListos){
        if (colaListos.isEmpty() || colaListos.getSize() == 1){
            return;
        }
        
        long tiempoActual = CPU.getReloj_global();
        
        Cola colaAuxiliar = new Cola("HRRN");
        
        while (!colaListos.isEmpty()){
            PCB procesoConMasTiempo = encontrarProcesoConMasTiempo(colaListos, tiempoActual);
            
        }
    }
}
