public class Cell<T> {

    private int x;
    private int y;
    private T content;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.content = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean hasContent() {
        return content != null;
    }
}