package tn.esprit.pidev.services;

import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import tn.esprit.pidev.entities.Planning;
import tn.esprit.pidev.utils.Database;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class    PlanningService {
    public ArrayList<Planning> planningArrayList;
   public  Planning planning = new Planning();
    public static PlanningService instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    public PlanningService() {
        req = new ConnectionRequest();
    }

    public static PlanningService getInstance() {
        if (instance == null) {
            instance = new PlanningService();
        }
        return instance;
    }


    public boolean addPlanning(Planning planning) {
        String url = Database.BASE_URL + "planning/api/add?titre="+planning.getTitreEvent()+
                "&type="+planning.getTypeEvent()+
                "&salle="+planning.getNomSalle()+
                "&date="+planning.getDate()+
                "&heureDebut="+planning.getHeureDebut()+
                "&heureFin="+planning.getHeureFin(); // Add Symfony URL here
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
    public boolean editPlanning(Planning planning) {
        String url = Database.BASE_URL + "planning/api/edit?id="+planning.getId()+
                "titre="+planning.getTitreEvent()+
                "&type="+planning.getTypeEvent()+
                "&salle="+planning.getNomSalle()+
                "&date="+planning.getDate()+
                "&heureDebut="+planning.getHeureDebut()+
                "&heureFin="+planning.getHeureFin(); // Add Symfony URL here
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public ArrayList<Planning> parsePlanning(String jsonText) {
        try {
            planningArrayList = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> planningListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) planningListJson.get("root");
            for (Map<String, Object> obj : list) {
                System.out.println(obj.get("heureDebut").toString().substring(11,16));
                Time heureDebut = new Time(new SimpleDateFormat("hh:mm:ss").parse(obj.get("heureDebut").toString().substring(11, 19)).getTime());
                Time heureFin = new Time(new SimpleDateFormat("hh:mm:ss").parse(obj.get("heureFin").toString().substring(11, 19)).getTime());
                System.out.println(heureFin+"");
                java.sql.Date date = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(obj.get("date").toString().substring(0,10)).getTime());
                Planning planning = new Planning((int) Float.parseFloat(obj.get("id").toString()),obj.get("typeEvent").toString() ,obj.get("titreEvent").toString(),obj.get("nomSalle").toString(), date, heureDebut, heureFin);
                planningArrayList.add(planning);
            }
        } catch (IOException | ParseException ex) {
        }
        return planningArrayList;
    }
    public boolean deletePlanning(int id){
        String url = Database.BASE_URL  + "planning/api/delete/"+id ; // Add Symfony URL here
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
    public ArrayList<Planning> showAll() {
        String url = Database.BASE_URL + "planning/api/show"; // Add Symfony URL Here
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                planningArrayList = parsePlanning(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return planningArrayList;
    }
    public ArrayList<Planning> showOrdered() {
        String url = Database.BASE_URL + "planning/api/showOrdered"; // Add Symfony URL Here
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                planningArrayList = parsePlanning(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return planningArrayList;
    }

    public ArrayList<Planning> getPlanningBySpectacleName(String titre) {

        String url = Database.BASE_URL + "planning/api/get?titre="+titre; // Add Symfony URL Here
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                planningArrayList = parsePlanning(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

        return planningArrayList;
    }
}
