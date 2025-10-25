/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import java.io.File;
import javax.swing.JOptionPane;
import Estructuras_de_Datos.*;
import Modelado_de_procesos.*;
/**
 *
 * @author pjroj
 */
public class CPU {
    private static File file;
    private static Cola colaNuevos;
    private static Cola colaListos;
    private static PCB procesoEnEjecucion;
    private static PCB procesoBloqueado;
    private static Cola colaListosSuspendidos;
    private static Cola colaBloqueadosSuspendidos;
    private static Cola colaBloqueados;
    private static Cola colaTerminado;
    private static GestorMemoria gestorMemoria;
    //para definir el tiempo que dura un ciclo de reloj en ms
    private static int ciclo_reloj;
    //para un booleano en la actualizacion en ventanas
    private static boolean actualizar;
    //para saber cuantas instrucciones que se han completado, para verificaciones
    //de tiempo ioExceptionCycle, ioCompletionTime
    private static int countInstrucciones;
    //ioExceptionCycle: cada cuantas instrucciones ocurre una interrupcion de E/S
    //Cada 10 instrucciones (asi lo definimos inicialmente)
    private static int ioExceptionCycle = 10;
    //cuanto tiempo estara bloqueado el proceso cuando ocurre una operacion de E/S
    //Bloqueado durante 5 ciclos (asi lo definimos inicialmente)
    private static int ioCompletionTime = 5;
    //Reloj global del sistema
    private static long reloj_global;
           
    
    /*ORDEN FUNCIONES:
    1.agregarProcesoNuevo       --->      Cola Nuevos (este lo hicimos al iniciar el programa)
    2.1.admitirProceso                    Cola Nuevos ---> Cola Listo Suspendido 
    2.2.admitirProceso                    Cola Nuevos ---> Cola Listos
    3.seleccionarProceso                  Cola Listos ---> Proceso en ejecucion
    4.ejecutarProceso                     Proceso en ejecucion
    5.1.moverEjecutadoACompletado         Proceso en ejecucion ---> Cola Terminado
    5.2.moverEjecutandoABloqueado         Proceso en ejecucion ---> Cola Bloqueado
    5.2.1.moverBloqueadoAListo            Cola Bloqueado ---> Cola Listo
    */
    
    //LA MAXIMA Y MAS IMPORTANTE FUNCION PARA GESTIONAR EL MOVIMIENTO DE COLAS
    public static void ejecutarProcesoCompleto(){
        //2.1.admitirProceso                    Cola Nuevos ---> Cola Listo Suspendido 
        //2.2.admitirProceso                    Cola Nuevos ---> Cola Listos
        admitirProceso();
        
        
        System.out.println("LLEGUE");
        
        Runnable ejecucionProceso = () -> { 
            while(!colaListos.isEmpty()){
                //3.seleccionarProceso                  Cola Listos ---> Proceso en ejecucion
                System.out.println("Antes: "+colaListos.isEmpty());
                colaListos.print2();
                seleccionarProceso();
                System.out.println("Despues: "+colaListos.isEmpty());
                colaListos.print2();
                System.out.println("Se selecciono un proceso");
                //4.ejecutarProceso                     Proceso en ejecucion
                ejecutarProceso();
                //5.1.moverEjecutadoACompletado         Proceso en ejecucion ---> Cola Terminado
                //5.2.moverEjecutandoABloqueado         Proceso en ejecucion ---> Cola Bloqueado
                
            }
        };
        
        Runnable reanudarBloqueado = () -> { 
            while(!colaBloqueados.isEmpty()){
                moverBloqueadoAListo();
                //5.2.1.moverBloqueadoAListo            Cola Bloqueado ---> Cola Listo
            }
        };
        ejecucionProceso.run();
        reanudarBloqueado.run();
        
        
    }
    
    
    //FUNCIONES DEL GESTOR DE COLAS VIEJO
    public static void agregarProcesoNuevo(PCB proceso){  // ESTO ES SOLO PARA AGREGAR PROCESOS A LA COLA DE NUEVOS
        proceso.setEstadoActual(EstadoProceso.NUEVO);
        colaNuevos.enColar(proceso);
    }
    
    public static void seleccionarProceso(){
        if(!colaListos.isEmpty()){
           CPU.setProcesoEnEjecucion(colaListos.desColar());
           procesoEnEjecucion.setEstadoActual(EstadoProceso.EJECUTANDO);
           System.out.println("El proceso: " + procesoEnEjecucion.getProcesoNombre() + "Se saco de la cola de listo" );
           //return procesoEnEjecucion;
        } else {
            CPU.setProcesoEnEjecucion(null);
        }
        //return null;
    }
    
    
    public static void ejecutarProceso(){
       
        
        if(procesoEnEjecucion != null){
            System.out.println("Proceso " + procesoEnEjecucion.getProcesoNombre() + "se esta EJECUTANDO");
            procesoEnEjecucion.ejecutar();
            if(procesoEnEjecucion.getEstadoActual() == EstadoProceso.BLOQUEADO){
                /*Mueve el proceso que estaba en ejecucion y que no se completo 
                (proceso IO_BOUND a la cola de Bloqueados;
                */
                System.out.println("Proceso " + procesoEnEjecucion.getProcesoNombre() + "se esta moviendo a BLOQUEADOS");
                moverEjecutandoABloqueado(procesoEnEjecucion);
            }else if (procesoEnEjecucion.getEstadoActual() == EstadoProceso.TERMINADO){
                /*Mueve el proceso que estaba en ejecucion y que se completo a la cola
                de procesos completados
                */
                System.out.println("Proceso " + procesoEnEjecucion.getProcesoNombre() + "se esta moviendo a TERMINADOS");
                moverEjecutadoACompletado(procesoEnEjecucion);
                
            }
        }
    }
    
