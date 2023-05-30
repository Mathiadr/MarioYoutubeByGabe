package no.brunostengine;

import no.brunostengine.Sprite;

class Frame {
    public Sprite sprite;
    public float frameTime;

    public Frame() {

    }

    public Frame(Sprite sprite) {
        this.sprite = sprite;
        this.frameTime = 0.1f;
    }

    public Frame(Sprite sprite, float frameTime) {
        this.sprite = sprite;
        this.frameTime = frameTime;
    }
}
