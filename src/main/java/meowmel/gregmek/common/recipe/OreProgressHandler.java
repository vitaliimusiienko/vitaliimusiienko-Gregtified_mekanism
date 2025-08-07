package meowmel.gregmek.common.recipe;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismGases;
import meowmel.gregmek.Gregmek;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.LV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static meowmel.gregmek.common.materials.FluidStorageKeysAddition.getDirtySlurryFluid;
import static meowmel.gregmek.common.materials.FluidStorageKeysAddition.getPureSlurryFluid;
import static meowmel.gregmek.common.materials.TagPrefixAddition.*;

public class OreProgressHandler {

    public static void recipeOreAddition(Consumer<FinishedRecipe> consumer) {
        String basePath = "processing/";

        // 测试配方
        ItemStackChemicalToItemStackRecipeBuilder.purifying(
                IngredientCreatorAccess.item().from(Blocks.DIRT),
                IngredientCreatorAccess.gas().from(MekanismGases.OXYGEN, 1),
                new ItemStack(Blocks.OAK_LOG)
        ).build(consumer, Gregmek.rl(basePath + "test_dirt"));

        // 为所有矿物材料生成配方
        for (Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (material.hasProperty(PropertyKey.ORE)) {
                String materialName = material.getName();
                OreProperty property = material.getProperty(PropertyKey.ORE);
                //溶解
                ORE_WASHER_RECIPES.recipeBuilder("dissolve_ore" + material.getName())
                        .inputItems(ore, material, 1)
                        .inputFluids(SulfuricAcid.getFluid(100))
                        .outputFluids(new FluidStack(getDirtySlurryFluid(material), 1000))
                        .duration(25).EUt(VA[LV]).save(consumer);

                ORE_WASHER_RECIPES.recipeBuilder("dissolve_raw" + material.getName())
                        .inputItems(rawOre, material, 3)
                        .inputFluids(SulfuricAcid.getFluid(100))
                        .outputFluids(new FluidStack(getDirtySlurryFluid(material), 2000))
                        .duration(25).EUt(VA[LV]).save(consumer);

                //清洗
                CHEMICAL_BATH_RECIPES.recipeBuilder("pure_slurry_" + material.getName() + "_by_wash")
                        .inputFluids(new FluidStack(getDirtySlurryFluid(material), 100))
                        .inputFluids(Water.getFluid(500))
                        .outputFluids(new FluidStack(getPureSlurryFluid(material), 100))
                        .duration(10).EUt(VA[LV]).save(consumer);

                //结晶
                AUTOCLAVE_RECIPES.recipeBuilder("crystal_" + material.getName() + "_by_pure_slurry")
                        .inputFluids(new FluidStack(getPureSlurryFluid(material), 200))
                        .outputItems(ChemicalHelper.get(crystal, material, 4))
                        .duration(40).EUt(VA[LV]).save(consumer);


                /////////////////////////////////////////////////////////////////////////////
                //压射 1晶体-1碎片
                ItemStackChemicalToItemStackRecipeBuilder.injecting(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(crystal, material, 1)),
                        IngredientCreatorAccess.gas().from(MekanismGases.HYDROGEN_CHLORIDE, 1),
                        ChemicalHelper.get(shard, material, 1)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/crystal_to_shard"));

                CHEMICAL_RECIPES.recipeBuilder("injecting_" + material.getName() + "crystal_to_shard")
                        .inputItems(ore, material, 1)
                        .inputFluids(HydrochloricAcid.getFluid(200))
                        .outputItems(shard, material, 1)
                        .chancedOutput(dust,property.getOreByProduct(2, material),3000,500)
                        .duration(25).EUt(VA[LV]).save(consumer);

                //压射 1原矿-4碎片
                ItemStackChemicalToItemStackRecipeBuilder.injecting(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(TagPrefix.ore, material, 1)),
                        IngredientCreatorAccess.gas().from(MekanismGases.HYDROGEN_CHLORIDE, 1),
                        ChemicalHelper.get(shard, material, 3)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/block_to_shard"));

                CHEMICAL_RECIPES.recipeBuilder("injecting_" + material.getName() + "block_to_shard")
                        .inputItems(ore, material, 1)
                        .inputFluids(HydrochloricAcid.getFluid(200))
                        .outputItems(shard, material, 3)
                        .chancedOutput(dust,property.getOreByProduct(2, material),3000,500)
                        .duration(25).EUt(VA[LV]).save(consumer);

                //压射 3粗矿矿-8碎片
                ItemStackChemicalToItemStackRecipeBuilder.injecting(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(TagPrefix.rawOre, material, 3)),
                        IngredientCreatorAccess.gas().from(MekanismGases.HYDROGEN_CHLORIDE, 1),
                        ChemicalHelper.get(shard, material, 8)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/raw_to_shard"));

                CHEMICAL_RECIPES.recipeBuilder("injecting_" + material.getName() + "raw_to_shard")
                        .inputItems(rawOre, material, 3)
                        .inputFluids(HydrochloricAcid.getFluid(200))
                        .outputItems(shard, material, 8)
                        .chancedOutput(dust,property.getOreByProduct(2, material),3000,500)
                        .duration(25).EUt(VA[LV]).save(consumer);

                /////////////////////////////////////////////////////////////////////////////
                // 提纯仓： 碎片 -> 1x 碎块
                ItemStackChemicalToItemStackRecipeBuilder.purifying(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(shard, material, 1)),
                        IngredientCreatorAccess.gas().from(MekanismGases.OXYGEN, 1),
                        ChemicalHelper.get(clump, material, 1)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/shard_to_clump"));

                CHEMICAL_RECIPES.recipeBuilder("purifying_" + material.getName() + "shard_to_clump")
                        .inputItems(shard, material, 1)
                        .inputFluids(Oxygen.getFluid(200))
                        .outputItems(clump, material, 1)
                        .chancedOutput(dust,property.getOreByProduct(1, material),3000,500)
                        .duration(25).EUt(VA[LV]).save(consumer);

                // 提纯仓：矿 -> 3x 碎块
                ItemStackChemicalToItemStackRecipeBuilder.purifying(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(TagPrefix.ore, material, 1)),
                        IngredientCreatorAccess.gas().from(MekanismGases.OXYGEN, 1),
                        ChemicalHelper.get(clump, material, 3)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/block_to_clump"));

                CHEMICAL_RECIPES.recipeBuilder("purifying_" + material.getName() + "block_to_clump")
                        .inputItems(ore, material, 1)
                        .inputFluids(Oxygen.getFluid(200))
                        .outputItems(clump, material, 3)
                        .chancedOutput(dust,property.getOreByProduct(1, material),3000,500)
                        .duration(25).EUt(VA[LV]).save(consumer);

                // 提纯仓：粗矿 -> 2x 碎块
                ItemStackChemicalToItemStackRecipeBuilder.purifying(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(TagPrefix.rawOre, material, 1)),
                        IngredientCreatorAccess.gas().from(MekanismGases.OXYGEN, 1),
                        ChemicalHelper.get(clump, material, 2)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/raw_to_clump"));

                CHEMICAL_RECIPES.recipeBuilder("purifying_" + material.getName() + "raw_to_clump")
                        .inputItems(rawOre, material, 1)
                        .inputFluids(Oxygen.getFluid(200))
                        .outputItems(clump, material, 2)
                        .chancedOutput(dust,property.getOreByProduct(1, material),3000,500)
                        .duration(25).EUt(VA[LV]).save(consumer);
                /////////////////////////////////////////////////////////////////////////////

                // 粉碎机：碎块 -> 污浊粉
                ItemStackToItemStackRecipeBuilder.crushing(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(clump, material, 1)),
                        ChemicalHelper.get(dirtyDust, material, 1)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/clump_to_dirty"));

                MACERATOR_RECIPES.recipeBuilder("macerator_" + material.getName() + "_to_dirty_dust")
                        .inputItems(clump, material)
                        .outputItems(dirtyDust, material)
                        .chancedOutput(dust,property.getOreByProduct(0, material),3000,500)
                        .duration(8).EUt(VA[LV]).save(consumer);

                /////////////////////////////////////////////////////////////////////////////

                // 富集仓：矿 -> 2纯净粉
                ItemStackToItemStackRecipeBuilder.enriching(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(TagPrefix.ore, material, 1)),
                        ChemicalHelper.get(dust, material, 2)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/ore_to_dust"));

                // 富集仓：污浊粉 -> 纯净粉
                ItemStackToItemStackRecipeBuilder.enriching(
                        IngredientCreatorAccess.item().from(ChemicalHelper.get(dirtyDust, material, 1)),
                        ChemicalHelper.get(dust, material, 1)
                ).build(consumer, Gregmek.rl(basePath + materialName + "/dirty_to_dust"));


                CENTRIFUGE_RECIPES.recipeBuilder("centrifuge_" + material.getName() + "_ore_by_product")
                        .inputItems(dirtyDust, material)
                        .outputItems(dust, material)
                        .duration(8).EUt(VA[LV]).save(consumer);
            }
        }
    }
}
