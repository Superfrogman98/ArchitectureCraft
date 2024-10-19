package gcewing.architecture.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import gcewing.architecture.ArchitectureContent;
import gcewing.architecture.ArchitectureCraft;

public class CreativeTab {

    public static final CreativeTabs AC_TAB = new CreativeTabs(ArchitectureCraft.MOD_NAME) {

        @Override
        public Item getTabIconItem() {
            return ArchitectureContent.itemHammer;
        }
    };
}
