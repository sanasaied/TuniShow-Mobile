package tn.esprit.pidev.views;

import com.codename1.components.ImageViewer;
import com.codename1.messaging.Message;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import tn.esprit.pidev.entities.Planning;
import tn.esprit.pidev.entities.Spectacle;
import tn.esprit.pidev.services.PlanningService;
import tn.esprit.pidev.views.*;

public class ShowSpectacle extends Form {
    Form current;
    PlanningService planningService = new PlanningService();

    public ShowSpectacle(Form previous, Spectacle spectacle) {
        /* *** *CONFIG SCREEN* *** */
        current = this;
        setTitle("Spectacle Details");
        setLayout(BoxLayout.y());
        /* *** *YOUR CODE GOES HERE* *** */
        Label titreLabel = new Label("Titre: " + spectacle.getTitre());
        Label genreLabel = new Label("Genre: " + spectacle.getGenre());
        Label dateLabel = new Label("Date: " + spectacle.getDate());
        Button showButton = new Button("Show in planning");
        showButton.addActionListener(evt -> {
            if (planningService.getPlanningBySpectacleName(spectacle.getTitre()).size() != 0) { // IF PLANNING IS FOUND
                System.out.println("exists");
                Planning planning = planningService.getPlanningBySpectacleName(spectacle.getTitre()).get(0); // GET THE PLANNING FOR THE SELECTED TITLE EVENT
              //  new ShowPlanning(current, planning).show(); //  NAVIGATE TO THE PLANNING DETAIL SCREEN
           new ShowPlanning(current, planning).show();
            } else { // IF PLANNING DOES NOT EXISTS
                System.out.println("noooo");
                Dialog.show("NOT FOUND", "There is no planning for this Spectacle", "OK", null);
            }
        });
        /* *THIS CODE USED TO DISPLAY IMAGE* */
        int deviceWidth = Display.getInstance().getDisplayWidth();
        Image placeholder = Image.createImage(deviceWidth, deviceWidth, 0xbfc9d2);
        EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
        Image image = URLImage.createToStorage(encImage, spectacle.getTitre() + spectacle.getId(), spectacle.getImage(), URLImage.RESIZE_SCALE_TO_FILL);
        ImageViewer imageViewer = new ImageViewer();
        imageViewer.setImage(image);

        addAll(imageViewer, titreLabel, genreLabel, dateLabel, showButton);        //ADD ALL COMPONENTS TO THE VIEW
        /* *** *BACK BUTTON* *** */
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        /* *** *OVERFLOW MENU* *** */
        getToolbar().addCommandToOverflowMenu("Share", null, (evt) -> {
            //SENDING EMAIL
            Display.getInstance().sendMessage(new String[]{""}, "Let's watch this!", new Message("Check out this show: " + spectacle.getTitre() + " it's: " + spectacle.getDate()));
        });
    }
}
