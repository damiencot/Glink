package fr.app.glink.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import fr.app.glink.MainApp;

public class LayoutBaseController {


	//R�f�rence � la class principale
    private MainApp mainApp;

    /**
     * setMainAp est appel� par l'application principale pour se r�f�rer � lui-m�me.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Cr�e un carnet de lien vide.
     */
    @FXML
    private void handleNew() {
        mainApp.getLinkData().clear();
        mainApp.setLinkFilePath(null);
    }

    /**
     * Ouvre un FileChooser pour permettre � l'utilisateur de s�lectionner un carnet de lien � charger.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        //D�finir le filtre d'extension
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la bo�te de dialogue "Enregistrer le fichier"
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadLinkDataFromFile(file);
        }
    }

    /**
     * Enregistre le fichier dans le fichier link qui est actuellement ouvert. S'il n'y a pas de fichier ouvert, la bo�te de dialogue "Enregistrer sous" s'affiche.
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
     * Ouvre un FileChooser pour permettre � l'utilisateur de s�lectionner un fichier � enregistrer..
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        //D�finir le filtre d'extension
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la bo�te de dialogue "Enregistrer le fichier"
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Verifie qu'il poss�de l'extension correcte
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveLinkDataToFile(file);
        }
    }

    /**
     * Ouverture de la fen�tre about.
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
