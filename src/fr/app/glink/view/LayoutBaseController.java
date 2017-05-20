package fr.app.glink.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import fr.app.glink.MainApp;

public class LayoutBaseController {


	//Référence à la class principale
    private MainApp mainApp;

    /**
     * setMainAp est appelé par l'application principale pour se référer à lui-même.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Crée un carnet de lien vide.
     */
    @FXML
    private void handleNew() {
        mainApp.getLinkData().clear();
        mainApp.setLinkFilePath(null);
    }

    /**
     * Ouvre un FileChooser pour permettre à l'utilisateur de sélectionner un carnet de lien à charger.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        //Définir le filtre d'extension
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue "Enregistrer le fichier"
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadLinkDataFromFile(file);
        }
    }

    /**
     * Enregistre le fichier dans le fichier link qui est actuellement ouvert. S'il n'y a pas de fichier ouvert, la boîte de dialogue "Enregistrer sous" s'affiche.
     */
    @FXML
    private void handleSave() {
        File linkFile = mainApp.getLinkFilePath();
        if (linkFile != null) {
            mainApp.saveLinkDataToFile(linkFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Ouvre un FileChooser pour permettre à l'utilisateur de sélectionner un fichier à enregistrer..
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        //Définir le filtre d'extension
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue "Enregistrer le fichier"
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Verifie qu'il possède l'extension correcte
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveLinkDataToFile(file);
        }
    }

    /**
     * Ouverture de la fenêtre about.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Glink");
        alert.setHeaderText("Information");
        alert.setContentText("Auteur: Personne");

        alert.showAndWait();
    }

    /**
     * Ferme l'application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

}
