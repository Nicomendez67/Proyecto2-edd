/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class NodoAVL {
    
    private String palabra;
    private ListaEnlazada<Investigacion> investigaciones;
    private int altura;
    private NodoAVL izquierdo;
    private NodoAVL derecho;

        
    NodoAVL(String palabra, Investigacion inv) {
        this.palabra = palabra;
        this.investigaciones = new ListaEnlazada<>();
        this.investigaciones.agregar(inv);
        this.altura = 1;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public ListaEnlazada<Investigacion> getInvestigaciones() {
        return investigaciones;
    }

    public void setInvestigaciones(ListaEnlazada<Investigacion> investigaciones) {
        this.investigaciones = investigaciones;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public NodoAVL getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVL izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoAVL getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVL derecho) {
        this.derecho = derecho;
    }
    
    
}
