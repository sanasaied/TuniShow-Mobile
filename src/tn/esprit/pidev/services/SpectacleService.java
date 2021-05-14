package tn.esprit.pidev.services;

import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import tn.esprit.pidev.entities.Spectacle;
import tn.esprit.pidev.utils.Database;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpectacleService {
    public ArrayList<Spectacle> spectacleArrayList;

    public static SpectacleService instance=null;
    public boolean resultOK;
    private ConnectionRequest req;

    public SpectacleService() {
        req = new ConnectionRequest();
    }

    public static SpectacleService getInstance() {
        if (instance == null) {
            instance = new SpectacleService();
        }
        return instance;
    }


    public boolean addSpectacle(Spectacle spectacle){
        String url = Database.BASE_URL  + "spectacle/api/add?titre="+spectacle.getTitre()+
                "&genre="+spectacle.getGenre()+
                "&img="+spectacle.getImage()+
                "&date="+spectacle.getDate(); // Add Symfony URL here
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
    public boolean deleteSpectacle(int id){
        String url = Database.BASE_URL  + "spectacle/api/delete/"+id ; // Add Symfony URL here
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
    public ArrayList<Spectacle> parseSpectacle(String jsonText){ //Parsing Issues with id and date type
        try {
            spectacleArrayList = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> spectacleListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String,Object>> list = (List<Map<String,Object>>)spectacleListJson.get("root");
            for(Map<String,Object> obj : list){
                Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(obj.get("date").toString().substring(0,10)).getTime());
                Spectacle spectacle = new Spectacle( (int) Float.parseFloat(obj.get("id").toString()), obj.get("titre").toString(), date ,obj.get("genre").toString(), obj.get("imagePath").toString());
                spectacleArrayList.add(spectacle);
            }
        } catch (IOException | ParseException ex) {
        }
        return spectacleArrayList;
    }
    public ArrayList<Spectacle> showAll(){
        String url = Database.BASE_URL+"spectacle/api/show"; // Add Symfony URL Here
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                spectacleArrayList = parseSpectacle(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return spectacleArrayList;
    }

    public boolean editSpectacle(Spectacle spectacle) {
            String url = Database.BASE_URL  + "spectacle/api/edit?id="+spectacle.getId()+
                    "titre="+spectacle.getTitre()+
                    "&genre="+spectacle.getGenre()+
                    "&img="+spectacle.getImage()+
                    "&date="+spectacle.getDate(); // Add Symfony URL here
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

    public ArrayList<Spectacle> showOrdered() {
        String url = Database.BASE_URL+"spectacle/api/showOrdered"; // Add Symfony URL Here
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                spectacleArrayList = parseSpectacle(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return spectacleArrayList;
    }
}
