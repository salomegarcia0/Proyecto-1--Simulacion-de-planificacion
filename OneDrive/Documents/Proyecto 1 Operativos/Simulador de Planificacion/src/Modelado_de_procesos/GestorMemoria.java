/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;
import Estructuras_de_Datos.gestorColas;

/**
 *
 * @author salom
 */
public class GestorMemoria {
    private int memoriaTotal;  //memoria del sistema
    private int memoriaUsada;  //memoria que ya esta siendo ocupada
    private int maxProcesosEnMemoria; // lista de procesos para manejar suspension

    public GestorMemoria() {
        this.memoriaTotal = 512; // GB de memoria total 
        this.memoriaUsada = 0;   // inicializampos la memeoria usada en 0 porque aun no se ejecuta nada
        this.maxProcesosEnMemoria = 4; // este numero puede variar, puse 4 para que la simulacion de los suspendidos no dure tanto
    }
    
    /**
     * Verifica si el proceso puede entrar a la memoria por la cantidad de proceso que haya en memoria
     * @param colas
     * @return 
     */
    public boolean puedeEntrarEnMemoria(gestorColas colas){
        int procesosEnMemoria = colas.getColaListos().getSize() + 
                colas.getColaBloqueados().getSize();
        
        if(colas.getProcesoEnEjecucion() != null){
            procesosEnMemoria++;
        }
        
        return procesosEnMemoria < maxProcesosEnMemoria;
    }
    
    /**
     * Este verifica si el proceso puede entrar a la memoria basandose en la cantidad de memoria
     * @param proceso
     * @return 
     */
    public boolean puedeEjecutar(PCB proceso){
        return (memoriaUsada + proceso.getMemoria())<= memoriaTotal; 
    }        
}
