/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import java.io.File;
import javax.swing.JOptionPane;
import Estructuras_de_Datos.*;
import Modelado_de_procesos.*;
import java.util.concurrent.Semaphore;
/**
 *
 * @author pjroj
 */
public class CPU {
    private static File file;
    private static Cola colaGuardados;
    private static Cola colaNuevos;
    private static Cola colaListos;
    private static PCB procesoEnEjecucion;
    private static Cola colaListosSuspendidos;
    private static Cola colaBloqueadosSuspendidos;
    private static Cola colaBloqueados;
    private static Cola colaTerminado;
    private static GestorMemoria gestorMemoria;
    //cuanto tiempo estara el proceso en ejecucion
    //Sera durante 4 ciclos (asi lo definimos inicialmente)
    private static int Round_Robin = 4;
    //para saber cuantas instrucciones que se han completado, para verificaciones
    //de tiempo Round_Robin;
    private static int countRound_Robin;
    //para definir el tiempo que dura un ciclo de reloj en ms
    private static int ciclo_reloj;
    //para un booleano en la actualizacion en ventanas
    private static boolean actualizar =  true;
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
    
    
    private static final Semaphore semaforoColas = new Semaphore(1); // protege las colas, impide que varios hilos modifiquen las coolas a la vez
    private static final Semaphore semaforoEjecucion = new Semaphore(1);// impide que varios procesos se ejecuten a la vez
    private static final Semaphore semaforoMemoria = new Semaphore(1); // protege los contadores de la memoria, entonces evita qque se asigne 
    
    /*ORDEN FUNCIONES:
    1.agregarProcesoNuevo       --->      Cola Nuevos
    2.1.admitirProceso                    Cola Nuevos ---> Cola Listo Suspendido (creo yo andrea)
    2.2.admitirProceso                    Cola Nuevos ---> Cola Listos
    3.aplicarPolitica                     Organiza la cola de listos
    4.seleccionarProceso                  Cola Listos ---> Proceso en ejecucion
    5.ejecutarProceso                     Proceso en ejecucion
    6.1.moverEjecutadoACompletado         Proceso en ejecucion ---> Cola Terminado
    6.2.moverEjecutandoABloqueado         Proceso en ejecucion ---> Cola Bloqueado
    6.2.1.moverBloqueadoAListo            Cola Bloqueado ---> Cola Listo
    */
    
    //hola commint
    
    //LA MAXIMA Y MAS IMPORTANTE FUNCION PARA GESTIONAR EL MOVIMIENTO DE COLAS
    public static void ejecutarProcesoCompleto(){
        //2.1.admitirProceso                    Cola Nuevos ---> Cola Listo Suspendido 
        //2.2.admitirProceso                    Cola Nuevos ---> Cola Listos
        admitirProceso();
        
        //3.aplicarPolitica                     Organiza la cola de listos 
        //probablemente requerimos un swicth
        
        
        System.out.println("LLEGUE");
        
                
        Runnable ejecucionProceso = () -> { 
            boolean verifi = false;
            while(!colaListos.isEmpty()){
                //4.seleccionarProceso                  Cola Listos ---> Proceso en ejecucion
                System.out.println("Antes COLA LISTOS: "+colaListos.isEmpty());
                colaListos.print2();
                seleccionarProceso();
                System.out.println("Despues LISTOS: "+colaListos.isEmpty());
                colaListos.print2();
                System.out.println("Se selecciono un proceso");
                //5.ejecutarProceso                     Proceso en ejecucion
                //FCFS
                ejecutarProceso();
                //ROUND ROBIN
                //ejecutarProcesoROUND_ROBIN();
                //6.1.moverEjecutadoACompletado         Proceso en ejecucion ---> Cola Terminado
                //6.2.moverEjecutandoABloqueado         Proceso en ejecucion ---> Cola Bloqueado
                System.out.println("COLA BLOQUEADO: "+colaListos.isEmpty());
                colaBloqueados.print2();
                System.out.println("Todo vacio? " +CPU.isEmpty());
//                if(CPU.isEmpty()){
//                    verifi = true;
//                }
            }
        };
        
        Runnable reanudarBloqueado = () -> { 
            while(!colaBloqueados.isEmpty()){
                moverBloqueadoAListo();
                //6.2.1.moverBloqueadoAListo            Cola Bloqueado ---> Cola Listo
            }
        };
        
        ejecucionProceso.run();
        reanudarBloqueado.run();
        
        
    }
   
