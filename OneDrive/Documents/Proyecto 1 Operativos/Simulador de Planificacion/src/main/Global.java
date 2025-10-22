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
public class Global {
    private static File file;
    private static Cola inicial;
    private static Cola listo;
    private static PCB procesoEjecutando;
    private static Cola listosSuspendido;
    private static Cola bloqueadosSuspendido;
    private static Cola bloqueado;
    private static Cola terminado;
    //para definir el tiempo que dura un ciclo de reloj en ms
    private static int ciclo_reloj;
    //para un booleano en la actualizacion en ventanas
    private static boolean actualizar;
            
    

    public static File getFile() {
        return file;
    }
    // definir el archivo txt a usar
    public static void setFile(File file) {
        Global.file = file;
    }

    public static Cola getListo() {
        return listo;
    }

    public static void setListo(Cola listo) {
        Global.listo = listo;
    }

    public static Cola getListosSuspendido() {
        return listosSuspendido;
    }

    public static void setListosSuspendido(Cola listosSuspendido) {
        Global.listosSuspendido = listosSuspendido;
    }

    public static Cola getBloqueadosSuspendido() {
        return bloqueadosSuspendido;
    }

    public static void setBloqueadosSuspendido(Cola bloqueadosSuspendido) {
        Global.bloqueadosSuspendido = bloqueadosSuspendido;
    }

    public static Cola getBloqueado() {
        return bloqueado;
    }

    public static void setBloqueado(Cola bloqueado) {
        Global.bloqueado = bloqueado;
    }

    public static Cola getInicial() {
        return inicial;
    }

    public static void setInicial(Cola inicial) {
        Global.inicial = inicial;
    }
    
    public static void hola(){
        System.out.println("hola");
    }

    public static PCB getProcesoEjecutando() {
        return procesoEjecutando;
    }

    public static void setProcesoEjecutando(PCB procesoEjecutando) {
        Global.procesoEjecutando = procesoEjecutando;
    }

    public static Cola getTerminado() {
        return terminado;
    }

    public static void setTerminado(Cola terminado) {
        Global.terminado = terminado;
    }

    public static int getCiclo_reloj() {
        return ciclo_reloj;
    }

    public static void setCiclo_reloj(int ciclo_reloj) {
        Global.ciclo_reloj = ciclo_reloj;
    }

    public static boolean isActualizar() {
        return actualizar;
    }

    public static void setActualizar(boolean actualizar) {
        Global.actualizar = actualizar;
    }
    
    
}
