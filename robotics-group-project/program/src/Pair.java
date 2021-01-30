/*
    Pair class to act as coordinate set data structure, for Semester2 project.
*/
public class Pair {
    
    public int x;
    public int y;
    
    public Pair(int nX, int nY)
    {
        this.x = nX;
        this.y = nY;
    }
    
    public String toString()
    {
        return "("+x+","+y+")";
    }
}