    //para mover el proceso que termino de ejecutarse. Proceso con el estado TERMINADO
    public static void moverEjecutadoACompletado(PCB proceso){
        //proceso.setEstadoActual(EstadoProceso.BLOQUEADO);
        colaTerminado.enColar(proceso);
        CPU.setProcesoEnEjecucion(null);
    }
    
    /*para mover el proceso que no termino de ejecutarse, paso a Bloqueado
    Proceso con estado BLOQUEADO, mayormente para interrumpociones IO_BOUND
    */
    public static void moverEjecutandoABloqueado(PCB proceso){ // esto es para mover el proceso a bloqueados pero hay que ver lo de las interrpciones
        //proceso.setEstadoActual(EstadoProceso.BLOQUEADO);
        colaBloqueados.enColar(proceso);
        CPU.setProcesoEnEjecucion(null);
    }
    
    public static void moverBloqueadoAListo(){       
        while(!colaBloqueados.isEmpty()){
            PCB proceso = colaBloqueados.getHead().getProceso();
            //Si 
            if(proceso.getTipo() == TipoProceso.IO_BOUND){
                if(gestorMemoria.puedeEntrarAMemoria(proceso)){
                    proceso = colaBloqueados.desColar();
                    int tiempoSimulado = CPU.getCiclo_reloj();
                    /*
                    se simula el tiempo que espera un proceso de tipo IO_BOUND en la cola de Bloqueado que sera
                    que sera de 5 ciclos (lo que definimos por defaul) por el valor de un ciclo
                    */
                    try {
                        Thread.sleep(tiempoSimulado*ioCompletionTime);
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                    //cambia el estado de BLOQUEADO A LISTO
                    proceso.reanudarBloqueado();
                    proceso.setEstadoActual(EstadoProceso.LISTO);
                    gestorMemoria.asignarMemoria(proceso);
                    colaListos.enColar(proceso);
                    System.out.println(proceso.getProcesoNombre() + " reanudado (LISTO)"); //borrar, solo para verificacion de que sirve

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
        
    }
    
    /**
     * Swapping FIFO para supender procesos cuando se llene la memoria
     * @param memoria
     * @return 
     */
    public static boolean suspenderProceso(int memoria){
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
    public static void admitirProceso(){
        while(!colaNuevos.isEmpty()){
            PCB proceso = colaNuevos.getHead().getProceso();
            System.out.println(colaNuevos.isEmpty());
            //Si 
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
                    System.out.println("break");
                    break;
                }
            }
        }
        System.out.println("termine");
    }
        
    public static void reanudarProceso(){
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

    public static File getFile() {
        return file;
    }
    // definir el archivo txt a usar
    public static void setFile(File file) {
        CPU.file = file;
    }

    public static int getIoExceptionCycle() {
        return ioExceptionCycle;
    }

    public static int getIoCompletionTime() {
        return ioCompletionTime;
    }
    
    public static int getCountInstrucciones() {
        return countInstrucciones;
    }

    public static void setCountInstrucciones(int countInstrucciones) {
        CPU.countInstrucciones = countInstrucciones;
    }
    
    public static Cola getColaNuevos() {
        return colaNuevos;
    }

    public static void setColaNuevos(Cola colaNuevos) {
        CPU.colaNuevos = colaNuevos;
    }

    public static Cola getColaListos() {
        return colaListos;
    }

    public static void setColaListos(Cola colaListos) {
        CPU.colaListos = colaListos;
    }

    public static PCB getProcesoEnEjecucion() {
        return procesoEnEjecucion;
    }

    public static void setProcesoEnEjecucion(PCB procesoEnEjecucion) {
        CPU.procesoEnEjecucion = procesoEnEjecucion;
    }

    public static Cola getColaListosSuspendidos() {
        return colaListosSuspendidos;
    }

    public static void setColaListosSuspendidos(Cola colaListosSuspendidos) {
        CPU.colaListosSuspendidos = colaListosSuspendidos;
    }

    public static Cola getColaBloqueadosSuspendidos() {
        return colaBloqueadosSuspendidos;
    }

    public static void setColaBloqueadosSuspendidos(Cola colaBloqueadosSuspendidos) {
        CPU.colaBloqueadosSuspendidos = colaBloqueadosSuspendidos;
    }

    public static Cola getColaBloqueados() {
        return colaBloqueados;
    }

    public static void setColaBloqueados(Cola colaBloqueados) {
        CPU.colaBloqueados = colaBloqueados;
    }

    public static Cola getColaTerminado() {
        return colaTerminado;
    }

    public static void setColaTerminado(Cola colaTerminado) {
        CPU.colaTerminado = colaTerminado;
    }

    public static GestorMemoria getGestorMemoria() {
        return gestorMemoria;
    }

    public static void setGestorMemoria(GestorMemoria gestorMemoria) {
        CPU.gestorMemoria = gestorMemoria;
    }

    
    public static int getCiclo_reloj() {
        return ciclo_reloj;
    }

    public static void setCiclo_reloj(int ciclo_reloj) {
        CPU.ciclo_reloj = ciclo_reloj;
    }

    public static boolean isActualizar() {
        return actualizar;
    }

    public static void setActualizar(boolean actualizar) {
        CPU.actualizar = actualizar;
    }

    public static long getReloj_global() {
        return reloj_global;
    }

    public static void setReloj_global(long reloj_global) {
        CPU.reloj_global = reloj_global;
    }

    public static PCB getProcesoBloqueado() {
        return procesoBloqueado;
    }

    public static void setProcesoBloqueado(PCB procesoBloqueado) {
        CPU.procesoBloqueado = procesoBloqueado;
    }
    
    
    
}
