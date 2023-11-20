package commands;

import commands.player.*;
import commands.playlist.CreatePlaylist;
import commands.playlist.ShowPlaylists;
import commands.search.SearchBar;
import commands.search.Select;
import fileio.input.LibraryInput;
import main.Output;
import java.util.ArrayList;

public class CommandExecute {

    private String command;
    private String username;
    private int timestamp;
    private static ArrayList<UserHistory> userHistory = new ArrayList<>();
    protected LibraryInput library;
    private static ArrayList<Playlist> allUsersPlaylists = new ArrayList<>(); // fac un arraylist cu playlisturule publice ale userilor

    public static void clear() {
        userHistory = new ArrayList<>();
        allUsersPlaylists = new ArrayList<>();
    }
    public CommandExecute(Command command, LibraryInput library) {
        this.command = command.getCommand();
        this.username = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.library = library;
    }

    public void execute() {
    }

    public Output generateOutput() {
        Output output = new Output(this.getCommand(), this.getUsername(), this.getTimestamp());
        return output;
    }

    public static ArrayList<Playlist> getAllUsersPlaylists() {
        return allUsersPlaylists;
    }

    public String getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public ArrayList<UserHistory> getUserHistory() {
        return userHistory;
    }


public int verifyUser(String user) {
    for (int i = 0; i < userHistory.size(); i++) {
        UserHistory iter = userHistory.get(i);
        if (user != null && iter.getUser().equals(user)) {
            return i; // Returnez indexul userului daca exista
        }
    }
    return -1; // Returnez -1 daca nu exista
}

    public Output executeCommand(Command command) {
       if (verifyUser(command.getUsername()) == -1)
           this.userHistory.add(new UserHistory(command.getUsername()));
        switch (this.command) {
            case "search":
                SearchBar search = new SearchBar(command, this.library, command.getType(), command.getFilters());
                search.execute();
                userHistory.get(verifyUser(command.getUsername())).setResultSearch(search.getResults());
                return search.generateOutput();
            case "select":
                Select selectItem = new Select(command, this.library, command.getItemNumber());
                return selectItem.generateOutput();
            case "load":
                Load load = new Load(command, library);
                return load.generateOutput();
            case "playPause":
                PlayPause playPause = new PlayPause(command, library);
                return playPause.generateOutput();
            case "status":
                Stats status = new Stats(command, library);
                status.execute();
                Output output = new Output(getCommand(), getUsername(), getTimestamp());
                output.outputStatus(status);
                return output;
            case "addRemoveInPlaylist":
                AddRemoveInPlaylist addRemoveInPlaylist = new AddRemoveInPlaylist(command, library, command.getPlaylistId());
                return addRemoveInPlaylist.generateOutput();
            case "createPlaylist":
                CreatePlaylist createPlaylist = new CreatePlaylist(command, library, command.getPlaylistName());
                return createPlaylist.generateOutput();
            case "like":
                Like likeSong = new Like(command, library);
                return likeSong.generateOutput();
            case "showPlaylists":
                ShowPlaylists showPlaylists = new ShowPlaylists(command, library);
                return showPlaylists.generateOutput();
            case "showPreferredSongs":
                ShowPreferredSongs showPreferredSongs = new ShowPreferredSongs(command, library);
                return showPreferredSongs.generateOutput();
        }
        return null;
    }
}
