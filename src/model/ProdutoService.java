package model;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Indexes.descending;

public class ProdutoService {
    private MongoCollection<Document> produtosCollection;
    
    private final String URI_DB = "mongodb+srv://cc24127_db_user:bddabeth@DarocaCluster.mongodb.net/DaRocaBD?retryWrites=true&w=majority";
    private final String DB_NAME = "DaRocaBD"; 
    private final String COLLECTION_NAME = "Produtos"; 

    public ProdutoService() {
        try {
            MongoClient mongoClient = MongoClients.create(URI_DB); 
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            this.produtosCollection = database.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao MongoDB: " + e.getMessage());
        }
    }

    private int obterProximoId() {
        Document ultimoId = produtosCollection.find()
            .sort(descending("_id"))
            .limit(1)
            .first();
        return (ultimoId != null) ? ultimoId.getInteger("_id") + 1 : 1;
    }

    public void incluir(Produto produto) {
        int proximoId = obterProximoId();
        produto.setIdProduto(proximoId); 

        Document novoDocumento = new Document("_id", proximoId)
        .append("idCategoria", produto.getCategoriaProduto())
        .append("nome", produto.getNomeProduto())
        .append("preco", produto.getPrecoProduto());
        
        try{
            produtosCollection.insertOne(novoDocumento);
            System.out.print("Insersão realizada com sucesso");
        }
        catch(Exception e){
            System.out.print("Não foi possível realizar a insersão, erro: "+e)
        }
    }

    public List<Produto> listarTodas() {
        List<Produto> produtos = new ArrayList<>();
        for (Document doc : produtosCollection.find().sort(Filters.eq("_id", 1))) { // Ordena por ID
            Produto c = new Produto(
                doc.getInteger("_id"), 
                doc.getInteger("idCategoria"),
                doc.getString("nome"),
                doc.getDouble("preco")
            );
            produtos.add(c);
        }
        return produtos;
    }
    
    public boolean alterar(Produto produto) {

        Document filtro = new Document("_id", produto.getIdProduto());
        Document dadosParaAlterar = new Document("$set", new Document("idCategoria", produto.getCategoriaProduto(),"nome", produto.getNomeProduto(), "preco", produto.getPrecoProduto()));
        try{
            UpdateResult resultado = produtosCollection.updateOne(filtro, dadosParaAlterar);
            System.out.print("Alteração realizada com sucesso");
            return resultado.getModifiedCount() > 0; // Retorna true se algo foi modificado
        }
        catch(Exception erro){
            System.out.print("Não foi possível realizar a alteração, erro: " + erro);
        }
        
        
    }

    public boolean excluir(int idProduto) {
        DeleteResult resultado = produtosCollection.deleteOne(Filters.eq("_id", idProduto));
        if (resultado.getDeletedCount() > 0) {
            System.out.print("Produto excluído com sucesso");
            return resultado.getDeletedCount() > 0;
        }
        else{
            System.out.print("Não foi possível excluir o produto");
        }
    }
    
    public Produto buscarPorId(int id) {
        Document doc = produtosCollection.find(Filters.eq("_id", id)).first();
        if (doc != null) {
            return new Produto(doc.getInteger("_id"), doc.getString("nome"));
        }
        else{
            System.out.print("Não foi possível achar o Produto!");
            return null;
        }
    }
}
