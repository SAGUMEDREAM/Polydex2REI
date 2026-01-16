package cc.thonly.polydex2rei.mixin;

import me.shedaniel.rei.impl.common.registry.displays.ServerDisplayRegistryImpl;
import net.minecraft.recipe.RecipeEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Comparator;

@Mixin(ServerDisplayRegistryImpl.class)
public class ServerDisplayRegistryImplMixin {
    @Shadow(remap = false)
    @Final
    private static Comparator<RecipeEntry<?>> RECIPE_COMPARATOR;

//    @Inject(method = "getAllSortedRecipes", at = @At("HEAD"), cancellable = true)
//    public void getAllSortedRecipes(CallbackInfoReturnable<List<RecipeEntry<?>>> cir) {
//        if (GameInstance.getServer() == null && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
//            MinecraftClient mc = MinecraftClient.getInstance();
//            ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
//            if (networkHandler == null) {
//                cir.setReturnValue(new ArrayList<>());
//                cir.cancel();
//                return;
//            }
//            RecipeManager recipeManager = networkHandler.getRecipeManager();
//            if (recipeManager instanceof ClientRecipeManager clientRecipeManager) {
//                SynchronizedRecipes synchronizedRecipes = clientRecipeManager.getSynchronizedRecipes();
//                Collection<RecipeEntry<?>> recipes = synchronizedRecipes.recipes();
//                cir.setReturnValue(recipes.parallelStream().sorted(RECIPE_COMPARATOR).toList());
//                cir.cancel();
//            }
//        }
//    }
}
