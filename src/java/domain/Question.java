package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Question implements Serializable {
    
    private static final long serialVersionUID = -6030722105781342208L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionNumber;
    private String questionName;
    private float betMinimum;
    private String result;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.EAGER)
    private Event event;
    
    public Question(Integer queryNumber, String query, float betMinimum, Event event) {
        this(query, betMinimum, event);
        this.questionNumber = queryNumber;
    }
    
    public Question(String query, float betMinimum, Event event) {
        this.questionName = query;
        this.betMinimum = betMinimum;
        this.event = event;
    }
    
    public Question() {
        // Empty constructor to instantiate object
    }

    /**
     * Get the minimun ammount of the bet
     * @return the minimum bet ammount
     */
    public float getBetMinimum() {
        return this.betMinimum;
    }

    /**
     * Get the event associated to the bet 
     * @return the associated event
     */
    public Event getEvent() {
        return this.event;
    }

    /**
     * Get the question description of the bet
     * @return the bet question
     */
    public String getQuestion() {
        return this.questionName;
    }

    /**
     * Get the  number of the question
     * @return the question number
     */
    public Integer getQuestionNumber() {
        return this.questionNumber;
    }

    /**
     * Get the result of the  query
     * @return the the query result
     */
    public String getResult() {
        return this.result;
    }

    /**
     * Get the minimun ammount of the bet
     * @param  betMinimum minimum bet ammount to be setted
     */
    public void setBetMinimum(float betMinimum) {
        this.betMinimum = betMinimum;
    }

    /**
     * Set the event associated to the bet
     * @param event to associate to the bet
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Set the question description of the bet 
     * @param question to be setted
     */ 
    public void setQuestion(String question) {
        this.questionName = question;
    }

    /**
     * Set the bet number to a question
     * @param questionNumber to be setted
     */
    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    /**
     * Get the result of the  query
     * @param result of the query to be setted
     */
    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s", this.questionNumber, this.questionName, Float.toString(this.betMinimum));
    }
}
