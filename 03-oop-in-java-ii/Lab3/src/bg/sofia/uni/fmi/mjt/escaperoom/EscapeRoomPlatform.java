package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.PlatformCapacityExceededException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;

public class EscapeRoomPlatform implements EscapeRoomAdminAPI, EscapeRoomPortalAPI {

    private final int maxCapacity;
    private final Team[] teams;
    private EscapeRoom[] escapeRooms;

    private int currentIDX;

    public EscapeRoomPlatform(Team[] teams, int maxCapacity) {
        this.teams = teams;
        this.maxCapacity = maxCapacity;
        this.escapeRooms = new EscapeRoom[0];
    }

    @Override
    public void addEscapeRoom(EscapeRoom room) throws RoomAlreadyExistsException {
        if (room == null) {
            throw new IllegalArgumentException("Room is null");
        }
        if (currentIDX == maxCapacity) {
            throw new PlatformCapacityExceededException("Capacity exceeded");
        }

        for (EscapeRoom a : escapeRooms) {
            if (a.getName().equals(room.getName())) {
                throw new RoomAlreadyExistsException("Room Already exists");
            }
        }

        EscapeRoom[] resultRooms = new EscapeRoom[++currentIDX];

        System.arraycopy(escapeRooms, 0, resultRooms, 0, currentIDX - 1);

        resultRooms[currentIDX - 1] = room;

        escapeRooms = resultRooms;

    }

    @Override
    public void removeEscapeRoom(String roomName) throws RoomNotFoundException {

        nameValidator(roomName);
        int remIDX = -1;
        for (int i = 0; i < currentIDX; ++i) {
            if (escapeRooms[i].getName().equals(roomName)) {
                remIDX = i;
            }
        }

        if (remIDX == -1) {
            throw new RoomNotFoundException("Could not find a room with this name");
        }


        EscapeRoom[] newRooms = new EscapeRoom[currentIDX - 1];

        if (remIDX == escapeRooms.length - 1) {
            System.arraycopy(escapeRooms, 0, newRooms, 0, remIDX);
        } else {
            System.arraycopy(escapeRooms, 0, newRooms, 0, remIDX);
            System.arraycopy(escapeRooms, remIDX, newRooms, remIDX, currentIDX - 1 - remIDX);
        }

        escapeRooms = newRooms;
        currentIDX--;
    }

    @Override
    public EscapeRoom[] getAllEscapeRooms() {

        return escapeRooms;
    }

    @Override
    public void registerAchievement(String roomName, String teamName, int escapeTime) throws RoomNotFoundException, TeamNotFoundException {
        nameValidator(roomName);
        nameValidator(teamName);
        if (escapeTime <= 0) {
            throw new IllegalArgumentException("Escape time needs to be [1,+inf]");
        }
        EscapeRoom resultRoom = null;
        for (EscapeRoom a : escapeRooms) {
            if (a.getName().equals(roomName)) {
                if (a.getMaxTimeToEscape() < escapeTime) {
                    throw new IllegalArgumentException("Escape time bigger than max room time");
                }
                resultRoom = a;
            }
        }

        if (resultRoom == null) {
            throw new RoomNotFoundException("Room with those specifications could not be found");
        }

        Team resultTeam = null;
        for (Team a : teams) {
            if (a.getName().equals(teamName)) {
                resultTeam = a;
                break;
            }
        }
        if (resultTeam == null) {
            throw new TeamNotFoundException("A team with that name could not be found");
        }

        int ratingUpdate = resultRoom.getDifficulty().getRank();

        double halfTime = (double) (resultRoom.getMaxTimeToEscape()) / 2d;
        double quarterTime = (double) (resultRoom.getMaxTimeToEscape()) - (double) (resultRoom.getMaxTimeToEscape()) / 4d;

        if (escapeTime <= halfTime) {
            ratingUpdate += 2;
        }

        if (escapeTime > halfTime && escapeTime <= quarterTime) {
            ratingUpdate++;
        }

        resultTeam.updateRating(ratingUpdate);
    }

    @Override
    public EscapeRoom getEscapeRoomByName(String roomName) throws RoomNotFoundException {
        nameValidator(roomName);

        for (EscapeRoom a : escapeRooms) {
            if (a.getName().equals(roomName)) {
                return a;
            }
        }

        throw new RoomNotFoundException("Could not find a room with that name");
    }

    @Override
    public void reviewEscapeRoom(String roomName, Review review) throws RoomNotFoundException {
        nameValidator(roomName);

        if (review == null) {
            throw new IllegalArgumentException("Review can't be null");
        }

        for (EscapeRoom a : escapeRooms) {
            if (a.getName().equals(roomName)) {
                a.addReview(review);
                return;
            }
        }

        throw new RoomNotFoundException("Room could not be found");
    }

    @Override
    public Review[] getReviews(String roomName) throws RoomNotFoundException {
        nameValidator(roomName);

        for (EscapeRoom a : escapeRooms) {
            if (a.getName().equals(roomName)) {
                return a.getReviews();
            }
        }

        throw new RoomNotFoundException("Room could not be found");
    }

    @Override
    public Team getTopTeamByRating() {
        Team result;
        if (teams == null || teams.length == 0) {
            return null;
        }
        result = teams[0];

        for (int i = 1; i < teams.length; ++i) {
            if (result.getRating() < teams[i].getRating()) {
                result = teams[i];
            }
        }
        return result;
    }

    private void nameValidator(String roomName) {
        if (roomName == null || roomName.isBlank() || roomName.isEmpty()) {
            throw new IllegalArgumentException("Illegal room name passed to method");
        }
    }
}
