/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;

import main.CPU;

/**
 * Clase para crear los procesos de la simualacion
 * @author salom
 */
public class PCB {

    private int procesoID;
    private String procesoNombre;
    private int instruccionesTotal;
    private TipoProceso tipo;
    private int ioExceptionCycle;
    private int ioCompletionTime;
    private EstadoProceso estadoActual;
    private int PC;
    private int MAR;
    
    private int contadorProximaIO;
    private int tiempoRestanteBloqueo;
    
    private int prioridad;
    private long tiempoCreacion; //tiempo de llegada
    private long tiempoInicioEjecucion; //tiempo de comienzo
    private long tiempoFinalizacion; 
    private int tiempoEnCPU; //será la sumatoria actual de los tiempos de servicio que hay en el cpu a medida que se agrega proceso
    //a la cola
    private int memoria; 
    
    public static final int prioridad_default = 5; 

    //Variables para las Politicas de planificacion
    private long TAT;
    private long tiempoEstanciaNormalizado; //estaba en long Andrea lo cambi a float
    private long tiempoEspera;  
    private long tiempoServicio;
    
    // sin prioridad
    /**
     * constructor de PCB sin prioridad
     * @param procesoID
     * @param procesoNombre
     * @param instruccionesTotal
     * @param tipo
     * @param ioExceptionCycle
     * @param ioCompletionTime 
     */
    public PCB(int procesoID, String procesoNombre, int instruccionesTotal, 
            TipoProceso tipo, int ioExceptionCycle, int ioCompletionTime, long tiempoCreacion, long tiempoServicio,int memoria) {
        
        this.procesoID = procesoID;
        this.procesoNombre = procesoNombre;
        this.instruccionesTotal = instruccionesTotal;
        this.tipo = tipo;
        //cada cuantas instrucciones ocurre una interrupcion de E/S
        //Cada 10 instrucciones (asi lo definimos inicialmente)
        this.ioExceptionCycle = ioExceptionCycle; 
        //cuanto tiempo estara bloqueado el proceso cuando ocurre una operacion de E/S
        //Bloqueado durante 5 ciclos (asi lo definimos inicialmente)
        this.ioCompletionTime = ioCompletionTime; 
        
        this.estadoActual = EstadoProceso.NUEVO;
        this.PC = 0; 
        this.MAR = 0;
         
        this.contadorProximaIO = (tipo == TipoProceso.IO_BOUND)? ioExceptionCycle : -1;
        this.tiempoRestanteBloqueo = 0;
        this.tiempoCreacion = tiempoCreacion;
        this.tiempoEnCPU = 0; 
        this.tiempoInicioEjecucion = 0;
        this.tiempoFinalizacion = 0;

        this.TAT = 0;
        this.tiempoEstanciaNormalizado = 0;
        this.tiempoEspera = 0;
        this.tiempoServicio = tiempoServicio;
        this.memoria = memoria; 

    }
    //VERIFICAR lo del mar y el pc porque no se utiliza de esta forma creo 
    public void ejecutarInstruccion(){
        if (estadoActual == EstadoProceso.EJECUTANDO){
            PC++;
            MAR++;
            tiempoEnCPU++;
            
            if (tipo == TipoProceso.IO_BOUND && contadorProximaIO > 0){
                contadorProximaIO--;
                if (contadorProximaIO == 0){
                    estadoActual = EstadoProceso.BLOQUEADO;
                    tiempoRestanteBloqueo = ioCompletionTime;
                    contadorProximaIO = ioExceptionCycle;
                }
            }
            
            if(haTerminado()){
                estadoActual = EstadoProceso.TERMINADO;
                tiempoFinalizacion = System.currentTimeMillis(); //segundos
            }
        }
        
//        System.out.println("Proceso " + proceso.getProcesoNombre() + " EJECUTANDO");
//        //numero de instrucciones que tiene en total el proceso
//        int instruc = proceso.getInstruccionesTotal();
//        //numero de instrucciones completadas hasta el momento
//        int instrucCompletadas =  0;
//        while (!proceso.haTerminado()) {
//            try {
//                //
//                Thread.sleep(CPU.getCiclo_reloj());
//                //
//                if(instrucCompletadas == instruc){
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            
//                
//                
//        }
//        System.out.println("Proceso " + proceso.getProcesoNombre() + " COMPLETADO");
    }
    
    public boolean haTerminado() {
        return PC >= instruccionesTotal;
    }
    
    public void actualizarBloqueo(){
        if (estadoActual == EstadoProceso.BLOQUEADO && tiempoRestanteBloqueo > 0){
            tiempoRestanteBloqueo--;
            if (tiempoRestanteBloqueo == 0){
                estadoActual = EstadoProceso.LISTO;
            }
        }
    }
    
