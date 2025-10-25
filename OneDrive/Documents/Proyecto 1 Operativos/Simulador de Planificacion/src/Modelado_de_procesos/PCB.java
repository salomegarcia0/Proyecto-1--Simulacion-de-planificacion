/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;
import Estructuras_de_Datos.Hilo;
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
    private EstadoProceso estadoActual;
    private int PC;
    private int MAR;
    
    private int contadorProximaIO;
    private int tiempoRestanteBloqueo;
    
    private int ioExceptionCycle;
    
    private int prioridad;
    private long tiempoCreacion; //ya no es tiempo de llegada
    private long tiempoLlegada; 
    private long tiempoInicioEjecucion; //tiempo de comienzo
    private long tiempoFinalizacion; 
    /*
    será la cantidad de tiempo que ha pasado en ejecucion, se iran sumando
    los tiempos del ciclo de reloj para ello, todo en ms
    */
    private long tiempoEnCPU; 
    private int memoria; 
    
    public static final int prioridad_default = 5; 

    //Variables para las Politicas de planificacion
    private long TAT;
    private long tiempoEstanciaNormalizado; //estaba en long Andrea lo cambi a float
    private long tiempoEspera;  
    private long tiempoServicio;
    
    //Variables para saber que intruccion se esta ejecutando
    
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
            TipoProceso tipo, long tiempoCreacion,int memoria) {
        
        this.procesoID = procesoID;
        this.procesoNombre = procesoNombre;
        this.instruccionesTotal = instruccionesTotal;
        this.tipo = tipo;
        this.ioExceptionCycle = CPU.getIoExceptionCycle();
        
        this.estadoActual = EstadoProceso.NUEVO;
        this.PC = 0; 
        this.MAR = 0; //numero de instrucciones actuales por asi decirlo
         
        this.contadorProximaIO = (tipo == TipoProceso.IO_BOUND)? ioExceptionCycle : -1;
        this.tiempoRestanteBloqueo = 0;
        this.tiempoCreacion = tiempoCreacion;
        this.tiempoEnCPU = 0; 
        this.tiempoInicioEjecucion = 0;
        this.tiempoFinalizacion = 0;

        this.TAT = 0;
        this.tiempoEstanciaNormalizado = 0;
        this.tiempoEspera = 0;
        this.tiempoServicio = 0;
        this.tiempoLlegada = 0;
        this.memoria = memoria; 

    }
    /**
     * ejecuta las instrucciones del proceso
     * MAR contiene la instruccion actual que se esta ejecutando
     * PC contiene la siguiente instruccion a ejecutar
     * NOTA: para todas las politicas menos ROUND ROBIN
     */
    public void ejecutar(){
        
        if (estadoActual == EstadoProceso.EJECUTANDO){
            //Ejecucion para procesos de tipo CPU_BOUND (sin interrupciones)
            if(tipo == TipoProceso.CPU_BOUND){
                System.out.println(procesoNombre + "Tipo: CPU_BOUND");
                while(!haTerminado()){
                    /*
                    Se pide el tiempo del Ciclo_reloj de CPU, de tal forma que si se llega a
                    ocurrir un cambio con respecto a este en medio de la ejecucion de una de las
                    instrucciones del proceso y la suma del dicho tiempo del ciclo reloj en CPU
                    no cambie y no haya incoherencias
                    */
                    int tiempoSimulado = CPU.getCiclo_reloj();
                    MAR = PC; // MAR = instrucción actual que se esta ejecutando
                    //se pone en marca el hilo para simular la ejecucion de una instruccion del proceso
                    Thread thread = new Thread(new Hilo(procesoNombre,MAR,tiempoSimulado));
                    thread.start();
                    try {
                        thread.join(); // espera a que el hilo termine antes de continuar
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /*Se le +1 a el contador de instrucciones del CPU para sabe cuantas instruccione se ha realizado hasta
                    el momento sobretodo para el tema del ioExceptionCycle para los procesos tipo
                    IO_BOUND para interrumpirlos.
                    */
                    CPU.setCountInstrucciones(CPU.getCountInstrucciones()+1);
                    PC++; // incrementar PC para apuntar a la siguiente instrucción
                    //se le suma al tiempoEnCPU el tiempo en ms del ciclo completado
                    tiempoEnCPU = tiempoEnCPU + tiempoSimulado ;
                    System.out.println("Instrucciones ejecutadas: " + CPU.getCountInstrucciones());
                    if(CPU.getCountInstrucciones() == CPU.getIoExceptionCycle()){
                        CPU.setCountInstrucciones(0);
                    };
                }
                //
                if(haTerminado()){
                    estadoActual = EstadoProceso.TERMINADO;
                    System.out.println("Proceso " + procesoNombre + " a TERMINADO");
                    //liberar el espacio de memoria
                    CPU.getGestorMemoria().limpiarMemoriaProceso(memoria);
                    tiempoFinalizacion = System.currentTimeMillis() - CPU.getReloj_global(); 
                }
            //Ejecucion para procesos de tipo IO_BOUND (con interrupciones)
           } else if (tipo == TipoProceso.IO_BOUND){
                System.out.println(procesoNombre + "Tipo: IO_BOUND");
                while(!haTerminado() && estadoActual != EstadoProceso.BLOQUEADO){
                    if (CPU.getCountInstrucciones() != CPU.getIoExceptionCycle()){
                        /*
                        Se pide el tiempo del Ciclo_reloj de CPU, de tal forma que si se llega a
                        ocurrir un cambio con respecto a este en medio de la ejecucion de una de las
                        instrucciones del proceso y la suma del dicho tiempo del ciclo reloj en CPU
                        no cambie y no haya incoherencias
                        */
                        int tiempoSimulado = CPU.getCiclo_reloj();
                        MAR = PC; // MAR = instrucción actual que se esta ejecutando
                        Thread thread = new Thread(new Hilo(procesoNombre,MAR,tiempoSimulado));
                        thread.start();
                        try {
                            thread.join(); // espera a que el hilo termine antes de continuar
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //Se le +1 a el contador de instrucciones del CPU para sabe cuantas instruccione se ha realizado hasta
                        //el momento sobretodo para el tema del ioExceptionCycle para los procesos tipo
                        //IO_BOUND para interrumpirlos
                        CPU.setCountInstrucciones(CPU.getCountInstrucciones()+1);
                        PC++; // incrementar PC para apuntar a la siguiente instrucción
                        //se le suma al tiempoEnCPU el tiempo en ms del ciclo completado
                        tiempoEnCPU = tiempoEnCPU + tiempoSimulado;
                    }
                    System.out.println("Instrucciones ejecutadas: " + CPU.getCountInstrucciones());
                    //si el numero de instrucciones realizadas actualmente en CPU es igual al valor del IoExceptionCycle()
                    if(CPU.getCountInstrucciones() == CPU.getIoExceptionCycle() && !haTerminado()){
                        System.out.println("Ya pasaron " + CPU.getIoExceptionCycle() + " instrucciones, inicia bloqueo");
                        CPU.setCountInstrucciones(0);
                        System.out.println("Proceso " + procesoNombre + " a BLOQUEADO");
                        //se le suma el tiempo que llevaba en ejecucion
                        tiempoFinalizacion = System.currentTimeMillis() - CPU.getReloj_global();
                        estadoActual = EstadoProceso.BLOQUEADO;
                    }
                }
                if(haTerminado()){
                    estadoActual = EstadoProceso.TERMINADO;
                    /*
                    se le suma el tiempo que llevaba en ejecucion + el tiempo que llevaba en ejecucion veces anteriores
                    se le sumara algo si ya había estado en ejecucion antes
                    */
                    System.out.println("Proceso " + procesoNombre + " a TERMINADO");
                    //liberar el espacio de memoria
                    CPU.getGestorMemoria().limpiarMemoriaProceso(memoria);
                    tiempoFinalizacion = (System.currentTimeMillis() - CPU.getReloj_global()) + tiempoFinalizacion; 
                }
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
    
    public void bloquear(){
        if (estadoActual == EstadoProceso.LISTO){
            estadoActual = EstadoProceso.BLOQUEADO;
        }
    }
    
    public void reanudar(){
        if (estadoActual == EstadoProceso.LISTO_SUSPENDIDO){
            estadoActual = EstadoProceso.LISTO;
        } else if (estadoActual == EstadoProceso.BLOQUEADO_SUSPENDIDO){
            estadoActual = EstadoProceso.BLOQUEADO;
        }
    }
    
    public void reanudarBloqueado(){
        if (estadoActual == EstadoProceso.BLOQUEADO){
            estadoActual = EstadoProceso.LISTO;
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

    public long getTiempoEnCPU() {
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
    

    public int getInstruccionActual() {
        return MAR;
    }
    

    public int getSiguienteInstruccion() {
        return PC;
    }
    
}
