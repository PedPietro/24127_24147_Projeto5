package service;

import model.Produto;
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
        try {
            Document ultimoId = produtosCollection.find()
                .sort(descending("_id"))
                .limit(1)
                .first();
            return (ultimoId != null) ? ultimoId.getInteger("_id") + 1 : 1;
        } catch (Exception e) {
            return 1;
        }
    }

    public void incluir(Produto produto) {
        int proximoId = obterProximoId();
        produto.setIdProduto(proximoId); 
        Document novoDocumento = new Document("_id", proximoId)
            .append("idCategoria", produto.getIdCategoria()) 
            .append("nome", produto.getNomeProduto())
            .append("preco", produto.getPrecoProduto());
        
        try {
            produtosCollection.insertOne(novoDocumento);
            System.out.println("Inserção realizada com sucesso");
        } catch(Exception e) {
            System.out.println("Não foi possível realizar a inserção, erro: " + e);
        }
    }

    public List<Produto> listarTodas() {
        List<Produto> produtos = new ArrayList<>();
        try {
            for (Document doc : produtosCollection.find().sort(Filters.eq("_id", 1))) {
                Produto p = new Produto(
                    doc.getInteger("_id"), 
                    doc.getString("nome"),
                    doc.getDouble("preco"),
                    doc.getInteger("idCategoria")
                );
                produtos.add(p);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar: " + e);
        }
        return produtos;
    }
    
    public boolean alterar(Produto produto) {
        Document filtro = new Document("_id", produto.getIdProduto());
        
        Document dadosParaAlterar = new Document("$set", new Document()
                .append("idCategoria", produto.getIdCategoria())
                .append("nome", produto.getNomeProduto())
                .append("preco", produto.getPrecoProduto()));
        try {
            UpdateResult resultado = produtosCollection.updateOne(filtro, dadosParaAlterar);
            if(resultado.getModifiedCount() > 0){
                System.out.println("Alteração realizada com sucesso");
                return true;
            }
        } catch(Exception erro) {
            System.out.println("Não foi possível realizar a alteração, erro: " + erro);
        }
        
        return false; 
    }

    public boolean excluir(int idProduto) {
        try {
            DeleteResult resultado = produtosCollection.deleteOne(Filters.eq("_id", idProduto));
            if (resultado.getDeletedCount() > 0) {
                System.out.println("Produto excluído com sucesso");
                return true;
            } else {
                System.out.println("Não foi possível excluir o produto");
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir: " + e);
        }
        return false;
    }
    
    public Produto buscarPorId(int id) {
        try {
            Document doc = produtosCollection.find(Filters.eq("_id", id)).first();
            if (doc != null) {
                return new Produto(
                    doc.getInteger("_id"), 
                    doc.getString("nome"),
                    doc.getDouble("preco"),
                    doc.getInteger("idCategoria")
                );
            } else {
                System.out.println("Não foi possível achar o Produto!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e);
            return null;
        }

    }
}