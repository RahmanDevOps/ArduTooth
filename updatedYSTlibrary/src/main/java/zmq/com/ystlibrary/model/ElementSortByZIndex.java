package zmq.com.ystlibrary.model;

import java.util.Comparator;

import zmq.com.ystlibrary.sprite.ShahSprite;

public class ElementSortByZIndex implements Comparator<ShahSprite> {
    @Override
    public int compare(ShahSprite o1, ShahSprite o2) {
        return o1.getZ()-o2.getZ();
    }
}
