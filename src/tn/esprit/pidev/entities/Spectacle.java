package tn.esprit.pidev.entities;

import java.sql.Date;
import java.util.Comparator;

public class Spectacle {
    private int id;
    private String titre;
    private Date date;
    private String genre;
    private String image;

    public Spectacle(int id, String titre, String genre, String image) {
        this.id = id;
        this.titre = titre;
        this.genre = genre;
        this.image = image;
    }

    public Spectacle() {
    }

    public Spectacle(int id, String titre, Date date, String genre, String image) {
        this.id = id;
        this.titre = titre;
        this.date = date;
        this.genre = genre;
        this.image = image;
    }

    public Spectacle(String titre, Date date, String genre, String image) {
        this.titre = titre;
        this.date = date;
        this.genre = genre;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Comparator<Spectacle> titleComparator = new Comparator<Spectacle>() {
        @Override
        public int compare(Spectacle o1, Spectacle o2) {
            return (int) (o1.getTitre().toLowerCase().compareTo(o2.getTitre().toLowerCase()));
        }
    };
}
