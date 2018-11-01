package model;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DestinationTicketDeck {
    private String [][] destinationTicketInfo = {{"Los Angeles", "New York", "21"}, {"Duluth", "Houston", "8"}, {"Sault St. Marie", "Nashville", "8"}, {"New York", "Atlanta", "6"}, {"Portland", "Nashville", "17"}, {"Vancouver", "Montreal", "20"}, {"Duluth", "El Paso", "10"}, {"Toronto", "Miami", "10"}, {"Portland", "Phoenix", "11"}, {"Dallas", "New York", "11"}, {"Calgary", "Salt Lake City", "7"}, {"Calgary", "Phoenix", "13"}, {"Los Angeles", "Miami", "20"}, {"Winnipeg", "Little Rock", "11"}, {"San Francisco", "Atlanta", "17"}, {"Kansas City", "Houston", "5"}, {"Los Angeles", "Chicago", "16"}, {"Denver", "Pittsburgh", "11"}, {"Chicago", "Santa Fe", "9"}, {"Vancouver", "Santa Fe", "13"}, {"Boston", "Miami", "12"}, {"Chicago", "New Orleans", "7"}, {"Montreal", "Atlanta", "9"}, {"Seattle", "New York", "22"}, {"Denver", "El Paso", "4"}, {"Helena", "Los Angeles", "8"}, {"Winnipeg", "Houston", "12"}, {"Montreal", "New Orleans", "13"}, {"Sault St. Marie", "Oklahoma City", "9"}, {"Seattle", "Los Angeles", "9"}};
    private List<DestinationTicketCard> destinationCards;
    private List<Location> cities;

    public DestinationTicketDeck() {
        initializeCities();
        initializeDestinationTicketDeck();
    }

    private void initializeCities()
    {
        try
        {
            String json = new String(Files.readAllBytes(Paths.get("cityLocations.json"))); //use new AuthToken(Files.readAllBytes(Paths.get("locations.json"))); as the call parameter to this function...do the same for the rest
            Gson gson = new Gson();
            ActiveGame.Data1 locArray = gson.fromJson(json, ActiveGame.Data1.class);
            cities = Arrays.asList(locArray.data);
        }
        catch (IOException e)
        {
            System.out.println("IOException in ActiveGame -> initializeCities method: ");
            e.printStackTrace();
            System.out.println("Generating Test Data Instead");
            cities = new ArrayList<Location>();
            cities.add(new Location(45.5017, 73.0589, "Boston", "United States"));
            cities.add(new Location(38.9072, 77.0369, "Washington", "United States"));
            cities.add(new Location(35.7796, 78.6382, "Raleigh", "United States"));
            cities.add(new Location(32.7765, 79.9311, "Charleston", "United States"));
            cities.add(new Location(25.7617, 80.1918, "Miami", "United States"));
            cities.add(new Location(40.4406, 79.9959, "Pittsburgh", "United States"));
            cities.add(new Location(33.7490, 84.3880, "Atlanta", "United States"));
        }
    }

    public Location getLocationFromName(String cityName)
    {
        for(Location l : cities)
        {
            if (l.getCity().equals(cityName))
                return l;
        }
        return null;
    }

    private void initializeDestinationTicketDeck()
    {
        destinationCards = new ArrayList<>();
        for(int i = 0; i < destinationTicketInfo.length; i++)
        {
            destinationCards.add(new DestinationTicketCard(getLocationFromName(destinationTicketInfo[i][0]), getLocationFromName(destinationTicketInfo[i][1]), Integer.valueOf(destinationTicketInfo[i][2])));
        }
        shuffle();
    }

    public List<DestinationTicketCard> getDestinationCards() {
        List<DestinationTicketCard> drawnCards;
        if (destinationCards.size() >= 3) {
            drawnCards = new ArrayList<DestinationTicketCard>();
            for(int i = 0; i < 3; i++)
            {
                drawnCards.add(destinationCards.get(i));
            }
            for(int i = 0; i < 3; i++)
            {
                destinationCards.remove(0);
            }
        } else {
            drawnCards = destinationCards;
            destinationCards = new ArrayList<DestinationTicketCard>();
        }
        return drawnCards;
    }

    private void shuffle()
    {
        for(int i = 0; i < 10; i++)
            Collections.shuffle(destinationCards);
    }
}
