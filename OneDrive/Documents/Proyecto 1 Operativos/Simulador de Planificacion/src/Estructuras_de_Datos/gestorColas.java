/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;
import Modelado_de_procesos.EstadoProceso;
import Modelado_de_procesos.GestorMemoria;
import Modelado_de_procesos.PCB;
import main.CPU;

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
    private GestorMemoria gestorMemoria;

    public gestorColas() {
        this.colaListos = new Cola("Listos"); // cola corto plazo
        this.colaBloqueados = new Cola("Bloqueados");
        this.colaListosSuspendidos = new Cola("Listos Suspendidos"); //mediano plazo
        this.colaBloqueadosSuspendidos = new Cola("Bloqueados Suspendidos"); // mediano plazo
        this.colaNuevos = new Cola("Nuevos (Cola Largo Plazo)"); 
        this.procesoEnEjecucion = null;
        this.gestorMemoria = new GestorMemoria();
    }
    
    public void agregarProcesoNuevo(PCB proceso){  // ESTO ES SOLO PARA AGREGAR PROCESOS A LA COLA DE NUEVOS
        proceso.setEstadoActual(EstadoProceso.NUEVO);
        colaNuevos.enColar(proceso);
    }
    
    public PCB seleccionarProceso(){
        if(!colaListos.isEmpty()){
           this.procesoEnEjecucion = colaListos.desColar();
           procesoEnEjecucion.setEstadoActual(EstadoProceso.EJECUTANDO);
           return procesoEnEjecucion;
        }
        return null;
    }
    
    public void moverEjecutandoABloqueado(PCB proceso){ // esto es para mover el proceso a bloqueados pero hay que ver lo de las interrpciones
        proceso.setEstadoActual(EstadoProceso.BLOQUEADO);
        colaBloqueados.enColar(proceso);
        this.procesoEnEjecucion = null;
    }
    
    public void moverBloqueadoAListo(PCB proceso){ 
        proceso.setEstadoActual(EstadoProceso.LISTO);
        colaListos.enColar(proceso);
    }
    
    /**
     * Swapping FIFO para supender procesos cuando se llene la memoria
     * @param memoria
     * @return 
     */
    public boolean suspenderProceso(int memoria){
        int memoriaLiberada = 0;
        
        while(!colaBloqueados.isEmpty() && memoriaLiberada < memoria){
            PCB proceso = colaBloqueados.desColar();
            proceso.suspender();
            gestorMemoria.limpiarMemoria(proceso);
            memoriaLiberada += proceso.getMemoria();
            colaBloqueadosSuspendidos.enColar(proceso);
            System.out.println(proceso.getProcesoNombre() + "ha sido sacado de MP y se libero" + proceso.getMemoria()); //borrar solo es parar verificacion de que funciona
        }
        
        while(!colaListos.isEmpty() && memoriaLiberada < memoria){
            PCB proceso = colaListos.desColar();
            
            if(proceso != procesoEnEjecucion){
                proceso.suspender();
                gestorMemoria.limpiarMemoria(proceso);
                memoriaLiberada += proceso.getMemoria();
                colaListosSuspendidos.enColar(proceso);
                System.out.println(proceso.getProcesoNombre() + "ha sido sacado de MP y se libero" + proceso.getMemoria()); // borrar solo es para verificacion de que funciona
            }
        }
        return memoriaLiberada >= memoria;
    }
    
    /**
     * admite procesos en la cola de listos pero valida la memoria para saber si hay que hacer swapping o no
     */
    public void admitirProceso(){
        while(!colaNuevos.isEmpty()){
            PCB proceso = colaNuevos.getHead().getProceso();
            
            if(gestorMemoria.puedeEntrarAMemoria(proceso)){
                proceso = colaNuevos.desColar();
                proceso.setEstadoActual(EstadoProceso.LISTO);
                gestorMemoria.asignarMemoria(proceso);
                colaListos.enColar(proceso);
                
                System.out.println(proceso.getProcesoNombre() + "fue admitido y esta en MP"); //borrar, solo para verificacion de que sirve
            } else{
                
                System.out.println("No hay memoria suficiente");
                if(suspenderProceso(proceso.getMemoria())){
                    System.out.println("reintentando"); // reintentando 
                }else{
                    break;
                }
            }
        }
    }
        
    public void reanudarProceso(){
        while(!colaListosSuspendidos.isEmpty()){
            PCB proceso = colaListosSuspendidos.getHead().getProceso();
            
            if (gestorMemoria.puedeEntrarAMemoria(proceso)){
                proceso = colaListosSuspendidos.desColar();
                proceso.reanudar();
                gestorMemoria.asignarMemoria(proceso);
                colaListos.enColar(proceso);
                System.out.println(proceso.getProcesoNombre() + "reanudado"); //verificacion
            }else{
                break;
            }
        }
        
        while(!colaBloqueadosSuspendidos.isEmpty()){
            PCB proceso = colaBloqueadosSuspendidos.getHead().getProceso();
            
            if (gestorMemoria.puedeEntrarAMemoria(proceso)){
                proceso = colaBloqueadosSuspendidos.desColar();
                proceso.reanudar();
                gestorMemoria.asignarMemoria(proceso);
                colaListos.enColar(proceso);
                System.out.println(proceso.getProcesoNombre() + "reanudado");  //verificacion
            }
        }
    }
    
    public Cola getColaListos() {
        return colaListos;
    }

    public void setColaListos(Cola colaListos) {
        this.colaListos = colaListos;
    }

    public Cola getColaBloqueados() {
        return colaBloqueados;
    }

    public void setColaBloqueados(Cola colaBloqueados) {
        this.colaBloqueados = colaBloqueados;
    }

    public Cola getColaListosSuspendidos() {
        return colaListosSuspendidos;
    }

    public void setColaListosSuspendidos(Cola colaListosSuspendidos) {
        this.colaListosSuspendidos = colaListosSuspendidos;
    }

    public Cola getColaBloqueadosSuspendidos() {
        return colaBloqueadosSuspendidos;
    }

    public void setColaBloqueadosSuspendidos(Cola colaBloqueadosSuspendidos) {
        this.colaBloqueadosSuspendidos = colaBloqueadosSuspendidos;
    }

    public Cola getColaNuevos() {
        return colaNuevos;
    }

    public void setColaNuevos(Cola colaNuevos) {
        this.colaNuevos = colaNuevos;
    }

    public PCB getProcesoEnEjecucion() {
        return procesoEnEjecucion;
    }

    public void setProcesoEnEjecucion(PCB procesoEnEjecucion) {
        this.procesoEnEjecucion = procesoEnEjecucion;
    }
    
    
    
}
