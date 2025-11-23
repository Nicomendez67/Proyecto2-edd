/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class AVLPalabrasClave {
    private NodoAVL raiz;

    public AVLPalabrasClave(NodoAVL raiz) {
        this.raiz = raiz;
    }

    public NodoAVL getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoAVL raiz) {
        this.raiz = raiz;
    }
    
    /**
     * Inserta una palabra clave en el árbol AVL
     *
     * @param palabra palabra clave a insertar
     * @param inv investigación asociada a la palabra clave
     */
    public void insertar(String palabra, Investigacion inv) {
        raiz = insertarRec(raiz, palabra, inv);
    }

    /**
     * Busca una palabra clave dentro del árbol.
     *
     * @param palabra palabra clave a buscar
     * @return el nodo correspondiente o null si no existe
     */
    public NodoAVL buscar(String palabra) {
        NodoAVL actual = raiz;
        while (actual != null) {
            int cmp = palabra.compareToIgnoreCase(actual.getPalabra());
            if (cmp == 0) {
                return actual;
            } else if (cmp < 0) {
                actual = actual.getIzquierdo();
            } else {
                actual = actual.getDerecho();
            }
        }
        return null;
    }

    /**
     * Devuelve una lista de todas las palabras clave en orden alfabetico
     * Utiliza un recorrido inorden
     *
     * @return lista enlazada con las palabras en orden ascendente
     */
    public ListaEnlazada<String> recorridoInOrden() {
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        recorridoInOrdenRec(raiz, lista);
        return lista;
    }

        /**
     * Inserta recursivamente un nodo en el arbol AVL
     * Realiza las rotaciones necesarias para mantener el arbol balanceado
     *
     * @param nodo   nodo actual del recorrido
     * @param palabra palabra clave a insertar
     * @param inv investigación asociada a la palabra
     * @return el nodo actualizado después del rebalanceo
     */
    private NodoAVL insertarRec(NodoAVL nodo, String palabra, Investigacion inv) {

        // Caso base se inserta un nuevo nodo
        if (nodo == null) {
            return new NodoAVL(palabra, inv);
        }

        int cmp = palabra.compareToIgnoreCase(nodo.getPalabra());

        if (cmp < 0) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), palabra, inv));
        } else if (cmp > 0) {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), palabra, inv));
        } else {
            // La palabra ya existe entonces agregar investigación a la lista
            nodo.getInvestigaciones().agregar(inv);
            return nodo;
        }

        // Actualizar altura
        nodo.setAltura(1 + Math.max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())));

        // Obtener factor de balance
        int balance = factorBalance(nodo);

        
        
        // CASOS DE ROTACION

        // Caso Izquierda-Izquierda (LL)
        if (balance > 1 && palabra.compareToIgnoreCase(nodo.getIzquierdo().getPalabra()) < 0) {
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Derecha (RR)
        if (balance < -1 && palabra.compareToIgnoreCase(nodo.getDerecho().getPalabra()) > 0) {
            return rotacionIzquierda(nodo);
        }

        // Caso Izquierda-Derecha (LR)
        if (balance > 1 && palabra.compareToIgnoreCase(nodo.getIzquierdo().getPalabra()) > 0) {
            nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Izquierda (RL)
        if (balance < -1 && palabra.compareToIgnoreCase(nodo.getIzquierdo().getPalabra()) < 0) {
            nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
            return rotacionIzquierda(nodo);
        }

        // Nodo sin cambios
        return nodo;
    }

    /**
     * Realiza un recorrido inorden para llenar una lista con las palabras clave
     *
     * @param nodo nodo actual del recorrido
     * @param lista lista donde se almacenarán las palabras
     */
    private void recorridoInOrdenRec(NodoAVL nodo, ListaEnlazada<String> lista) {
        if (nodo != null) {
            recorridoInOrdenRec(nodo.getIzquierdo(), lista);
            lista.agregar(nodo.getPalabra());
            recorridoInOrdenRec(nodo.getDerecho(), lista);
        }
    }

    /**
     * Retorna la altura de un nodo o 0 si es null.
     *
     * @param nodo nodo a evaluar
     * @return altura del nodo
     */
    private int altura(NodoAVL nodo) {
        if (nodo == null) {
            return 0;
        }
        else{
            return nodo.getAltura();
        }
    }

    /**
     * Calcula el factor de balance de un nodo.
     *
     * @param nodo nodo a evaluar
     * @return diferencia de alturas entre subárbol izquierdo y derecho
     */
    private int factorBalance(NodoAVL nodo) {
        if (nodo == null) {
            return 0;
        }else{
            int alturaIzquierdo = altura(nodo.getIzquierdo());
            int alturaDerecho = altura(nodo.getDerecho());

            return alturaIzquierdo - alturaDerecho;
        }
    }

    /**
     * Realiza una rotación simple a la derecha.
     *
     * @param y nodo desbalanceado
     * @return nueva raíz del subárbol
     */
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.getIzquierdo();
        NodoAVL T2 = x.getDerecho();

        // Rotación
        x.setDerecho(y);
        y.setIzquierdo(T2);

        // Actualizar alturas
        y.setAltura(1 + Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())));
        x.setAltura(1 + Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())));

        return x;
    }

    /**
     * Realiza una rotación simple a la izquierda.
     *
     * @param x nodo desbalanceado
     * @return nueva raíz del subárbol
     */
    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.getDerecho();
        NodoAVL T2 = y.getIzquierdo();

        // Rotación
        y.setIzquierdo(x);
        x.setDerecho(T2);

        // Actualizar alturas
        x.setAltura(1 + Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())));
        y.setAltura(1 + Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())));

        return y;
    }
}
