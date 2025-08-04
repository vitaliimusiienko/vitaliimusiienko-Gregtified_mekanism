package meowmel.gregmek.common.recipe;

import mekanism.api.annotations.NothingNullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@NothingNullByDefault
public class GregMekRecipeProvider implements DataProvider { // 实现 DataProvider 接口

    private final PackOutput output;
    private final ExistingFileHelper existingFileHelper;
    private final String modid;

    public GregMekRecipeProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modid) {
        this.output = output;
        this.existingFileHelper = existingFileHelper;
        this.modid = modid;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.runAsync(() -> {
            Consumer<FinishedRecipe> consumer = recipe -> {
                // 这里实际保存配方到文件
                DataProvider.saveStable(cache, recipe.serializeRecipe(),
                        output.getOutputFolder().resolve("data/" + recipe.getId().getNamespace() +
                                "/recipes/" + recipe.getId().getPath() + ".json"));
            };

            OreProgressHandler.recipeOreAddition(consumer);
        });
    }

    @Override
    public String getName() {
        return modid;
    }
}