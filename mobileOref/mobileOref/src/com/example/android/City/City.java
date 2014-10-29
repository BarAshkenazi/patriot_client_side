package com.example.android.City;


public class City 
{
	private int id;
    private String name;
    private String isChosen;
   
    public City()
    {
    	
    }
    
	public City(String name, String isChosen) {
		this.name = name;
  		this.isChosen = isChosen;
	}

	public long getId()
    {
    	return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

	public String getIsChosen() {
		return isChosen;
	}

	public void setIsChosen(String isChosen) {
		this.isChosen = isChosen;	
	}
}
