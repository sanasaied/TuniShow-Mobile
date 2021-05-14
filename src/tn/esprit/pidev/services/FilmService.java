package tn.esprit.pidev.services;

import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import tn.esprit.pidev.utils.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilmService {
    public ArrayList<String> filmArrayList;

    public static FilmService instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    public FilmService() {
        req = new ConnectionRequest();
    }

    public static FilmService getInstance() {
        if (instance == null) {
            instance = new FilmService();
        }
        return instance;
    }

    public ArrayList<String> parseFilm(String jsonText) {
        try {
            filmArrayList = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> filmListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) filmListJson.get("root");
            for (Map<String, Object> obj : list) {
                filmArrayList.add(obj.get("titre").toString());
            }
        } catch (IOException ex) {
        }
        return filmArrayList;
    }

    public ArrayList<String> showAll() {
        String url = Database.BASE_URL + "film/api/get"; // Add Symfony URL Here
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                filmArrayList = parseFilm(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return filmArrayList;
    }

}

