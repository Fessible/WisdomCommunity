//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.com.wisdomcommunity.util.photo;


import com.example.com.wisdomcommunity.util.photo.keep.KeepStorageManager;

class Platform {
    Platform() {
    }

    public static StorageManager findPlatform() {
        return new KeepStorageManager();
    }
}
