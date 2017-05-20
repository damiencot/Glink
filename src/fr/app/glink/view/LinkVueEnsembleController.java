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
	 * Les annontations @FXML est n�cessaire au fichier fxml pour acc�der aux champs et aux m�thodes priv�es
	 */

	//  TableView est con�u pour visualiser un nombre illimit� de lignes de donn�es, divis�es en colonnes.
	@FXML
	private TableView<Link> linkTable;

	//Responsable de l'affichage et de l'�dition de donn�es pour une seule colonne.
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
	 * Appel�e automatiquement apr�s le chargement du fichier fxml
	 */
	@FXML
	private void initialize(){
		//Nous d�terminons  quel champ dans les objets Link devraient �tre utilis�s pour une colonne particuli�re
		titreColumn.setCellValueFactory(cellData->cellData.getValue().titreProperty());

		showLinkDetails(null);


		//Chaque fois que l'utilisateur s�lectionne un lien dans la table.
		//Nous prenons le lien nouvellement s�lectionn�e pour la transmettre � la m�thode showPersonDetails(...).
		linkTable.getSelectionModel().selectedItemProperty().addListener(
	            (observable, oldValue, newValue)->showLinkDetails(newValue));

	}


	/*
	 * Appel� par l'application principal pour donner une reference a lui-m�me
	 *
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;

		// Ajoute des donn�es de la table a la liste <observable list>
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
	 * Appel� lorsque l'utilisateur clique sur le bouton Supprimer.
	 */
	@FXML
	private void handleDeletePerson() {
	    int selectedIndex = linkTable.getSelectionModel().getSelectedIndex();
	    //Si un lien � �t� s�lectionn�
	    if (selectedIndex >= 0) {
	    	linkTable.getItems().remove(selectedIndex);
	    } else {
	        // Rien n'a �t� s�lectionn�
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("Aucune Selection");
	        alert.setHeaderText("Aucun lien n'a �t� s�lectionner");
	        alert.setContentText("Veuillez s�lectionner un lien.");

	        alert.showAndWait();
	    }
	}

	/**
	 * Appel� lorsque l'utilisateur clique sur le nouveau bouton. Ouvre une bo�te de dialogue � �diter
	 * D�tails pour un nouveau Lien.
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
	 * Appel� lorsque l'utilisateur clique sur le bouton d'�dition. Ouvre une bo�te de dialogue � �diter
	 * D�tails pour le Lien s�lectionn�e
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
	        //Rien n'a �t� s�lectionn�
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("Aucune selection");
	        alert.setHeaderText("Aucun lien n'a �t� s�lectionner");
	        alert.setContentText("Veuillez s�lectionner un lien.");

	        alert.showAndWait();
	    }
	}



}
