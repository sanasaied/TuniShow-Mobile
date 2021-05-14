package tn.esprit.pidev.views;

import com.codename1.components.MultiButton;
import com.codename1.ui.*;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import tn.esprit.pidev.entities.Planning;
import tn.esprit.pidev.services.PlanningService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeScreen extends Form {
    Form current;
    PlanningService planningService = new PlanningService();

    public HomeScreen() {
        /* *** *CONFIG SCREEN* *** */
        current = this;
        setTitle("WELCOME");
        setLayout(BoxLayout.y());
        setScrollableY(false);
       // getStyle().setBgColor(0x99CCCC);
        /* *** *YOUR CODE GOES HERE* *** */
        Calendar calendar = new Calendar();
        Container eventContainer = BoxLayout.encloseY();
        eventContainer.setScrollableY(true);
        setHighlightedDate(calendar);
        calendar.addActionListener(evt -> {
            try {
                System.out.println(calendar.getDate() + "");
ArrayList<Planning> planningArray = new ArrayList<>();
                Date selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US).parse(calendar.getDate() + "")));
                System.out.println(selectedDate + "j");
                for (Planning planning : planningService.showAll()) {
                    if (planning.getDate() != null && planning.getDate().compareTo(selectedDate)==0) {
                        System.out.println("equals");
                        planningArray.add(planning);
                    }
                }
                eventContainer.removeAll();
                if(planningArray.size()==0){
                    eventContainer.add(new Label("There is no Planning in this day"));
                }else {
                    for(Planning planning : planningArray){
                        showPlanning(planning, eventContainer);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        addAll(new Label("Choose an option from side menu"), calendar, eventContainer);

        /* *** *SIDE MENU* *** */
        getToolbar().addCommandToLeftSideMenu("", null, (evt) -> {
        });
        getToolbar().addCommandToLeftSideMenu("Shows", null, (evt) -> {
            new SpectacleForm(current).show();
        });
        getToolbar().addCommandToLeftSideMenu("Planning", null, (evt) -> {
            new PlanningsForm(current).show();
        });

    }

    public void setHighlightedDate(Calendar calendar) {
        for (Planning planning : planningService.showAll()) {
            calendar.highlightDate(planning.getDate(), planning.getId() + "");
            calendar.setUIID(planning.getId() + "");
            //calendar.addDayActionListener(evt -> System.out.println("hello world: "+calendar.getSelectedDaysUIID()));
        }
    }
    public void showPlanning(Planning planning, Container container) {
        MultiButton multiButton = new MultiButton();
        multiButton.setTextLine1(planning.getTitreEvent());
        multiButton.setTextLine2(planning.getTypeEvent());
        multiButton.setHorizontalLayout(true);
        multiButton.setTextLine3(planning.getNomSalle());
        multiButton.setTextLine4(planning.getDate() + "           " + planning.getHeureDebut() + "           " + planning.getHeureFin());
        multiButton.setUIID(planning.getId() + "");
        multiButton.setEmblem(FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_RIGHT, "", 10.0f));
        multiButton.addActionListener(l -> new ShowPlanning(current, planning).show());
        // container.add(multiButton);
        container.add(multiButton);
    }
}
