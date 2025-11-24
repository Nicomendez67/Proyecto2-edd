/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class AnalizadorDeResumen {
    /**
     * Limpia el texto eliminando caracteres especiales básicos.
     *
     * @param texto texto original
     * @return texto en minúsculas y sin signos de puntuación
     */
    public String limpiarTexto(String texto) {

        if (texto == null) {
            return "";
        }

        String limpio = texto.toLowerCase();

        limpio = limpio.replace(".", " ");
        limpio = limpio.replace(",", " ");
        limpio = limpio.replace(";", " ");
        limpio = limpio.replace(":", " ");
        limpio = limpio.replace("?", " ");
        limpio = limpio.replace("!", " ");
        limpio = limpio.replace("(", " ");
        limpio = limpio.replace(")", " ");
        limpio = limpio.replace("\n", " ");

        return limpio;
    }

    /**
     * Convierte el texto en una lista de palabras.
     *
     * @param texto texto limpio
     * @return lista de palabras
     */
    public ListaEnlazada<String> obtenerPalabras(String texto) {

        ListaEnlazada<String> palabras = new ListaEnlazada<>();

        if (texto == null) {
            return palabras;
        }

        String[] partes = texto.split(" ");

        for (int i = 0; i < partes.length; i++) {

            String p = partes[i].trim();

            if (p.length() > 0) {
                palabras.agregar(p);
            }
        }

        return palabras;
    }

    /**
     * Cuenta cuántas veces aparece cada palabra.
     *
     * @param texto texto original
     * @return lista de objetos PalabraFrecuencia
     */
    public ListaEnlazada<PalabraFrecuencia> contarFrecuencias(String texto) {

        ListaEnlazada<PalabraFrecuencia> listaFrecuencias = new ListaEnlazada<>();

        if (texto == null) {
            return listaFrecuencias;
        }

        String limpio = limpiarTexto(texto);
        ListaEnlazada<String> palabras = obtenerPalabras(limpio);

        int tam = palabras.tamano();

        for (int i = 0; i < tam; i++) {

            String palabra = palabras.obtener(i);

            PalabraFrecuencia pf = buscarFrecuencia(listaFrecuencias, palabra);

            if (pf == null) {

                PalabraFrecuencia nueva = new PalabraFrecuencia(palabra, 1);
                listaFrecuencias.agregar(nueva);

            } else {

                int nuevaCant = pf.getFrecuencia() + 1;
                pf.setFrecuencia(nuevaCant);
            }
        }

        return listaFrecuencias;
    }

    /**
     * Busca dentro de una lista de frecuencias si una palabra ya existe.
     *
     * @param lista lista donde se buscará
     * @param palabra palabra que se desea encontrar
     * @return objeto PalabraFrecuencia si existe, o null si no
     */
    private PalabraFrecuencia buscarFrecuencia(ListaEnlazada<PalabraFrecuencia> lista, String palabra) {

        int tam = lista.tamano();

        for (int i = 0; i < tam; i++) {

            PalabraFrecuencia pf = lista.obtener(i);

            if (pf.getPalabra().equals(palabra)) {
                return pf;
            }
        }

        return null;
    }

    /**
     * Obtiene las N palabras más frecuentes del resumen.
     *
     * @param texto texto original
     * @param n cantidad de palabras a retornar
     * @return lista con las n más repetidas
     */
    public ListaEnlazada<PalabraFrecuencia> obtenerTopFrecuencias(String texto, int n) {

        ListaEnlazada<PalabraFrecuencia> todas = contarFrecuencias(texto);
        ListaEnlazada<PalabraFrecuencia> top = new ListaEnlazada<>();

        for (int i = 0; i < n; i++) {

            PalabraFrecuencia mayor = obtenerMayor(todas);

            if (mayor != null) {
                top.agregar(mayor);
                todas.eliminar(mayor);
            }
        }

        return top;
    }

    /**
     * Obtiene el elemento con mayor frecuencia dentro de una lista.
     *
     * @param lista lista de palabras con frecuencia
     * @return la palabra con mayor frecuencia o null si la lista está vacía
     */
    private PalabraFrecuencia obtenerMayor(ListaEnlazada<PalabraFrecuencia> lista) {

        PalabraFrecuencia mayor = null;

        int tam = lista.tamano();

        for (int i = 0; i < tam; i++) {

            PalabraFrecuencia pf = lista.obtener(i);

            if (mayor == null) {
                mayor = pf;
            } else {

                if (pf.getFrecuencia() > mayor.getFrecuencia()) {
                    mayor = pf;
                }
            }
        }

        return mayor;
    }
}
