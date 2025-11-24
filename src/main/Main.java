import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.MainFrame;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {
    public static void main( String[] args ) {

        
        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb+srv://Pietro:2910@daroca.osq8bji.mongodb.net/?appName=DaRoca";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("daroca");
            MongoCollection<Document> collection = database.getCollection("categoria");

            Document doc = collection.find();
            if (doc != null) {
                System.out.println(doc.toJson());
            } else {
                System.out.println("Nada encontrado");
            }
        }
        SwingUtilities.invokeLater(() -> {
            try {
                // Tenta aplicar o look-and-feel nativo do sistema operacional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // Se não conseguir, segue com o padrão do Java
                System.err.println("Não foi possível aplicar o look-and-feel do sistema: " + ex.getMessage());
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });

        
    }
}
