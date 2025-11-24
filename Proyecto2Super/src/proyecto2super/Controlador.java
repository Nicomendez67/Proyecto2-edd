/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2super;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;   // ← agregado para validación del archivo

/**
 *
 * @author Nicolas Mendez, Antonio Yibirin
 */
public class Controlador {
    
    private HashTableInvestigaciones tablaInvestigaciones;
    private AVL<String> avlAutores;
    private AVL<String> avlPalabrasClave;
    
    public Controlador() {
        this.tablaInvestigaciones = new HashTableInvestigaciones();
        this.avlAutores = new AVL<>();
        this.avlPalabrasClave = new AVL<>();
    }
    
    
    /**
    * Normaliza un texto para comparación y almacenamiento.
    * Aplica trim(), convierte a minúsculas y reduce múltiples espacios a uno solo.
    *
    * @param s cadena original
    * @return cadena normalizada o null si la entrada es null
    */
    private String normalizar(String s) {
        if (s == null) return null;
        String t = s.trim().toLowerCase();
        t = t.replaceAll("\\s+", " ");  // colapsar múltiples espacios
        return t;
    }
    
    
    /**
     * Inserta una investigación en todas las estructuras.
     *                                                                          
     * @param inv investigación a insertar
     * @return true si se insertó, false si ya existía
     */
    public boolean insertarInvestigacion(Investigacion inv) {
        if (inv == null) {
            return false;
        }

        String titulo = inv.getTitulo();
        if (titulo == null) {
            return false;
        }

        boolean insertado = this.tablaInvestigaciones.insertar(titulo, inv);
        if (!insertado) {
            return false;
        }

        // Insertar autores en el AVL
        String[] autores = inv.getAutores();
        if (autores != null) {
            for (int i = 0; i < autores.length; i++) {
                String autor = autores[i];
                if (autor != null) {

                    // *** NORMALIZACIÓN OBLIGATORIA ***
                    autor = normalizar(autor);

                    // evitar vacíos
                    if (!autor.isEmpty()) {
                        this.avlAutores.insertar(autor, inv);
                    }
                }
            }
        }

        // Insertar palabras clave en el AVL
        String[] claves = inv.getPalabrasClaves();
        if (claves != null) {
            for (int i = 0; i < claves.length; i++) {
                String clave = claves[i];
                if (clave != null) {

                    // *** NORMALIZACIÓN OBLIGATORIA ***
                    clave = normalizar(clave);

                    // evitar vacíos
                    if (!clave.isEmpty()) {
                        this.avlPalabrasClave.insertar(clave, inv);
                    }
                }
            }
        }

        return true;
    }
    
    
    /**
     * Busca una investigación por su título.
     *
     * @param titulo título
     * @return investigación o null si no existe
     */
    public Investigacion buscarPorTitulo(String titulo) {
        if (titulo == null) {
            return null;
        }
        return this.tablaInvestigaciones.buscar(titulo);
    }
    
    
    /**
     * Obtiene todas las investigaciones asociadas a un autor.
     *
     * @param autor nombre del autor
     * @return lista de investigaciones o null si el autor no existe
     */
    public ListaEnlazada<Investigacion> buscarPorAutor(String autor) {
        if (autor == null) {
            return null;
        }
        return this.avlAutores.buscar(autor);
    }
    
    
    /**
     * Obtiene todas las investigaciones asociadas a una palabra clave.
     *
     * @param palabra palabra clave
     * @return lista de investigaciones o null si no existe
     */
    public ListaEnlazada<Investigacion> buscarPorPalabraClave(String palabra) {
        if (palabra == null) {
            return null;
        }
        return this.avlPalabrasClave.buscar(palabra);
    }
    
    
    /**
     * Obtiene todas las investigaciones (sin orden).
     *
     * @return lista de investigaciones
     */
    public ListaEnlazada<Investigacion> obtenerTodasLasInvestigaciones() {
        return this.tablaInvestigaciones.listarInvestigaciones();
    }
    
    
    /**
    * Obtiene todas las palabras clave almacenadas en el AVL,
    * ordenadas alfabéticamente mediante recorrido inorden.
    *
    * @return lista enlazada con palabras clave en orden ascendente
    */

    public ListaEnlazada<String> listarPalabrasClaveOrdenadas() {
        return this.avlPalabrasClave.recorridoInOrden();
    }
    
    
    /**
    * Recupera todos los autores almacenados en el AVL correspondiente,
    * ordenados alfabéticamente mediante recorrido inorden.
    *
    * @return lista enlazada de autores ordenados
    */

