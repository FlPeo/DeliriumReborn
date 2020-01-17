package model;

public interface Fallable {
    int DELAY = 5;
    boolean isFalling();
    void stopFalling();
    boolean fallTo(int x, int y);
}
