/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class NodoAVL<T> {

    private T clave;
    private ListaEnlazada<Investigacion> investigaciones;
    private int altura;
    private NodoAVL<T> izquierdo;
    private NodoAVL<T> derecho;


    public NodoAVL(T clave, Investigacion inv) {
        this.clave = clave;
        this.investigaciones = new ListaEnlazada<>();
        this.investigaciones.agregar(inv);
        this.altura = 1;
        this.izquierdo = null;
        this.derecho = null;
    }


    public T getClave() {
        return clave;
    }

    public void setClave(T clave) {
        this.clave = clave;
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

    public NodoAVL<T> getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVL<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoAVL<T> getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVL<T> derecho) {
        this.derecho = derecho;
    }
}