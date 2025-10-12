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
    private int programCounter;
    private int memoryAddressRegister;
    private int instruccionesContador;
    private int[] cpuRegistros;
    private int contadorProximaIo;
    private int tiempoRestanteBloqueo;
    private int prioridad;
    private long inicioProceso;
    private long finalProceso;
    private int tiempoEspera;
    private int tiempoTotal;
    private boolean isSuspendido;
    private int totalMemoria;
    
    private static int NumRegistros = 6;
    public static int prioridad_default = 5;
   
    
    public PCB(int procesoID, String procesoNombre, int instruccionesTotal, TipoProceso tipo, int ioExceptionCycle, int ioCompletionTime) {
        this.procesoID = procesoID;
        this.procesoNombre = procesoNombre;
        this.instruccionesTotal = instruccionesTotal;
        this.tipo = tipo;
        this.ioExceptionCycle = ioExceptionCycle;
        this.ioCompletionTime = ioCompletionTime;
        
        this.estadoActual = EstadoProceso.Nuevo;
        this.programCounter = 0;
        this.memoryAddressRegister = 0;
        this.instruccionesContador = 0;
        
        this.cpuRegistros = new int[NumRegistros];
                
        this.contadorProximaIo = (tipo == TipoProceso.IO_BOUND)? ioExceptionCycle : -1;
        this.tiempoRestanteBloqueo = 0;
        this.prioridad = prioridad_default;
        this.inicioProceso = -1;
        this.finalProceso = -1;
        this.tiempoEspera = 0;
        this.tiempoTotal = 0;
        this.isSuspendido = false;
        this.totalMemoria = totalMemoria;
    }
}
