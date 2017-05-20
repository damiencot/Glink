package fr.app.glink.view;




import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import fr.app.glink.MainApp;
import fr.app.glink.model.Link;


public class LinkVueEnsembleController {


	/*
	 * Les annontations @FXML est nécessaire au fichier fxml pour accéder aux champs et aux méthodes privées
	 */

	//  TableView est conçu pour visualiser un nombre illimité de lignes de données, divisées en colonnes.
	@FXML
	private TableView<Link> linkTable;

	//Responsable de l'affichage et de l'édition de données pour une seule colonne.
	@FXML
    private TableColumn<Link, String> titreColumn;


    @FXML
    private Label titreLabel;
    @FXML
    private Label urlLabel;



    // Reference au fichier principal de l'application
    public MainApp mainApp;


    /*
     * Constructeur
     */
	public LinkVueEnsembleController() {
	}


	/*
	 * Appelée automatiquement après le chargement du fichier fxml
	 */
	@FXML
	private void initialize(){
		//Nous déterminons  quel champ dans les objets Link devraient être utilisés pour une colonne particulière
		titreColumn.setCellValueFactory(cellData->cellData.getValue().titreProperty());

		showLinkDetails(null);


		//Chaque fois que l'utilisateur sélectionne un lien dans la table.
		//Nous prenons le lien nouvellement sélectionnée pour la transmettre à la méthode showPersonDetails(...).
		linkTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue)->showLinkDetails(newValue));

	}


	/*
	 * Appelé par l'application principal pour donner une reference a lui-même
	 *
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;

		// Ajoute des données de la table a la liste <observable list>
		linkTable.setItems(mainApp.getLinkData());
	}


	/**
	 * Fills all text fields to show details about the person.
	 * If the specified person is null, all text fields are cleared.
	 *
	 * @param person the person or null
	 */
	private void showLinkDetails(Link link) {
	    if (link != null) {
	        // Remplis les labels avec les informations recupere depuis l'objet link
	        titreLabel.setText(link.getTitre());
	        urlLabel.setText(link.getUrl());


	    } else {
	        // Si Link est NULL, on vide les champs text
	    	titreLabel.setText("");
	    	urlLabel.setText("");
	    }
	}


	/**
	 * Appelé lorsque l'utilisateur clique sur le bouton Supprimer.
	 */
	@FXML
	private void handleDeletePerson() {
	    int selectedIndex = linkTable.getSelectionModel().getSelectedIndex();
	    //Si un lien à été sélectionné
	    if (selectedIndex >= 0) {
	    	linkTable.getItems().remove(selectedIndex);
	    } else {
	        // Rien n'a été sélectionné
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("Aucune Selection");
	        alert.setHeaderText("Aucun lien n'a été sélectionner");
	        alert.setContentText("Veuillez sélectionner un lien.");

	        alert.showAndWait();
	    }
	}

	/**
	 * Appelé lorsque l'utilisateur clique sur le nouveau bouton. Ouvre une boîte de dialogue à éditer
	 * Détails pour un nouveau Lien.
	 */
	@FXML
	private void handleNewPerson() {
	    Link tempLink = new Link();
	    boolean okClicked = mainApp.showLinkEditDialog(tempLink);
	    if (okClicked) {
	        mainApp.getLinkData().add(tempLink);
	    }
	}

	/**
	 * Appelé lorsque l'utilisateur clique sur le bouton d'édition. Ouvre une boîte de dialogue à éditer
	 * Détails pour le Lien sélectionnée
	 */
	@FXML
	private void handleEditPerson() {
		Link selectedLink = linkTable.getSelectionModel().getSelectedItem();
	    if (selectedLink != null) {
	        boolean okClicked = mainApp.showLinkEditDialog(selectedLink);
	        if (okClicked) {
	            showLinkDetails(selectedLink);
	        }

	    } else {
	        //Rien n'a été sélectionné
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("Aucune selection");
	        alert.setHeaderText("Aucun lien n'a été sélectionner");
	        alert.setContentText("Veuillez sélectionner un lien.");

	        alert.showAndWait();
	    }
	}



}
