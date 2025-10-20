/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Politicas_de_Planificacion;
import Estructuras_de_Datos.*;

/**
 *
 * @author pjroj
 */
public class FCFS {
    private Cola procesosFCFS;

    //crea una cola llamada procesosFCFS
    public FCFS() {
        this.procesosFCFS = new Cola();
    }

    public Cola getProcesosFCFS() {
        return procesosFCFS;
    }
    
    public void planificacionFCFS(Cola cola_procesos){
        Nodo pointer = cola_procesos.getHead();
        //para tener un pointer del proceso anterior
        Nodo pointerAnterior = cola_procesos.getHead();
        long tiempoFinalizacion;
        long tiempoComienzo;
        System.out.println("Politicas de Planificacion FCFS");
        while (pointer.getNext() != null){
            if (pointer == cola_procesos.getHead()){
                //Obtiene el tiempo que dice que requiere el proceso para completarse que sera, para el primer proceso, el tiempo de servicio
                tiempoFinalizacion = pointer.getProceso().getTiempoServicio();
                //Obtiene el tiempo en que comenzo el proceso que sera, para el primer proceso, el tiempo de llegada
                tiempoComienzo = pointer.getProceso().getTiempoCreacion();
            } else {
                //Obtiene el tiempo en que comenzara el proceso, que eso sera cuando el proceso anterior finalice
                //Tiempo Comienzo = Tiempo Finalizacion del proceso anterior
                tiempoComienzo = pointerAnterior.getProceso().getTiempoFinalizacion();
                //Obtiene el tiempo que requiere el proceso para completarse y a eso se le suma el tiempo que tarda el proceso
                //anterior en finaliza, es decir: 
                //Tiempo Finalizacion = Tiempo de servicio del proceso actual + Tiempo de finalizacion del proceso anterior
                tiempoFinalizacion = pointer.getProceso().getTiempoServicio() + pointerAnterior.getProceso().getTiempoFinalizacion(); 
                //se pide el siente pointerAnterior que será este
                pointerAnterior = pointerAnterior.getNext();
            }
            pointer.getProceso().setTiempoFinalizacion(tiempoFinalizacion);   
            pointer.getProceso().setTiempoInicioEjecucion(tiempoComienzo);
            pointer.getProceso().calculoTiempos();
            
            pointer = pointer.getNext();
        }
        
        cola_procesos.printFCFS();
    }

}
