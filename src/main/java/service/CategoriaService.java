package service;

import model.Categoria;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Sorts.ascending; 

public class CategoriaService {
    
    private MongoCollection<Document> categoriasCollection;
    private final String URI_DB = "mongodb+srv://cc24127_db_user:bddabeth@darocacluster.5kzlcbx.mongodb.net/?appName=DarocaCluster";
    private final String DB_NAME = "DaRocaBD"; 
    private final String COLLECTION_NAME = "Categorias"; 

    public CategoriaService() {
        try {
            MongoClient mongoClient = MongoClients.create(URI_DB); 
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            this.categoriasCollection = database.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Falha crítica ao conectar ao MongoDB: " + e.getMessage(), e);
        }
    }
    
    private int obterProximoId() {
        Document ultimoId = categoriasCollection.find()
            .sort(descending("_id")) 
            .limit(1)
            .first();
        return (ultimoId != null) ? ultimoId.getInteger("_id") + 1 : 1;
    }

    // Métodos de CRUD
    public void incluir(Categoria categoria) { 
        int proximoId = obterProximoId();
        categoria.setIdCategoria(proximoId);
        
        Document novoDocumento = new Document("_id", proximoId)
            .append("nome", categoria.getNomeCategoria());

        categoriasCollection.insertOne(novoDocumento);
    }

    public List<Categoria> listarTodas() {
        List<Categoria> categorias = new ArrayList<>();
        for (Document doc : categoriasCollection.find().sort(ascending("_id"))) { 
            Categoria c = new Categoria(
                doc.getInteger("_id"), 
                doc.getString("nome")
            );
            categorias.add(c);
        }
        return categorias;
    }
    
    public boolean alterar(Categoria categoria) {
        Document filtro = new Document("_id", categoria.getIdCategoria());
        
        Document dadosParaAlterar = new Document("$set", new Document("nome", categoria.getNomeCategoria()));

        UpdateResult resultado = categoriasCollection.updateOne(filtro, dadosParaAlterar);

        return resultado.getModifiedCount() > 0;
    }

    public boolean excluir(int idCategoria) {
        DeleteResult resultado = categoriasCollection.deleteOne(Filters.eq("_id", idCategoria));
        return resultado.getDeletedCount() > 0;
    }
    
    public Categoria buscarPorId(int id) { 
        Document doc = categoriasCollection.find(Filters.eq("_id", id)).first();
        if (doc != null) {
            return new Categoria(doc.getInteger("_id"), doc.getString("nome"));
        }
        return null;
    }
}