    public ListaEnlazada<String> listarAutoresOrdenados() {
        return this.avlAutores.recorridoInOrden();
    }
    
    
   /**
    * Guarda todas las investigaciones en un archivo de texto,
    *
    * @param ruta ruta completa donde se guardará el archivo
    * @throws Exception si ocurre algún error de escritura
    */

    public void guardarEnArchivo(String ruta) throws Exception {
        this.tablaInvestigaciones.guardarEnArchivo(ruta);
    }
    
    
    /**
     * Carga investigaciones desde un archivo y las reinserta en todas las estructuras.
     *
     * @param ruta ruta del archivo a leer
     * @throws Exception si ocurre un error de lectura
     */

    public void cargarDesdeArchivo(String ruta) throws Exception {
        this.tablaInvestigaciones = new HashTableInvestigaciones();
        this.avlAutores = new AVL<>();
        this.avlPalabrasClave = new AVL<>();

        this.tablaInvestigaciones.cargarDesdeArchivo(ruta);

        ListaEnlazada<Investigacion> lista = this.tablaInvestigaciones.listarInvestigaciones();
        int tam = lista.tamano();

        for (int i = 0; i < tam; i++) {
            Investigacion inv = lista.obtener(i);
            if (inv == null) continue;


            // Insertar autores en el AVL de autores
            String[] autores = inv.getAutores();
            if (autores != null) {
                for (int a = 0; a < autores.length; a++) {
                    String autor = autores[a];
                    if (autor != null) {
                        autor = normalizar(autor);
                        if (!autor.isEmpty()) {
                            this.avlAutores.insertar(autor, inv);
                        }
                    }
                }
            }

            // Insertar palabras clave en el AVL de palabras clave
            String[] claves = inv.getPalabrasClaves();
            if (claves != null) {
                for (int k = 0; k < claves.length; k++) {
                    String clave = claves[k];
                    if (clave != null) {
                        clave = normalizar(clave);
                        if (!clave.isEmpty()) {
                            this.avlPalabrasClave.insertar(clave, inv);
                        }
                    }
                }
            }
        }

    }
    
    
    /**
     * Carga un archivo de texto con artículos.
     *
     * @param ruta ruta del archivo
     * @return true si se cargó bien
     */
    public boolean cargarArchivo(String ruta) {

        // *** VALIDACIÓN OBLIGATORIA DEL ARCHIVO ***
        File f = new File(ruta);
        if (!f.exists() || f.length() == 0) {
            System.out.println("Archivo inválido o vacío: " + ruta);
            return false;
        }

        int cargados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea;
            String titulo = null;
            String autores = null;
            String claves = null;
            StringBuilder resumenSb = new StringBuilder();
            boolean leyendoResumen = false;

            while ((linea = br.readLine()) != null) {

                linea = linea.trim();
                if (linea.isEmpty()) continue;

                if (linea.startsWith("TITULO:")) {
                    titulo = linea.substring("TITULO:".length()).trim();
                    leyendoResumen = false;
                    resumenSb.setLength(0);

                } else if (linea.startsWith("AUTORES:")) {
                    autores = linea.substring("AUTORES:".length()).trim();
                    leyendoResumen = false;

                } else if (linea.startsWith("PALABRAS:")) {
                    claves = linea.substring("PALABRAS:".length()).trim();
                    leyendoResumen = false;

                } else if (linea.startsWith("RESUMEN:")) {
                    String after = linea.substring("RESUMEN:".length()).trim();
                    resumenSb.setLength(0);
                    resumenSb.append(after);
                    leyendoResumen = true;

                } else if (linea.equals("FIN")) {

                    if (titulo == null || autores == null || claves == null) {
                        System.out.println("Artículo inválido omitido (faltan campos): " + titulo);
                    } else {

                        String[] listaAutores = autores.split(",");
                        String[] listaClaves = claves.split(",");
                        String resumen = resumenSb.toString();

                        Investigacion inv = new Investigacion(titulo, listaAutores, resumen, listaClaves);
                        boolean ok = insertarInvestigacion(inv);

                        if (ok) cargados++;
                    }

                    titulo = null;
                    autores = null;
                    claves = null;
                    resumenSb.setLength(0);
                    leyendoResumen = false;

                } else {

                    if (leyendoResumen) {
                        if (resumenSb.length() > 0) resumenSb.append("\n");
                        resumenSb.append(linea);
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("Error leyendo archivo: " + ex.getMessage());
            return false;
        }

        return cargados > 0;
    }
    
    
    /**
     * Obtiene todos los autores en orden ascendente (inorden del AVL).
     *
     * @return ListaEnlazada<String> (puede ser vacía)
     */
    public ListaEnlazada<String> listarAutores() {
        return this.avlAutores.recorridoInOrden();
    }
    
    
    /**
     * Recupera todos los títulos disponibles (recorriendo la tabla de dispersión).
     *
     * @return ListaEnlazada<String> con títulos (puede ser vacía)
     */
    public ListaEnlazada<String> listarTitulos() {

        ListaEnlazada<String> lista = new ListaEnlazada<String>();
        ListaEnlazada<Investigacion> invs = this.tablaInvestigaciones.listarInvestigaciones();

        if (invs == null) {
            return lista;
        }

        int tam = invs.tamano();

        for (int i = 0; i < tam; i++) {

            Investigacion inv = invs.obtener(i);

            if (inv != null && inv.getTitulo() != null) {
                lista.agregar(inv.getTitulo());
            }
        }

        return lista;
    }
    
    
    /**
    * Analiza un resumen y determina la frecuencia de aparición de las palabras clave registradas en el sistema.
    *
    * @param texto contenido del resumen a analizar
    * @return lista de objetos PalabraFrecuencia ordenados por frecuencia descendente
    */

    public ListaEnlazada<PalabraFrecuencia> analizarResumen(String texto) {
        ListaEnlazada<PalabraFrecuencia> resultado = new ListaEnlazada<>();

        if (texto == null || texto.isEmpty()) {
            return resultado;
        }

        // Pasamos todo a minúsculas para evitar errores de coincidencia
        String resumen = texto.toLowerCase();

        // 1. Obtener TODAS las palabras clave registradas en el AVL global
        ListaEnlazada<String> claves = this.avlPalabrasClave.recorridoInOrden();
        int totalClaves = claves.tamano();

        // Arreglo temporal para ordenar
        PalabraFrecuencia[] arreglo = new PalabraFrecuencia[totalClaves];
        int count = 0;

        // 2. Contar cuántas veces aparece cada palabra clave en el resumen
        for (int i = 0; i < totalClaves; i++) {
            String clave = claves.obtener(i).toLowerCase();

            if (clave == null || clave.trim().isEmpty()) {
                continue;
            }

            int frecuencia = contarOcurrencias(resumen, clave);

            if (frecuencia > 0) {
                arreglo[count++] = new PalabraFrecuencia(clave, frecuencia);
            }
        }

        // Si no hubo coincidencias, terminamos
        if (count == 0) {
            return resultado;
        }

        // 3. Recortar arreglo a tamaño real
        PalabraFrecuencia[] finalArr = new PalabraFrecuencia[count];
        System.arraycopy(arreglo, 0, finalArr, 0, count);

        // 4. Ordenar por frecuencia DESCENDENTE (burbuja sencilla para no complicar)
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (finalArr[j].getFrecuencia() < finalArr[j + 1].getFrecuencia()) {
                    PalabraFrecuencia temp = finalArr[j];
                    finalArr[j] = finalArr[j + 1];
                    finalArr[j + 1] = temp;
                }
            }
        }

        // 5. Insertar solo el Top 10 en la lista
        int limite = Math.min(10, count);
        for (int i = 0; i < limite; i++) {
            resultado.agregar(finalArr[i]);
        }

        return resultado;
    }
    
    
    /**
    * Cuenta cuántas veces aparece una palabra dentro de un texto dado.
    * Utiliza búsqueda de subcadenas.
    *
    * @param texto texto donde buscar
    * @param palabra palabra clave a contabilizar
    * @return número de apariciones encontradas
    */
    private int contarOcurrencias(String texto, String palabra) {
        int contador = 0;
        int index = 0;

        while ((index = texto.indexOf(palabra, index)) != -1) {
            contador++;
            index += palabra.length();
        }

        return contador;
    }




    
    /**
    * Construye un reporte en formato texto plano que lista todos los autores y las investigaciones asociadas a cada uno.
    *
    * @return cadena con el reporte formateado
    */

    public String generarReporteAutores() {
        StringBuilder sb = new StringBuilder();
        ListaEnlazada<String> autores = listarAutoresOrdenados();
        for (int i = 0; i < autores.tamano(); i++) {
            String autor = autores.obtener(i);
            ListaEnlazada<Investigacion> invs = buscarPorAutor(autor);
            sb.append("Autor: ").append(autor).append("\n");
            if (invs == null || invs.tamano() == 0) {
                sb.append("  (sin investigaciones)\n\n");
                continue;
            }
            for (int j = 0; j < invs.tamano(); j++) {
                sb.append("  - ").append(invs.obtener(j).getTitulo()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    
    
}