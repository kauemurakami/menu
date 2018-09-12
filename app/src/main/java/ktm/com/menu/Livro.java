package ktm.com.menu;

public class Livro {
    private String nome;
    private String autor;
    private String linkLivro;
    private int likes;
    private int qtPaginas;

    public Livro(String nome, String autor, int qtPaginas) {
        this.nome = nome;
        this.autor = autor;
        this.qtPaginas = qtPaginas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getLinkLivro() {
        return linkLivro;
    }

    public void setLinkLivro(String linkLivro) {
        this.linkLivro = linkLivro;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getQtPaginas() {
        return qtPaginas;
    }

    public void setQtPaginas(int qtPaginas) {
        this.qtPaginas = qtPaginas;
    }
}
