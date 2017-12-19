import java.awt.*;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by dayan on 19.12.2017.
 */
public class DrawInfo implements Serializable {
    int _x;
    int _y;
    Color _clr;
    int port = 0;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DrawInfo(int _x, int _y, Color _clr) {
        this._x = _x;
        this._y = _y;
        this._clr = _clr;
    }

    public DrawInfo(int _x, int _y, Color _clr, int port) {
        this._x = _x;
        this._y = _y;
        this._clr = _clr;
        this.port = port;
    }

    public int get_x() {
        return _x;
    }

    public void set_x(int _x) {
        this._x = _x;
    }

    public int get_y() {
        return _y;
    }

    public void set_y(int _y) {
        this._y = _y;
    }

    public Color get_clr() {
        return _clr;
    }

    public void set_clr(Color _clr) {
        this._clr = _clr;
    }
}
