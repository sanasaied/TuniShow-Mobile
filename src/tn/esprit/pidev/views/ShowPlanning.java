package tn.esprit.pidev.views;

import com.codename1.messaging.Message;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import tn.esprit.pidev.entities.Planning;

public class ShowPlanning extends Form {
    Form current;

    public ShowPlanning(Form previous, Planning planning) {
        /* *** *CONFIG SCREEN* *** */
        current = this;
        setTitle("Planning Details");
        setLayout(BoxLayout.y());
        /* *** *YOUR CODE GOES HERE* *** */
        Label titreLabel = new Label("Titre: " + planning.getTitreEvent());
        Label typeLabel = new Label("Type: " + planning.getTypeEvent());
        Label salleLabel = new Label("Salle: " + planning.getNomSalle());
        Button buyButton = new Button("Buy Ticket");
        buyButton.addActionListener(evt -> {
        });

        addAll(titreLabel, typeLabel, salleLabel, buyButton); //ADD ALL COMPONENTS TO THE VIEW
        /* *** *BACK BUTTON* *** */
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        /* *** *OVERFLOW MENU* *** */
        getToolbar().addCommandToOverflowMenu("Share", null, (evt) -> {
            Display.getInstance().sendMessage(new String[]{""}, "Join Me !", new Message("Check out this planning: " + planning.getTitreEvent()));
        });
    }
}

