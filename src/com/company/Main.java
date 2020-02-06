package com.company;


import java.util.List;
import java.util.Scanner;

/**
 * Created by Pramod Kondur
 * This is a JDBC app
 */
public class Main {

    public static void main(String[] args) {

        //Opening the datasource conn
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_DESC); // Gets all the artists name and sorts in Desc
        if(artists == null) {
            System.out.println("No artists!");
            return;
        }

        for(Artist artist : artists) {
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }

        List<String> albumsForArtist =
                datasource.queryAlbumsForArtist("Carole King", Datasource.ORDER_BY_ASC); // Finds the album name for the artist

        for(String album : albumsForArtist) {
            System.out.println(album);
        }

        List<SongArtist> songArtists = datasource.queryArtistsForSong("Go Your Own Way", Datasource.ORDER_BY_ASC); // finds the artist info for the song and orders by Asc
        if(songArtists == null) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist : songArtists) {
            System.out.println("Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track = " + artist.getTrack());
        }

        datasource.querySongsMetadata();

        int count = datasource.getCount(Datasource.TABLE_SONGS); // get the number of records in the songs table
        System.out.println("Number of songs is: " + count);

        datasource.createViewForSongArtists(); // check  in DB browser

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a song title: ");
        String title = scanner.nextLine();

        List<SongArtist> songArtistUser = datasource.querySongInfoView(title); // used to query the information from user entered song title from the view
        if(songArtistUser.isEmpty()) {
            System.out.println("Couldn't find the artist for the song");
            return;
        }

        for(SongArtist artist : songArtistUser)
        {
            System.out.println("FROM VIEW - Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track number = " + artist.getTrack());
        }
        scanner.close();

        datasource.insertSong("Bird Dog", "Everly Brothers", "All-Time Greatest Hits", 7); // inserts song
        int newcount = datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("Number of songs is: " + newcount); // shows the count after successfully adding a new song

        datasource.close(); // closes the datasource conn
    }
}