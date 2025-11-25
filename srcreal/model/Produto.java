package model;

public class Produto {
    
    private int idProduto;
    private String nomeProduto;
    private Double precoProduto;
    private int idCategoria; // Alterado de String para int para bater com o banco
    
    // Construtor vazio (boa prática)
    public Produto() {
    }

    // Construtor completo
    public Produto(int idProduto, String nomeProduto, Double precoProduto, int idCategoria) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.idCategoria = idCategoria;
    }

    // Construtor parcial (para o buscarPorId, se necessário, embora seja melhor trazer tudo)
    public Produto(int idProduto, String nomeProduto) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(Double precoProduto) {
        this.precoProduto = precoProduto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }
    
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    } 

    @Override
    public String toString() {
        return "Produto [idProduto=" + idProduto + ", nomeProduto=" + nomeProduto + 
               ", precoProduto=" + precoProduto + ", idCategoria=" + idCategoria + "]";
    }
}