package team8.kaiji.model;

public class User{
  int id;
  String name;
  int gu;
  int cho;
  int pa;
  int star;

  public int getId(){
    return id;
  }

  public void setId(int id){
    this.id=id;
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name=name;
  }

  public int getGu(){
    return gu;
  }

  public void setGu(int gu){
    this.gu=gu;
  }

  public int getCho(){
    return cho;
  }

  public void setCho(int cho){
    this.cho=cho;
  }

  public int getPa(){
    return pa;
  }

  public void setPa(int pa){
    this.pa=pa;
  }

  public int getStar(){
    return star;
  }

  public void setStar(int star){
    this.star=star;
  }

}
