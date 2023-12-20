package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Event implements Serializable {
    
    private static final long serialVersionUID = -521880598499143182L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventNumber;
    private String description;
    private Date eventDate;
    @OneToMany(targetEntity = Question.class, mappedBy = "event",
            fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Question> questions;

    public Event(Integer eventNumber, String description, Date eventDate) {
        this(description, eventDate);
        this.eventNumber = eventNumber;
    }

    public Event(String description, Date eventDate) {
        this.description = description;
        this.eventDate = eventDate;
        this.questions = new ArrayList<>();
    }
    
    public Event() {
        // Empty constructor to instantiate object
    }

    /**
     * This method creates a bet with a question, minimum bet ammount and percentual profit
     * 
     * @param question to be added to the event
     * @param betMinimum of that question
     * @return Bet
     */
    public Question addQuestion(String question, float betMinimum) {
        Question q = new Question(question, betMinimum, this);
        this.questions.add(q);
        return q;
    }

    /**
     * This method checks if the question already exists for that event
     * 
     * @param question that needs to be checked if there exists
     * @return true if the question exists and false in other case
     */
    public boolean doesQuestionExists(String question)  {   
        for (Question q : this.getQuestions()) {
            if (q.getQuestion().compareTo(question) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        return this.eventNumber.intValue() != other.eventNumber.intValue();
    }

    public String getDescription() {
        return this.description;
    }

    public Date getEventDate() {
        return this.eventDate;
    }

    public Integer getEventNumber() {
        return this.eventNumber;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.eventNumber.intValue();
        return result;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventNumber(Integer eventNumber) {
        this.eventNumber = eventNumber;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return String.format("%s;%s", this.eventNumber, this.description);
    }
}
