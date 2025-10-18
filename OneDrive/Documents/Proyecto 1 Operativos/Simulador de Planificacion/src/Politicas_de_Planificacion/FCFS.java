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
        System.out.println("holaaa");
    }

}
