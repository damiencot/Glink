package fr.app.glink.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/*
 * Contient notre liste de Link/Url en vue de l'enregistrer en XML.
 */

@XmlRootElement(name = "links")
public class LinkListWrapper {

    private List<Link> links;

    //nom optionel que nous pouvons spécifier pour l'élément
    @XmlElement(name = "link")
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
