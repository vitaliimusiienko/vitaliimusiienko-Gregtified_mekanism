package meowmel.gregmek.common.materials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.api.fluids.FluidState;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKey;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class FluidStorageKeysAddition {
    public static final TagKey<Fluid> PURE_SLURRY_FLUIDS = TagUtil.createFluidTag("pure_slurry");
    public static final TagKey<Fluid> DIRTY_SLURRY_FLUIDS = TagUtil.createFluidTag("dirty_slurry");
    //pureSlurry
    public static final FluidStorageKey PURE_SLURRY = new FluidStorageKey(GTCEu.id("pure_slurry"), PURE_SLURRY_FLUIDS,
            MaterialIconType.liquid,
            m -> "pure_slurry_" + m.getName(),
            m -> "gtceu.fluid.pure_slurry",
            FluidState.LIQUID, -1);

    //污浊浆液
    public static final FluidStorageKey DIRTY_SLURRY = new FluidStorageKey(GTCEu.id("dirty_slurry"), DIRTY_SLURRY_FLUIDS,
            MaterialIconType.molten,
            m -> "dirty_slurry" + m.getName(),
            m -> "gtceu.fluid.dirty_slurry",
            FluidState.LIQUID, -1);


    public static Fluid getPureSlurryFluid(Material material) {
        return material.getProperty(PropertyKey.FLUID).getStorage().get(FluidStorageKeysAddition.PURE_SLURRY);
    }
    public static Fluid getDirtySlurryFluid(Material material) {
        return material.getProperty(PropertyKey.FLUID).getStorage().get(FluidStorageKeysAddition.DIRTY_SLURRY);
    }
}
