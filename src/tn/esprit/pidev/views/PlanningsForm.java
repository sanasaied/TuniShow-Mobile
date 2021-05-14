package tn.esprit.pidev.views;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import tn.esprit.pidev.entities.Planning;
import tn.esprit.pidev.services.PlanningService;

import java.util.ArrayList;
import java.util.Collections;

public class PlanningsForm extends Form {
    Form current;
    PlanningService planningService = new PlanningService();
    ArrayList<Planning> planningArrayList = new ArrayList<>();
    public PlanningsForm(Form previous) {
        /* *** *CONFIG SCREEN* *** */
        current = this;
        setTitle("Spectacle List");
        setLayout(BoxLayout.y());
        /* *** *YOUR CODE GOES HERE* *** */
        planningArrayList = planningService.showAll();
        showPlanning();
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
            Collections.sort(planningArrayList, Planning.titleEventComparator);
            showPlanning();
        });
        /* *** *SIDE MENU* *** */
        getToolbar().addCommandToLeftSideMenu("", null, (evt) -> {        });
        getToolbar().addCommandToLeftSideMenu("Shows", null, (evt) -> {            new SpectacleForm(current).show();        });
        getToolbar().addCommandToLeftSideMenu("Planning", null, (evt) -> {            new PlanningsForm(current).show();        });
        getToolbar().addCommandToOverflowMenu("Stats", FontImage.createMaterial(FontImage.MATERIAL_PIE_CHART, UIManager.getInstance().getComponentStyle("TitleCommand")), (evt) -> {
            new StatsPlanningsForm(current).show();
        });

    }
    public void showPlanning(){
        for (Planning planning : planningArrayList) {
            MultiButton multiButton = new MultiButton();
            multiButton.setTextLine1(planning.getTitreEvent());
            multiButton.setTextLine2(planning.getTypeEvent());
            multiButton.setHorizontalLayout(true);
            multiButton.setTextLine3(planning.getNomSalle());
            multiButton.setTextLine4(planning.getDate()+"           "+planning.getHeureDebut() + "           "+planning.getHeureFin());
            multiButton.setUIID(planning.getId() + "");
            multiButton.setEmblem(FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_RIGHT, "", 10.0f));
            multiButton.addActionListener(l -> new ShowPlanning(current, planning).show());
            // container.add(multiButton);
            add(multiButton);
        }
    }
}
