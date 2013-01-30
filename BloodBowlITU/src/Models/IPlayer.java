package models;

import java.util.ArrayList;

public interface IPlayer {

public String getTitle();
public int getCost();
public int getMA();
public int getST();
public int getAG();
public int getAV();
public ArrayList<Skill> getSkills();

}
