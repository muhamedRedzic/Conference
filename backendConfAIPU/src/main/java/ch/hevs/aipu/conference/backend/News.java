package ch.hevs.aipu.conference.backend;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * Created by Matthieu on 27.11.2015.
 */
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String text;
    private Date published;

    //constructor
    public News(){};
    public News(String title, String text, Date published){
        this.title = title;
        this.text = text;
        this.published = published;
    }

    //getter and setter
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Date getDate() {
        return published;
    }
    public void setDate(Date published) {
        this.published= published;
    }
}
