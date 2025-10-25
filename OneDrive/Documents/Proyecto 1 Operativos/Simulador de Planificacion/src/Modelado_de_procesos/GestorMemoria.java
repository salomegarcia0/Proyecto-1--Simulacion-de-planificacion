/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelado_de_procesos;
import java.util.concurrent.Semaphore;

/**
 *
 * @author salom
 */
public class GestorMemoria {
    private int memoriaTotal;  //memoria del sistema
    private int memoriaUsada;  //memoria que ya esta siendo ocupada
    private final Semaphore semaforoMemoria = new Semaphore(1);
   

    public GestorMemoria() {
        this.memoriaTotal = 1000; // GB de memoria total 
        this.memoriaUsada = 0;
    }
    
    /**
     * esta funcion solo actualiza el contador de la memoria utilizada por el gestor de memoria no del proceso
     * @param proceso 
     */
    public void asignarMemoria(PCB proceso){
        try{
            semaforoMemoria.acquire();
            memoriaUsada += proceso.getMemoria(); 
            System.out.println("Memoria USada:" + memoriaUsada); //borrar
        } catch (InterruptedException e){
            throw new RuntimeException("Error asignando memoria", e);
        }finally{
            semaforoMemoria.release();
        } 
    }
    
    /**
     * esta es para limpiar la memoria luego de que sale un proceso que se esta ejecutando
     * @param proceso 
     */
    public void limpiarMemoria(PCB proceso){
        
        try{
            semaforoMemoria.acquire();
            memoriaUsada -= proceso.getMemoria();
            System.out.println("Memoria USada:" + memoriaUsada);//borrar
        } catch (InterruptedException e){
            throw new RuntimeException("Error asignando memoria", e);
        }finally{
            semaforoMemoria.release();
        }   
    }
    
    /**
     * esta es para limpiar la memoria luego de que sale un proceso que se esta ejecutando pero es usado en una funcion
     * de PCB
     */
    public void limpiarMemoriaProceso(int memoriaProceso){
        memoriaUsada -= memoriaProceso;
        System.out.println("Memoria liberada");
        System.out.println("Memoria USada:" + memoriaUsada);//borrar
    }
    
    /**
     * Este verifica si el proceso puede entrar a la memoria basandose en la cantidad de memoria
     * @param proceso
     * @return 
     */
    public boolean puedeEntrarAMemoria(PCB proceso){
        try{
            semaforoMemoria.acquire();
            return (memoriaUsada + proceso.getMemoria())<= memoriaTotal;
        } catch (InterruptedException e){
            throw new RuntimeException("Error asignando memoria", e);
        }finally{
            semaforoMemoria.release();
        }
         
    }     

    public int getMemoriaTotal() {
        return memoriaTotal;
    }

    public int getMemoriaUsada() {
        return memoriaUsada;
    }
    
    /**
     * para saber cuanta memoria queda disponible y asi gestionar los procesos en MP
     * @return 
     */
    public int getMemoriaDisponible(){
        return memoriaTotal - memoriaUsada;
    }

    public void setMemoriaTotal(int memoriaTotal) {
        this.memoriaTotal = memoriaTotal;
    }

    public void setMemoriaUsada(int memoriaUsada) {
        this.memoriaUsada = memoriaUsada;
    }
    
    
}