    public void suspender(){
        if (estadoActual == EstadoProceso.LISTO){
            estadoActual = EstadoProceso.LISTO_SUSPENDIDO;
        } else if (estadoActual == EstadoProceso.BLOQUEADO){
            estadoActual = EstadoProceso.BLOQUEADO_SUSPENDIDO;
        }
    }
    
    public void reanudar(){
        if (estadoActual == EstadoProceso.LISTO_SUSPENDIDO){
            estadoActual = EstadoProceso.LISTO;
        } else if (estadoActual == EstadoProceso.BLOQUEADO_SUSPENDIDO){
            estadoActual = EstadoProceso.BLOQUEADO;
        }
    }

    public int getProcesoID() {
        return procesoID;
    }

    public String getProcesoNombre() {
        return procesoNombre;
    }

    public void setProcesoNombre(String procesoNombre) {
        this.procesoNombre = procesoNombre;
    }

    public long getTAT() {
        return TAT;
    }

    public long getTiempoEstanciaNormalizado() {
        return tiempoEstanciaNormalizado;
    }

    public long getTiempoEspera() {
        return tiempoEspera;
    }

    public long getTiempoServicio() {
        return tiempoServicio;
    }

    public void setTiempoServicio(long tiempoServicio) {
        this.tiempoServicio = tiempoServicio;
    }

    /*Luego de calculado el tiempoFinalización en la respectiva Politica_de_Planificación se procede a llamar esta funcion
    para completar los calculos de TAT(tiempo de estancia), tiempoEstanciaNormalizado y tiempoEspera
    
    Utilizado en la clase FCFS
    */
    public void calculoTiempos() {
        //Tiempo de estancia (TAT) = Tiempo de finalización - tiempo de llegada 
        this.TAT = this.tiempoFinalizacion - this.tiempoCreacion;
        //Tiempo de estancia normalizado = TAT / tiempo de servicio
        this.tiempoEstanciaNormalizado = this.TAT / this.tiempoServicio;
        //Tiempo de espera = TAT - tiempo de servicio
        this.tiempoEspera = this.TAT - this.tiempoServicio;
    }
    
    public int getInstruccionesTotal() {
        return instruccionesTotal;
    }

    public TipoProceso getTipo() {
        return tipo;
    }

    public int getIoExceptionCycle() {
        return ioExceptionCycle;
    }

    public int getIoCompletionTime() {
        return ioCompletionTime;
    }

    public EstadoProceso getEstadoActual() {
        return estadoActual;
    }

    public int getPC() {
        return PC;
    }

    public int getMAR() {
        return MAR;
    }

    public int getContadorProximaIO() {
        return contadorProximaIO;
    }

    public int getTiempoRestanteBloqueo() {
        return tiempoRestanteBloqueo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public long getTiempoCreacion() {
        return tiempoCreacion;
    }

    public long getTiempoInicioEjecucion() {
        return tiempoInicioEjecucion;
    }

    public long getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }

    public int getTiempoEnCPU() {
        return tiempoEnCPU;
    }

    public static int getPrioridad_default() {
        return prioridad_default;
    }

    public void setEstadoActual(EstadoProceso estado) {
        this.estadoActual = estado;
        
        if (estado == EstadoProceso.EJECUTANDO && tiempoInicioEjecucion == 0){
            tiempoInicioEjecucion = System.currentTimeMillis();
        }
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setTiempoInicioEjecucion(long tiempoInicioEjecucion) {
        this.tiempoInicioEjecucion = tiempoInicioEjecucion;
    }

    public void setTiempoFinalizacion(long tiempoFinalizacion) {
        this.tiempoFinalizacion = tiempoFinalizacion;
    }

    public void setProcesoID(int procesoID) {
        this.procesoID = procesoID;
    }

    public void setInstruccionesTotal(int instruccionesTotal) {
        this.instruccionesTotal = instruccionesTotal;
    }

    public void setTipo(TipoProceso tipo) {
        this.tipo = tipo;
    }

    public void setIoExceptionCycle(int ioExceptionCycle) {
        this.ioExceptionCycle = ioExceptionCycle;
    }

    public void setIoCompletionTime(int ioCompletionTime) {
        this.ioCompletionTime = ioCompletionTime;
    }

    public void setTiempoRestanteBloqueo(int tiempoRestanteBloqueo) {
        this.tiempoRestanteBloqueo = tiempoRestanteBloqueo;
    }

    public void setTiempoCreacion(long tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    public void setTiempoEnCPU(int tiempoEnCPU) {
        this.tiempoEnCPU = tiempoEnCPU;
    }

    public void setTAT(long TAT) {
        this.TAT = TAT;
    }

    public void setTiempoEstanciaNormalizado(long tiempoEstanciaNormalizado) {
        this.tiempoEstanciaNormalizado = tiempoEstanciaNormalizado;
    }

    public void setTiempoEspera(long tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public int getMemoria() {
        return memoria;
    }

    public void setMemoria(int memoria) {
        this.memoria = memoria;
    }
    
}
