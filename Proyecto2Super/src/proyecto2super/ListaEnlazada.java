/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class ListaEnlazada<T> {
    private NodoLista cabeza;
    private int size;

    public ListaEnlazada(NodoLista cabeza, int size) {
        this.cabeza = cabeza;
        this.size = size;
    }

    public NodoLista getCabeza() {
        return cabeza;
    }

    public void setCabeza(NodoLista cabeza) {
        this.cabeza = cabeza;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Agrega un elemento al final de la lista enlazada.
     *
     * @param dato elemento que se desea añadir
     */
    public void agregar(T dato) {
        NodoLista<T> nuevo = new NodoLista<T>(dato);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoLista<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        size++;
    }

    /**
     * Verifica si un elemento existe en la lista.
     *
     * @param dato elemento a buscar
     * @return true si el elemento está en la lista, false en caso contrario
     */
    public boolean contiene(T dato) {
        NodoLista<T> actual = cabeza;

        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Obtiene el elemento almacenado en una posición específica.
     *
     * @param index posición (comenzando desde 0) del elemento a recuperar
     * @return el elemento en esa posición o null si el índice es inválido
     */
    public T obtener(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        NodoLista<T> actual = cabeza;
        int contador = 0;

        while (actual != null) {
            if (contador == index) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
            contador++;
        }
        return null;
    }

    /**
     * Devuelve la cantidad de elementos almacenados en la lista.
     *
     * @return tamaño de la lista
     */
    public int tamaño() {
        return size;
    }

    
}
