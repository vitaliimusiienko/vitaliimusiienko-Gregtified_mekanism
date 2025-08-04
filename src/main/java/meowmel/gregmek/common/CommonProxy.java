package meowmel.gregmek.common;

import com.gregtechceu.gtceu.GTCEu;
import meowmel.gregmek.common.materials.TagPrefixAddition;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class CommonProxy {
    public CommonProxy() {
        init();
    }

    public static void init() {
        GTCEu.LOGGER.info("GregMek common proxy init!");
        initMaterials();

    }


    private static void initMaterials() {
        TagPrefixAddition.init();


    }

    @SubscribeEvent
    public void loadComplete(FMLLoadCompleteEvent e) {

    }
}
