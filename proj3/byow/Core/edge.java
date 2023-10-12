package byow.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * edge class
 */
public class edge implements Comparable<edge> {
    Position p;
    Position p1;

    double distance;


    public edge(Position p, Position p1) {
        this.p = p;
        this.p1 = p1;
        distance =  Position.getDistanceByPosition(p, p1);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        edge other = (edge) o;
        return (this.p.equals(other.p) && this.p1.equals(other.p1)) || (this.p.equals(other.p1) && this.p1.equals(other.p));
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, p1) + Objects.hash(p1, p);
    }
    @Override
    public int compareTo(edge o) {
        return  Double.compare(this.distance, o.distance);
    }
}
