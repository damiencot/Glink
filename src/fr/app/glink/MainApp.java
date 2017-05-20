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

	//Objet de Stage est le conteneur principal ( ex: fenêtre)
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
     * Retourne la fenêtre principal.
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
            //charge le layout de base à partir du fichier fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/LayoutBase.fxml"));
            LayoutBase = (BorderPane) loader.load();

            //Afficher la scène contenant le layout de base
            Scene scene = new Scene(LayoutBase);
            primaryStage.setScene(scene);

            // Donnez l'accès du contrôleur à l'application principale.
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

            //Définis la vue d'ensemble des liens dans le centre du layout de base
            LayoutBase.setCenter(LinkVueEnsemble);

            //Connexion de MainApp avec le PersonOverviewController. Permets d'accéder à l'objet MainApp et d'obtenir la liste des Link(Liens/URL)
            LinkVueEnsembleController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**

     * Ouvre une boîte de dialogue pour modifier les détails du lien/url spécifiée. Si l'utilisateur
     * Clique sur OK, les modifications sont enregistrées dans l'objet link fourni et vrai
     * Est retourné.
     *
     * @param LINK L'objet LINK à éditer
     * @return True si l'utilisateur a cliqué sur OK, false sinon.
     */
    public boolean showLinkEditDialog(Link link) {
        try {
            // Chargez le fichier fxml et créez une nouvelle étape pour la boîte de dialogue contextuelle.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LinkEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Créez le dialogue Stage.
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

            // Afficher la boîte de dialogue et attends que l'utilisateur la ferme
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Renvoie la préférence du fichier link, c'est-à-dire le fichier qui a été ouvert pour la dernière fois.
     * La préférence est lue dans le registre spécifique du système d'exploitation. Si aucune de ces préférences ne peut être trouvée,
     * null est renvoyé.
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
     * Définit le chemin du fichier actuellement chargé. Le chemin d'accès persiste dans le registre
	* spécifique du système d'exploitation.
     *
     * @param file Le fichier ou nul pour supprimer le chemin
     */
    public void setLinkFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Mettre à jour le titre de la fenêtre
            primaryStage.setTitle("Glink - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Modifie le titre de la fenêtre.
            primaryStage.setTitle("Glink");
        }
    }


    /**
     * Charge les données personnelles à partir du fichier spécifié. Les données personnelles actuelles seront remplacées.
     *
     * @param file
     */
    public void loadLinkDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(LinkListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Lecture de XML à partir du fichier et désactivation.
            LinkListWrapper wrapper = (LinkListWrapper) um.unmarshal(file);

            linkData.clear();
            linkData.addAll(wrapper.getLinks());

            // Enregistrez le chemin du fichier dans le registre.
            setLinkFilePath(file);

        } catch (Exception e) { // Attrape TOUTE exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger des données");
            alert.setContentText("Impossible de charger les données du fichier:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Enregistre les données personnelles actuelles dans le fichier spécifié.
     *
     * @param file
     */
    public void saveLinkDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(LinkListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Emballage de nos données personnelles.
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