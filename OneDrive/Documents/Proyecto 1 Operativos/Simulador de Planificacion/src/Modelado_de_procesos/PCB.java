/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;

/**
 *
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
    private long tiempoCreacion;
    private long tiempoInicioEjecucion;
    private long tiempoFinalizacion;
    private int tiempoEnCPU;
    
    public static final int prioridad_default = 5;
   
    
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
            TipoProceso tipo, int ioExceptionCycle, int ioCompletionTime) {
        
        this.procesoID = procesoID;
        this.procesoNombre = procesoNombre;
        this.instruccionesTotal = instruccionesTotal;
        this.tipo = tipo;
        this.ioExceptionCycle = ioExceptionCycle;
        this.ioCompletionTime = ioCompletionTime;
        
        this.estadoActual = EstadoProceso.NUEVO;
        this.PC = 0;
        this.MAR = 0;
         
        this.contadorProximaIO = (tipo == TipoProceso.IO_BOUND)? ioExceptionCycle : -1;
        this.tiempoRestanteBloqueo = 0;
        this.tiempoCreacion = System.currentTimeMillis();
        this.tiempoEnCPU = 0;
        this.tiempoInicioEjecucion = 0;
        this.tiempoFinalizacion = 0;
    }
    
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
                tiempoFinalizacion = System.currentTimeMillis();
            }
        }
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
    
}
