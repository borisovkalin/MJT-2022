package bg.sofia.uni.fmi.mjt.escaperoom.team;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class Team implements Ratable {

    private final String name;
    private final TeamMember[] members;

    private int rating_points;

    private Team(String name, TeamMember[] members) {
        this.name = name;
        this.members = members;
    }

    public static Team of(String name, TeamMember[] members) {
        return new Team(name, members);
    }

    /**
     * Updates the team rating by adding the specified points to it.
     *
     * @param points the points to be added to the team rating.
     * @throws IllegalArgumentException if the points are negative.
     */
    public void updateRating(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Additional rating points need to be >= 0");
        }

        this.rating_points += points;
    }

    @Override
    public double getRating() {
        return rating_points;
    }

    /**
     * Returns the team name.
     */
    public String getName() {
        return name;
    }

    public TeamMember[] getMembers() {
        return members;
    }
}
