package fr.app.glink.model;


import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Link {

    private final StringProperty titre;
    private final StringProperty url;

    /**
     * Constructeur par défaut.
     */
    public Link() {
        this(null, null);
    }

    /**
     * Constructeur avec paramétres.
     *
     * @param titre
     * @param url
     */
    public Link(String titre, String url) {
        this.titre = new SimpleStringProperty(titre);
        this.url = new SimpleStringProperty(url);
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public StringProperty titreProperty() {
        return titre;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public StringProperty urlProperty() {
        return url;
    }




}
