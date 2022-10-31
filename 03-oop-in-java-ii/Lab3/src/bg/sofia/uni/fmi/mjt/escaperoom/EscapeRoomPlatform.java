package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;

public class EscapeRoomPlatform implements EscapeRoomAdminAPI,EscapeRoomPortalAPI{


    @Override
    public void addEscapeRoom(EscapeRoom room) {

    }

    @Override
    public void removeEscapeRoom(String roomName) throws RoomNotFoundException {

    }

    @Override
    public EscapeRoom[] getAllEscapeRooms() {
        return new EscapeRoom[0];
    }

    @Override
    public void registerAchievement(String roomName, String teamName, int escapeTime) throws RoomNotFoundException, TeamNotFoundException {

    }

    @Override
    public EscapeRoom getEscapeRoomByName(String roomName) throws RoomNotFoundException {
        return null;
    }

    @Override
    public void reviewEscapeRoom(String roomName, Review review) throws RoomNotFoundException {

    }


    @Override
    public Review[] getReviews(String roomName) throws RoomNotFoundException {
        return new Review[0];
    }

    @Override
    public Team getTopTeamByRating() {
        return null;
    }
}
