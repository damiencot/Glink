package fr.app.glink;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fr.app.glink.model.Link;
import fr.app.glink.model.LinkListWrapper;
import fr.app.glink.view.LayoutBaseController;
import fr.app.glink.view.LinkEditDialogController;
import fr.app.glink.view.LinkVueEnsembleController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

	//Objet de Stage est le conteneur principal ( ex: fen�tre)
    private Stage primaryStage;
    private BorderPane LayoutBase;
    private ObservableList<Link> linkData = FXCollections.observableArrayList();



    /*
     * Constructeur
     */
    public MainApp() {
		linkData.add(new Link("Livre 1 ", "Lien 1"));
		linkData.add(new Link("Novel 2", "Lien 2"));
	}

    /**
     * Retourne la fen�tre principal.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }



	public ObservableList<Link> getLinkData() {
		return linkData;
	}

	@Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Glink");

        initRootLayout();

        showLinkOverview();
    }

    /**
     * Initialise le layout de base.
     */

    public void initRootLayout() {
        try {
            //charge le layout de base � partir du fichier fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/LayoutBase.fxml"));
            LayoutBase = (BorderPane) loader.load();

            //Afficher la sc�ne contenant le layout de base
            Scene scene = new Scene(LayoutBase);
            primaryStage.setScene(scene);

            // Donnez l'acc�s du contr�leur � l'application principale.
            LayoutBaseController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Essayez de charger le dernier fichier de personne ouvert.
        File file = getLinkFilePath();
        if (file != null) {
            loadLinkDataFromFile(file);
        }
    }

    ////
    /**
     * Affiche la vue d'ensemble du lien dans le layout de base.
     */
    public void showLinkOverview() {
        try {
            // Charge la vue d'ensemble des URL
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LinkVueEnsemble.fxml"));
            AnchorPane LinkVueEnsemble = (AnchorPane) loader.load();

            //D�finis la vue d'ensemble des liens dans le centre du layout de base
            LayoutBase.setCenter(LinkVueEnsemble);

            //Connexion de MainApp avec le PersonOverviewController. Permets d'acc�der � l'objet MainApp et d'obtenir la liste des Link(Liens/URL)
            LinkVueEnsembleController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**

     * Ouvre une bo�te de dialogue pour modifier les d�tails du lien/url sp�cifi�e. Si l'utilisateur
     * Clique sur OK, les modifications sont enregistr�es dans l'objet link fourni et vrai
     * Est retourn�.
     *
     * @param LINK L'objet LINK � �diter
     * @return True si l'utilisateur a cliqu� sur OK, false sinon.
     */
    public boolean showLinkEditDialog(Link link) {
        try {
            // Chargez le fichier fxml et cr�ez une nouvelle �tape pour la bo�te de dialogue contextuelle.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LinkEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Cr�ez le dialogue Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set le lien dans le controller
            LinkEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLink(link);

            // Afficher la bo�te de dialogue et attends que l'utilisateur la ferme
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Renvoie la pr�f�rence du fichier link, c'est-�-dire le fichier qui a �t� ouvert pour la derni�re fois.
     * La pr�f�rence est lue dans le registre sp�cifique du syst�me d'exploitation. Si aucune de ces pr�f�rences ne peut �tre trouv�e,
     * null est renvoy�.
     *
     * @return
     */
    public File getLinkFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * D�finit le chemin du fichier actuellement charg�. Le chemin d'acc�s persiste dans le registre
	* sp�cifique du syst�me d'exploitation.
     *
     * @param file Le fichier ou nul pour supprimer le chemin
     */
    public void setLinkFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Mettre � jour le titre de la fen�tre
            primaryStage.setTitle("Glink - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Modifie le titre de la fen�tre.
            primaryStage.setTitle("Glink");
        }
    }


    /**
     * Charge les donn�es personnelles � partir du fichier sp�cifi�. Les donn�es personnelles actuelles seront remplac�es.
     *
     * @param file
     */
    public void loadLinkDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(LinkListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Lecture de XML � partir du fichier et d�sactivation.
            LinkListWrapper wrapper = (LinkListWrapper) um.unmarshal(file);

            linkData.clear();
            linkData.addAll(wrapper.getLinks());

            // Enregistrez le chemin du fichier dans le registre.
            setLinkFilePath(file);

        } catch (Exception e) { // Attrape TOUTE exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger des donn�es");
            alert.setContentText("Impossible de charger les donn�es du fichier:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Enregistre les donn�es personnelles actuelles dans le fichier sp�cifi�.
     *
     * @param file
     */
    public void saveLinkDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(LinkListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Emballage de nos donn�es personnelles.
            LinkListWrapper wrapper = new LinkListWrapper();
            wrapper.setLinks(linkData);

            // Marquer et enregistrer XML dans le fichier..
            m.marshal(wrapper, file);

            // Enregistrez le chemin du fichier dans le registre.
            setLinkFilePath(file);
        } catch (Exception e) { // Attrape TOUTE exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de sauvegarder");
            alert.setContentText("Impossible de sauvegarder de le fichier:\n" + file.getPath());

            alert.showAndWait();
        }
    }


}