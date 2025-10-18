/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;
import Modelado_de_procesos.EstadoProceso;
import Modelado_de_procesos.PCB;

/**
 *
 * @author salom
 */
public class gestorColas {
    private Cola colaListos;
    private Cola colaBloqueados;
    private Cola colaListosSuspendidos;
    private Cola colaBloqueadosSuspendidos;
    private Cola colaNuevos;
    
    private PCB procesoEnEjecucion;

    public gestorColas() {
        this.colaListos = new Cola("Listos"); // cola corto plazo
        this.colaBloqueados = new Cola("Bloqueados");
        this.colaListosSuspendidos = new Cola("Listos Suspendidos"); //mediano plazo
        this.colaBloqueadosSuspendidos = new Cola("Bloqueados Suspendidos"); // mediano plazo
        this.colaNuevos = new Cola("Nuevos (Cola Largo Plazo)"); 
        this.procesoEnEjecucion = null;
    }
    
    public void agregarProcesoNuevo(PCB proceso){
        proceso.setEstadoActual(EstadoProceso.NUEVO);
        colaNuevos.enColar(proceso);
    }
    
    public void moverNuevoAListo(PCB proceso){
        proceso.setEstadoActual(EstadoProceso.LISTO);
        colaListos.enColar(proceso);
    }
    
    public void moverBloqueadoAListo(PCB proceso){
        proceso.setEstadoActual(EstadoProceso.LISTO);
        colaListos.enColar(proceso);
    }
    
    
    
}
