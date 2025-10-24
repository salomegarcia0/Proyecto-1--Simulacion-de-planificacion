/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras_de_Datos;
import Modelado_de_procesos.*;
import main.*;
/**
 *
 * @author pjroj
 */
public class Hilo extends Thread {
    private PCB proceso;

    public Hilo(PCB proceso) {
        this.proceso = proceso;
    }

    @Override
    //necesito luego de cada thread aumenta un contador de instrucciiones que se han llevado actualmente, verificar si el procesos es IO que ocurra una
    //una interrumpcion para eso requiero el reloj en global
    public void run() {
        System.out.println("Proceso " + proceso.getProcesoNombre() + " EJECUTANDO");
        //numero de instrucciones que tiene en total el proceso
        
        //numero de instrucciones completadasnt instruc = proceso.getInstruccionesTotal(); hasta el momento
        //int instrucCompletadas =  0;
        try {
            Thread.sleep(CPU.getCiclo_reloj());
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
 // mar = pc
 //luego pc+1
    }
}

