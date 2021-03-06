import java.awt.*;
import java.io.Serializable;

public class DrawInfo implements Serializable {
    int _x1;
    int _y1;
    int _x2;
    int _y2;
    Color _clr;
    int width = 1;
    int port = 0;

    public DrawInfo(int _x1, int _y1, int _x2, int _y2, Color _clr, int width) {
        this._x1 = _x1;
        this._y1 = _y1;
        this._x2 = _x2;
        this._y2 = _y2;
        this._clr = _clr;
        this.width = width;
    }

    public DrawInfo(int _x1, int _y1, int _x2, int _y2, Color _clr, int width, int port) {
        this._x1 = _x1;
        this._y1 = _y1;
        this._x2 = _x2;
        this._y2 = _y2;
        this._clr = _clr;
        this.width = width;
        this.port = port;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public int get_x1() {
        return _x1;
    }

    public void set_x1(int _x1) {
        this._x1 = _x1;
    }

    public int get_y1() {
        return _y1;
    }

    public void set_y1(int _y1) {
        this._y1 = _y1;
    }

    public int get_x2() {
        return _x2;
    }

    public void set_x2(int _x2) {
        this._x2 = _x2;
    }

    public int get_y2() {
        return _y2;
    }

    public void set_y2(int _y2) {
        this._y2 = _y2;
    }

    public Color get_clr() {
        return _clr;
    }

    public void set_clr(Color _clr) {
        this._clr = _clr;
    }
}
