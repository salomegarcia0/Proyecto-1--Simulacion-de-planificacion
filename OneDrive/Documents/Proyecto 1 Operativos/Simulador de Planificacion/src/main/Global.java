/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import java.io.File;
import javax.swing.JOptionPane;
import Estructuras_de_Datos.*;
/**
 *
 * @author pjroj
 */
public class Global {
    private static File file;
    private static Cola inicial;
    private static Cola listo;
    private static Cola ejecutado;
    private static Cola bloqueado;
    

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

    public static Cola getEjecutado() {
        return ejecutado;
    }

    public static void setEjecutado(Cola ejecutado) {
        Global.ejecutado = ejecutado;
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
    
    
}
