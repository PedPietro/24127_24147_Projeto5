package model;

public class Produto {
    
    private int idProduto;
    private String nomeProduto;
    private Double precoProduto;
    private String categoriaProduto;
    
    public Produto(int idProduto, String nomeProduto, Double precoProduto, String categoriaProduto) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.categoriaProduto = categoriaProduto;
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

    public String getCategoriaProduto() {
        return categoriaProduto;
    }
    
    public void setCategoriaProduto(String categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    } 

    @Override
    public String toString() {
        return "Produto [idProduto=" + idProduto + ", nomeProduto=" + nomeProduto + ", precoProduto=" + precoProduto
                + ", categoriaProduto=" + categoriaProduto + "]";
    }
}