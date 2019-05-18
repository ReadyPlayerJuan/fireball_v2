package com.fireball.game.textures;

public enum TextureData {
    TEST_IMAGE ("images/badlogic.jpg"),
    ROUNDED_RECT ("images/rounded_rect.png"),
    GROUND_SMALL ("images/ground_small.png", 4, 4),
    GROUND_BIG ("images/ground_big.png", 4, 4),
    WALLS ("images/walls.png", 8, 8),
    WALLS_GRAY ("images/walls_gray.png", 8, 8);

    private String fileName;
    private int sheetRows, sheetCols;

    TextureData(String fileName) {
        this.fileName = fileName;
        sheetRows = 1;
        sheetCols = 1;
    }

    TextureData(String fileName, int sheetRows, int sheetCols) {
        this.fileName = fileName;
        this.sheetRows = sheetRows;
        this.sheetCols = sheetCols;
    }

    public String getFileName() {
        return fileName;
    }

    public void load() {
        TextureManager.loadTexture(this, sheetRows, sheetCols);
    }
}
