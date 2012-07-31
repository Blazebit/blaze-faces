package com.blazebit.portlet;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "gambitController")
@SessionScoped
public class GambitController implements Serializable {

    private int amount = 10000;
    private int bet;

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getAmount() {
        return amount;
    }

    public void playRed() {
        play(0);
    }

    public void playBlack() {
        play(1);
    }

    private void play(int winCondition) {
        int result = (int) (Math.random() * 2);
        FacesMessage msg = new FacesMessage();

        if (result == winCondition) {
            amount = amount + bet;
            msg.setSummary("YOU WIN!!!");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
        } else {
            amount = amount - bet;
            msg.setSummary("YOU LOST!!!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void reset() {
        this.amount = 10000;
        this.bet = 0;
    }
}
