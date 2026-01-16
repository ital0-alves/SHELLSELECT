package pdv;

public class Cliente {
    private String nome;
    private String cpf;
    private int pontos;

    public Cliente(String nome, String cpf, int pontos) {
        this.nome = nome;
        this.cpf = cpf;
        this.pontos = pontos;
    }

   
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public int getPontos() { return pontos; }
}