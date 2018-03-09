package com.zaaach.citypicker.model;

public class LocatedCity extends City {

    public LocatedCity(String name, String province, String code) {
        super(name, province, "定位城市", code);
    }

    public LocatedCity(String name) {
        super(name, null, "定位城市", null);
    }
}
