/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;

/**
 * Clase para crear los procesos de la simualacion
 * @author salom
 */
public class PCB {
    private String procesoID;
    private String procesoNombre;
    private EstadoProceso estadoActual;
    private TipoProceso tipo;
    private int programCounter;
    private int instruccionesContador;
    private int instruccionesTotal;
    //Variables para las Politicas de planificacion
    private int tiempoServicio;
    private int tiempoLlegada;
    private int tiempoFinalizacion;
    private int TAT;
    private int tiempoEstanciaNormalizado;
    private int tiempoEspera;

    public PCB(String procesoID, String procesoNombre, int tiempoServicio, int tiempoLlegada) {
        this.procesoID = procesoID;
        this.procesoNombre = procesoNombre;
        this.tiempoServicio = tiempoServicio;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoFinalizacion = 0;
        this.TAT = 0;
        this.tiempoEstanciaNormalizado = 0;
        this.tiempoEspera = 0;
    }

    public String getProcesoID() {
        return procesoID;
    }

    public void setProcesoID(String procesoID) {
        this.procesoID = procesoID;
    }

    public String getProcesoNombre() {
        return procesoNombre;
    }

    public void setProcesoNombre(String procesoNombre) {
        this.procesoNombre = procesoNombre;
    }

    public int getTiempoServicio() {
        return tiempoServicio;
    }

    public void setTiempoServicio(int tiempoServicio) {
        this.tiempoServicio = tiempoServicio;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public int getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }
    
    public void setTiempoFinalizacion(int tiempoFinalizacion) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public int getTAT() {
        return TAT;
    }

    public int getTiempoEstanciaNormalizado() {
        return tiempoEstanciaNormalizado;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

   
    /*Luego de calculado el tiempoFinalización en la respectiva Politica_de_Planificación se procede a llamar esta funcion
    para completar los calculos de TAT(tiempo de estancia), tiempoEstanciaNormalizado y tiempoEspera
    
    Utilizado en la clase FCFS
    */
    public void calculoTiempos() {
        this.TAT = this.tiempoFinalizacion - this.tiempoLlegada;
        this.tiempoEstanciaNormalizado = this.TAT / this.tiempoLlegada;
        this.tiempoEspera = this.TAT - this.tiempoServicio;
    }
    
    

    
    
}
