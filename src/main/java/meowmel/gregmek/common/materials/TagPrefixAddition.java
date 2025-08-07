package meowmel.gregmek.common.materials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.Conditions.hasOreProperty;

public class TagPrefixAddition {
    public static TagPrefix dirtyDust;
    public static TagPrefix clump;
    public static TagPrefix shard;
    public static TagPrefix crystal;

    public static void init() {
        //污浊粉
        dirtyDust = new TagPrefix("dirtyDust")
                .idPattern("dirty_%s_dust")
                .defaultTagPath("dirty_dusts/%s")
                .unformattedTagPath("dirty_dusts")
                .langValue("Dirty Pile of %s Dust")
                .materialAmount(GTValues.M)
                .materialIconType(MaterialIconTypeAddition.dirtyDust)
                .unificationEnabled(true)
                .generateItem(true)
                .generationCondition(hasOreProperty);

        //碎块 clumps
        clump = new TagPrefix("clump")
                .idPattern("%s_clump")
                .defaultTagPath("clumps/%s")
                .unformattedTagPath("clumps")
                .langValue("%s Clump")
                .materialAmount(GTValues.M / 16)
                .materialIconType(MaterialIconTypeAddition.clump)
                .unificationEnabled(true)
                .generateItem(true)
                .generationCondition(hasOreProperty);

        //碎片 shards
        shard = new TagPrefix("shard")
                .idPattern("%s_shard")
                .defaultTagPath("shards/%s")
                .unformattedTagPath("shards")
                .langValue("%s Shard")
                .materialAmount(GTValues.M / 16)
                .materialIconType(MaterialIconTypeAddition.shard)
                .unificationEnabled(true)
                .generateItem(true)
                .generationCondition(hasOreProperty);

        //晶体 crystal
        crystal = new TagPrefix("crystal")
                .idPattern("%s_crystal")
                .defaultTagPath("crystals/%s")
                .unformattedTagPath("crystals")
                .langValue("%s Crystal")
                .materialAmount(GTValues.M / 16)
                .materialIconType(MaterialIconTypeAddition.crystal)
                .unificationEnabled(true)
                .generateItem(true)
                .generationCondition(hasOreProperty);
    }
}
