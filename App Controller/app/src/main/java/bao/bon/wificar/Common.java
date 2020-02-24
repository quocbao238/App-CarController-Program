package bao.bon.wificar;

public class Common {
    String speed;
    String direction;
    String angle;

    public Common(String speed, String direction, String angle) {
        this.speed = speed;
        this.direction = direction;
        this.angle = angle;
    }

    public Common(String speed, String direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
