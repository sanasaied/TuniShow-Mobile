package tn.esprit.pidev.services;

import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import tn.esprit.pidev.utils.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalleService {
    public ArrayList<String> salleNameArrayList;

    public static SalleService instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    public SalleService() {
        req = new ConnectionRequest();
    }

    public static SalleService getInstance() {
        if (instance == null) {
            instance = new SalleService();
        }
        return instance;
    }

    public ArrayList<String> parseSalle(String jsonText) {
        try {
            salleNameArrayList = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> salleNameListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) salleNameListJson.get("root");
            for (Map<String, Object> obj : list) {
                salleNameArrayList.add(obj.get("nom").toString());
            }
        } catch (IOException ex) {
        }
        return salleNameArrayList;
    }

    public ArrayList<String> showAll() {
        String url = Database.BASE_URL + "salle/api/get"; // Add Symfony URL Here
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                salleNameArrayList = parseSalle(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return salleNameArrayList;
    }

}
