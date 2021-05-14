package tn.esprit.pidev.views;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import tn.esprit.pidev.entities.Spectacle;
import tn.esprit.pidev.services.SpectacleService;

import java.util.ArrayList;
import java.util.Collections;

public class SpectacleForm extends Form {
    Form current;
    SpectacleService spectacleService = new SpectacleService();
    ArrayList<Spectacle> spectacleArrayList = new ArrayList<>();
    public SpectacleForm(Form previous) {
        /* *** *CONFIG SCREEN* *** */
        current = this;
        setTitle("Spectacle List");
        setLayout(BoxLayout.y());
        /* *** *YOUR CODE GOES HERE* *** */
        spectacleArrayList = spectacleService.showAll();
        Collections.reverse(spectacleArrayList);
        showSpectacle();
        /* *** *SEARCHBAR* *** */
        getToolbar().addSearchCommand(e -> {
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                // clear search
                for (Component cmp : getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
                getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                for (Component cmp : getContentPane()) {
                    MultiButton mb = (MultiButton) cmp;
                    String line1 = mb.getTextLine1();
                    String line2 = mb.getTextLine2();
                    boolean show = line1 != null && line1.toLowerCase().indexOf(text) > -1 ||
                            line2 != null && line2.toLowerCase().indexOf(text) > -1;
                    mb.setHidden(!show);
                    mb.setVisible(show);

                }
                getContentPane().animateLayout(150);
            }
        }, 4);
        /* *** *OVERFLOW MENU* *** */
        getToolbar().addCommandToOverflowMenu("Sort by Name", null, (evt)->{
            removeAll();
            Collections.sort(spectacleArrayList, Spectacle.titleComparator);
            showSpectacle();
        });
        /* *** *SIDE MENU* *** */
        getToolbar().addCommandToLeftSideMenu("", null, (evt) -> {        });
        getToolbar().addCommandToLeftSideMenu("Shows", null, (evt) -> {            new SpectacleForm(current).show();        });
        getToolbar().addCommandToLeftSideMenu("Planning", null, (evt) -> {            new PlanningsForm(current).show();        });

    }
    public void showSpectacle(){
        for (Spectacle spectacle : spectacleArrayList) {
            int deviceWidth = Display.getInstance().getDisplayWidth();
            Image placeholder = Image.createImage(deviceWidth / 3, deviceWidth / 4, 0xbfc9d2);
            EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
            Image image = URLImage.createToStorage(encImage, spectacle.getTitre() + spectacle.getId(), spectacle.getImage(), URLImage.RESIZE_SCALE);
            MultiButton multiButton = new MultiButton();
            multiButton.setTextLine1(spectacle.getTitre() + "");
            multiButton.setTextLine2(spectacle.getGenre() + "");
            multiButton.setTextLine3(spectacle.getDate()+"");
            multiButton.setIcon(image);
            multiButton.setUIID(spectacle.getId() + "");
            multiButton.addActionListener(l -> new ShowSpectacle(current, spectacle).show());
            add(multiButton);
        }
    }
}
