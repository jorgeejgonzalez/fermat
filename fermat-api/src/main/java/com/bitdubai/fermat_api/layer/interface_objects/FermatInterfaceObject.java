package com.bitdubai.fermat_api.layer.interface_objects;

import java.util.List;

/**
 * Created by mati on 2015.11.01..
 */
public interface FermatInterfaceObject{

    InterfaceType getType();

    String getName();

    String getIcon();

    void setIconResource(int iconRes);

    int getIconResource();

    int getPosition();

    void setPosition(int position);


}
