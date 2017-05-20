package fr.app.glink.view;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import fr.app.glink.model.Link;

public class LinkEditDialogController {


	@FXML
    private TextField titreField;
    @FXML
    private TextField urlField;



    private Stage dialogStage;
    private Link link;
    private boolean okClicked = false;



    /**
     * Initialise la classe de contr�leur. Cette m�thode est automatiquement appel�e
     * Apr�s que le fichier fxml a �t� charg�.
     *
     */
    @FXML
    private void initialize() {
    }


    /**
     * D�finit la fen�tre de ce dialogue
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }



    /**
     * D�finit le lien � modifier dans la bo�te de dialogue.
     *
     * @param link
     */
    public void setLink(Link link) {
        this.link = link;

        titreField.setText(link.getTitre());
        urlField.setText(link.getUrl());

    }



    /**
     * Renvoie true si l'utilisateur a cliqu� sur OK, false sinon.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }


    /**
     * handleOk est appel� lorsque l'utilisateur clique sur OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            link.setTitre(titreField.getText());
            link.setUrl(urlField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }


    /**
     * handleCancel est appel� lorsque l'utilisateur clique sur annuler.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Valide l'entr�e de l'utilisateur dans les champs de texte.
     *
     * @return true Si l'entr�e est valide
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (titreField.getText() == null || titreField.getText().length() == 0) {
            errorMessage += "Titre non valide!\n";
        }
        if (urlField.getText() == null || urlField.getText().length() == 0) {
            errorMessage += "Url non valide !\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Affiche le message d'erreur.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez remplir correctement les champs");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

}