    //FUNCIONES DEL GESTOR DE COLAS VIEJO
    public static void agregarProcesoNuevo(PCB proceso){  // ESTO ES SOLO PARA AGREGAR PROCESOS A LA COLA DE NUEVOS

        try{
        semaforoColas.acquire();
        proceso.setEstadoActual(EstadoProceso.NUEVO);
        colaNuevos.enColar(proceso);
        }catch (InterruptedException e){
            throw new RuntimeException("Se interrumpio la operacion", e);
        }finally{
            semaforoColas.release();
        }

    }
    
    public static void agregarProcesoNuevoGUARDADO(PCB proceso){  

        try{
        semaforoColas.acquire();
        proceso.setEstadoActual(EstadoProceso.NUEVO);
        colaGuardados.enColar(proceso);
        }catch (InterruptedException e){
            throw new RuntimeException("Se interrumpio la operacion", e);
        }finally{
            semaforoColas.release();
        }

    }
    
    public static void seleccionarProceso(){
        
        try{
            semaforoColas.acquire();
            semaforoEjecucion.acquire();
            if(!colaListos.isEmpty()){
               CPU.setProcesoEnEjecucion(colaListos.desColar());
               procesoEnEjecucion.setEstadoActual(EstadoProceso.EJECUTANDO);

            } else {
                CPU.setProcesoEnEjecucion(null);
            }
        } catch (InterruptedException e){
            throw new RuntimeException("Se interrumpio la operacion", e);    
        } finally {
            semaforoEjecucion.release();
            semaforoColas.release();
        }
    }
    
    
    public static void ejecutarProceso(){
        try{
            semaforoEjecucion.acquire();
            
            if(procesoEnEjecucion != null){
                System.out.println("Proceso " + procesoEnEjecucion.getProcesoNombre() + "se esta EJECUTANDO");
                procesoEnEjecucion.ejecutar();
                if(procesoEnEjecucion.getEstadoActual() == EstadoProceso.BLOQUEADO){
                /*Mueve el proceso que estaba en ejecucion y que no se completo 
                (proceso IO_BOUND a la cola de Bloqueados;
                */
                moverEjecutandoABloqueado(procesoEnEjecucion);
                }else if(procesoEnEjecucion.getEstadoActual() == EstadoProceso.TERMINADO){
                /*Mueve el proceso que estaba en ejecucion y que se completo a la cola
                de procesos completados
                */
                moverEjecutadoACompletado(procesoEnEjecucion);
                }
            }
        }catch(InterruptedException e){
            throw new RuntimeException("Se interrumpio la operacion", e); 
        } finally {
            semaforoEjecucion.release();
        }
    }

    
        public static void ejecutarProcesoROUND_ROBIN(){
            System.out.println("PROCESO ROUND ROBIN");
            try{
                semaforoEjecucion.acquire();

                if(procesoEnEjecucion != null){
                    System.out.println("Proceso " + procesoEnEjecucion.getProcesoNombre() + "se esta EJECUTANDO");
                    procesoEnEjecucion.ejecutarRoundRobin();
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

                    }else if (procesoEnEjecucion.getEstadoActual() == EstadoProceso.LISTO){
                        /*Mueve el proceso que estaba en ejecucion y aunque no se ha completado a la cola
                        de procesos listos para esperar a ejecutarse de nuevo
                        */
                        System.out.println("Proceso " + procesoEnEjecucion.getProcesoNombre() + "se esta moviendo a Listos");
                        moverEjecutadoAListo(procesoEnEjecucion);
                    }
                }
            }catch(InterruptedException e){
                throw new RuntimeException("Se interrumpio la operacion", e); 
            } finally {
                semaforoEjecucion.release();
            }
    }
    
    //para mover el proceso que termino de ejecutarse. Proceso con el estado TERMINADO
    public static void moverEjecutadoACompletado(PCB proceso){
        
        try{
            semaforoColas.acquire();
            proceso.setEstadoActual(EstadoProceso.TERMINADO);
            colaTerminado.enColar(proceso);
            CPU.setProcesoEnEjecucion(null); 
        } catch(InterruptedException e){
            throw new RuntimeException("Se interrumpio la operacion", e); 
        } finally {
            semaforoColas.release();
        }
    }
    
    /*para mover el proceso que no termino de ejecutarse, paso a Bloqueado
    Proceso con estado BLOQUEADO, mayormente para interrumpociones IO_BOUND
    */
    public static void moverEjecutandoABloqueado(PCB proceso){ // esto es para mover el proceso a bloqueados pero hay que ver lo de las interrpciones
        try{
            semaforoColas.acquire();

            proceso.setEstadoActual(EstadoProceso.BLOQUEADO);
            colaBloqueados.enColar(proceso);
            CPU.setProcesoEnEjecucion(null);
        }catch(InterruptedException e){
            throw new RuntimeException("Se interrumpio la operacion", e);
        } finally{
            semaforoColas.release();
        }
    }
    
    public static void moverBloqueadoAListo(){
        try{
            while(true){
                boolean hayProcesos = false; // flag para ssaber si existen procesos en la cola
                PCB proceso = null; // el proceso actual con el que va a trabajar
                
                semaforoColas.acquire(); //bloquea el acceso a la cola
                try{
                    hayProcesos = !colaBloqueados.isEmpty(); // revisa que hay elementos en la cola
                    if(hayProcesos){ //si lo hay entonces nos ASEGURAMOS de que la cabeza no sea null
                        Nodo cabeza = colaBloqueados.getHead();
                        proceso = (cabeza != null) ? cabeza.getProceso() : null;
                    }
                } finally{
                    semaforoColas.release(); //desbloquea la cola 
                }
                
                if (!hayProcesos || proceso == null) break; // matamos el bucle si la cola esta vacia o la cabeza es null
                
                if(proceso.getTipo() != TipoProceso.IO_BOUND){ //verificamos wue son io bound porwuer son lo unicos que se bloquean 
                    continue;
                }
                    int tiempoSimulado = CPU.getCiclo_reloj(); //obtener la duracion del ciclo 
                    try{
                        Thread.sleep(tiempoSimulado * ioCompletionTime); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    
                    //ya aca es donde esta todo el movimiento de bloqueado a listo
                    semaforoColas.acquire();
                    
                    try{
                        PCB procesoReanudando = colaBloqueados.desColar();
                        if (procesoReanudando != null){
                            procesoReanudando.reanudarBloqueado();
                            procesoReanudando.setEstadoActual(EstadoProceso.LISTO);
                            colaListos.enColar(procesoReanudando);
                            System.out.println(procesoReanudando.getProcesoNombre()+ " reanudado");
                        }
                    } finally {
                        semaforoColas.release();
                    }
            }
        } catch (InterruptedException e){
            throw new RuntimeException("Error en moverBloqueadosAListo", e);
        }
    }
    
    
    /**
    public static void moverBloqueadoAListo(){
        try{
            
            semaforoColas.acquire();
            semaforoMemoria.acquire();
            
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
                       /** try {
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
       }catch (InterruptedException e){
           throw new RuntimeException("Error moviendo bloqueado a listo", e);
       }finally{
            semaforoMemoria.release();
            semaforoColas.release();
        }  
    }
*/
    
    //SOLO PARA ROUND_ROBIN, los procesos tipo CPU luego de que termine el ciclo de Round Robin vuelven a la cola de listos
    public static void moverEjecutadoAListo(PCB proceso){
        try{
            semaforoColas.acquire();
            proceso.setEstadoActual(EstadoProceso.LISTO);
            colaListos.enColar(proceso);
            CPU.setProcesoEnEjecucion(null); 
        } catch(InterruptedException e){
            throw new RuntimeException("Se interrumpio la operacion", e); 
        } finally {
            semaforoColas.release();
        }
    }
    
    /**
     * Swapping FIFO para supender procesos cuando se llene la memoria
     * @param memoria
     * @return 
     */
    public static boolean suspenderProceso(int memoria){
        int memoriaLiberada = 0;
        try{
            semaforoColas.acquire();
            semaforoMemoria.acquire();

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
        } catch(InterruptedException e){
           throw new RuntimeException("Error moviendo bloqueado a listo", e);
        }finally{
            semaforoMemoria.release();
            semaforoColas.release();
        }
        
    }
    
    /**
     * admite procesos en la cola de listos pero valida la memoria para saber si hay que hacer swapping o no
     */
    public static void admitirProceso(){
        
        try{
            semaforoColas.acquire();
            semaforoMemoria.acquire();
            while(!colaNuevos.isEmpty()){
                PCB proceso = colaNuevos.getHead().getProceso();
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
                        break;
                    }
                }
            }
        }catch(InterruptedException e){
           throw new RuntimeException("Error moviendo bloqueado a listo", e);
        }finally{
            semaforoMemoria.release();
            semaforoColas.release();
        }
        
    }
        
    public static void reanudarProceso(){
        try{
            semaforoColas.acquire();
            semaforoMemoria.acquire();
            
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
        }catch(InterruptedException e){
           throw new RuntimeException("Error moviendo bloqueado a listo", e);
        }finally{
            semaforoMemoria.release();
            semaforoColas.release();
        }
        
    }
    
    public static void moverBloqueadoSuspAListoSusp(){
        try{
            semaforoColas.acquire();
            if (colaBloqueadosSuspendidos.isEmpty()){
                return;
            }
            
            int n = colaBloqueadosSuspendidos.getSize();
            
            for (int i = 0; i < n; i++){
                PCB proceso = colaBloqueadosSuspendidos.desColar();
                
                proceso.actualizarBloqueoSuspendido();
                
                if (proceso.getEstadoActual() == EstadoProceso.LISTO_SUSPENDIDO){
                    colaListosSuspendidos.enColar(proceso);
                    System.out.println(proceso.getProcesoNombre() +" completo su bloqueo -> esta en Listo Suspendido");
                } else {
                    colaBloqueadosSuspendidos.enColar(proceso);
                }
            }
            
        } catch(InterruptedException e){
           throw new RuntimeException("Error en moverBloqueadoSuspAListoSusp", e);
        } finally{
            semaforoColas.release();
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

    
    public static boolean isEmpty(){
        return colaNuevos.isEmpty() && colaListos.isEmpty() && colaListosSuspendidos.isEmpty() && colaBloqueadosSuspendidos.isEmpty() && colaBloqueados.isEmpty() &&
        colaTerminado.isEmpty() &&  procesoEnEjecucion == null     ;
    
    }

    public static int getRound_Robin() {
        return Round_Robin;
    }

    public static void setRound_Robin(int Round_Robin) {
        CPU.Round_Robin = Round_Robin;
    }

    public static int getCountRound_Robin() {
        return countRound_Robin;
    }

    public static void setCountRound_Robin(int countRound_Robin) {
        CPU.countRound_Robin = countRound_Robin;
    }

    public static Cola getColaGuardados() {
        return colaGuardados;
    }

    public static void setColaGuardados(Cola colaGuardados) {
        CPU.colaGuardados = colaGuardados;
    }
    
    
    
    
    
}
