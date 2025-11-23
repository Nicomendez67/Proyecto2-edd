/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class NodoLista<T> {
    private T Dato;  
    private NodoLista<T> Siguiente;

    public NodoLista(T Dato) {
        this.Dato = Dato;
        this.Siguiente = null;
    }

    public T getDato() {
        return Dato;
    }

    public void setDato(T Dato) {
        this.Dato = Dato;
    }

    public NodoLista<T> getSiguiente() {
        return Siguiente;
    }

    public void setSiguiente(NodoLista<T> Siguiente) {
        this.Siguiente = Siguiente;
    }
    
    
}
