package gcewing.architecture.client.gui.widget;

import net.minecraft.util.StatCollector;

public enum GuiText {

    // UI Text
    Sawbench,
    Makes,

    // Page Names
    Roofing,
    Rounded,
    Classical,
    Window,
    Arches,
    Railings,
    Other,

    Glow,
    Curves,

    DoubleSlopes,

    SlopesA,
    SlopesB,
    SlopesC,

    // Tiles
    RoofTile,
    RoofOuterCorner,
    RoofInnerCorner,
    RoofRidge,
    RoofSmartRidge,
    RoofValley,
    RoofSmartValley,

    RoofOverhang,
    RoofOverhangOuterCorner,
    RoofOverhangInnerCorner,

    Cylinder,
    CylinderHalf,
    CylinderQuarter,
    CylinderLargeQuarter,
    AnticylinderLargeQuarter,
    Pillar,
    Post,
    Pole,

    BevelledOuterCorner,
    BevelledInnerCorner,

    PillarBase,
    DoricCapital,
    IonicCapital,
    CorinthianCapital,
    DoricTriglyph,
    DoricTriglyphCorner,
    DoricMetope,
    Architrave,
    ArchitraveCorner,

    WindowFrame,
    WindowCorner,
    WindowMullion,

    SphereFull,
    SphereHalf,
    SphereQuarter,
    SphereEighth,
    SphereEighthLarge,
    SphereEighthLargeRev,

    RoofOverhangGableLH,
    RoofOverhangGableRH,
    RoofOverhangGableEndLH,
    RoofOverhangGableEndRH,
    RoofOverhangRidge,
    RoofOverhangValley,

    CorniceLH,
    CorniceRH,
    CorniceEndLH,
    CorniceEndRH,
    CorniceRidge,
    CorniceValley,
    CorniceBottom,

    CladdingSheet,

    ArchD1,
    ArchD2,
    ArchD3A,
    ArchD3B,
    ArchD3C,
    ArchD4A,
    ArchD4B,
    ArchD4C,

    BanisterPlainBottom,
    BanisterPlain,
    BanisterPlainTop,

    BalustradeFancy,
    BalustradeFancyCorner,
    BalustradeFancyWithNewel,
    BalustradeFancyNewel,

    BalustradePlain,
    BalustradePlainOuterCorner,
    BalustradePlainWithNewel,

    BanisterPlainEnd,

    BanisterFancyNewelTall,

    BalustradePlainInnerCorner,
    BalustradePlainEnd,

    BanisterFancyBottom,
    BanisterFancy,
    BanisterFancyTop,
    BanisterFancyEnd,

    BanisterPlainInnerCorner,

    Slab,
    Stairs,
    StairsOuterCorner,
    StairsInnerCorner,

    SlopeTileA1,
    SlopeTileA2,
    SlopeTileB1,
    SlopeTileB2,
    SlopeTileB3,
    SlopeTileC1,
    SlopeTileC2,
    SlopeTileC3,
    SlopeTileC4,

    AngledRoofRidge,
    SquareSE,
    SlabSE,
    RoofTileSE,
    SlopeTileA1SE,
    SlopeTileA2SE,
    SlopeTileB1SE,
    SlopeTileB2SE,
    SlopeTileB3SE,
    SlopeTileC1SE,
    SlopeTileC2SE,
    SlopeTileC3SE,
    SlopeTileC4SE,
    DoubleRoofTile,

    Curve2b2A,
    Curve2b2B,

    DoubleSlopeAStart,
    DoubleSlopeAEnd,
    DoubleSlopeBStart,
    DoubleSlopeBMiddle,
    DoubleSlopeBEnd,
    DoubleSlopeC1,
    DoubleSlopeC2,
    DoubleSlopeC3,
    DoubleSlopeC4,

    // Slope Corners
    StraightCornerA1,
    StraightCornerA2,
    StraightCornerB1,
    StraightCornerB2,
    StraightCornerB3,
    StraightCornerC1,
    StraightCornerC2,
    StraightCornerC3,
    StraightCornerC4,

    DiagonalCornerA1,
    DiagonalCornerA2,
    DiagonalCornerB1,
    DiagonalCornerB2,
    DiagonalCornerB3,
    DiagonalCornerC1,
    DiagonalCornerC2,
    DiagonalCornerC3,
    DiagonalCornerC4,

    BevelledCornerA1,
    BevelledCornerA2,
    BevelledCornerB1,
    BevelledCornerB2,
    BevelledCornerB3,
    BevelledCornerC1,
    BevelledCornerC2,
    BevelledCornerC3,
    BevelledCornerC4,


    // UI Colors
    FontColor(0x404040),
    SelectedBgColor(0x66CCFF);

    private final String root;
    private final int color;

    GuiText() {
        this.root = "gui.architecturecraft";
        this.color = 0x000000;
    }

    GuiText(final String s) {
        this.root = s;
        this.color = 0x000000;
    }

    GuiText(final int hex) {
        this.root = "gui.architecturecraft";
        this.color = hex;
    }

    public int getColor() {
        String hex = StatCollector.translateToLocal(this.getUnlocalized());
        int color = this.color;
        if (hex.length() <= 6) {
            try {
                color = Integer.parseUnsignedInt(hex, 16);
            } catch (final NumberFormatException ignored) {

            }
        }
        return color;
    }

    public String getLocal() {
        return StatCollector.translateToLocal(this.getUnlocalized());
    }

    public String getUnlocalized() {
        return this.root + '.' + this;
    }
}
