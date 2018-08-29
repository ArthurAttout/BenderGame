package be.henallux.masi.bendergame.model;

public class Mode {

    private EnumMode type;
    private boolean available;

    public Mode() {
    }

    public Mode(EnumMode type, boolean available) {
        this.type = type;
        this.available = available;
    }

    public EnumMode getType() {
        return type;
    }

    public void setType(EnumMode type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
