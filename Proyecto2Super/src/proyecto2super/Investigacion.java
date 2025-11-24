package proyecto2super;

public class Investigacion {
    
    private String titulo;
    private String[] autores;
    private String cuerpo;
    private String[] palabrasClaves;

    public Investigacion(String titulo, String[] autores, String cuerpo, String[] palabrasClaves) {
        this.titulo = titulo;
        this.autores = autores;
        this.cuerpo = cuerpo;
        this.palabrasClaves = palabrasClaves;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String[] getAutores() {
        return autores;
    }

    public void setAutores(String[] autores) {
        this.autores = autores;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String[] getPalabrasClaves() {
        return palabrasClaves;
    }

    public void setPalabrasClaves(String[] palabrasClaves) {
        this.palabrasClaves = palabrasClaves;
    }

    public String getAutoresComoTexto() {
        if (autores == null) return "";
        String s = "";
        for (int i = 0; i < autores.length; i++) {
            s += autores[i];
            if (i < autores.length - 1) s += ", ";
        }
        return s;
    }

    public boolean contieneAutor(String autor) {
        for (String a : autores) {
            if (a.trim().equalsIgnoreCase(autor.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String claves = "";
        if (palabrasClaves != null) {
            for (int i = 0; i < palabrasClaves.length; i++) {
                claves += palabrasClaves[i];
                if (i < palabrasClaves.length - 1) claves += ", ";
            }
        }

        return "TÍTULO: " + titulo + "\n" +
               "AUTORES: " + getAutoresComoTexto() + "\n" +
               "PALABRAS CLAVE: " + claves + "\n\n" +
               "RESUMEN:\n" + cuerpo;
    }
}